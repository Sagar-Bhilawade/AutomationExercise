package com.sagar.automation.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading test data from Excel files using a two-sheet structure.
 * - 'TestParameters' sheet defines the schema (parameter names) for each test method.
 * - 'TestData' sheet contains the actual data values, linked by 'TestName'.
 * The Excel file path is read internally from config.properties.
 */
public class ExcelReader {
    private static final Logger log = LogManager.getLogger(ExcelReader.class.getName());

    // Fixed sheet names for parameter and data sheets
    private static final String PARAM_SHEET_NAME = "TestParameters";
    private static final String DATA_SHEET_NAME = "TestData";
    private static final String TEST_NAME_COLUMN = "TestName"; // Column header in both sheets to link data

    // Cache to store all parsed data to avoid re-reading the Excel file for every data provider call
    private static Map<String, List<Map<String, String>>> cachedAllTestData = new LinkedHashMap<>();
    private static String lastReadFilePath = null; // To track if the file has changed

    /**
     * Constructs the full path to the Excel test data file based on config.properties.
     *
     * @return The full file path as a String.
     */
    private static String getExcelFilePathFromConfig() {
        ConfigReader.initializeProperties();
        String excelFileName = ConfigReader.getProperty("test.data.excel.file");
        if (excelFileName == null || excelFileName.isEmpty()) {
            log.error("Property 'test.data.excel.file' not found or is empty in config.properties.");
            throw new RuntimeException("Excel test data file name not configured in config.properties.");
        }
        String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator + excelFileName;
        log.debug("Constructed Excel file path: {}", filePath);
        return filePath;
    }

    /**
     * Reads and parses test data from the Excel file using the two-sheet structure.
     * This method is private as it's an internal helper to fetch all raw data.
     *
     * @return A Map where keys are TestNG test method names and values are
     * Lists of Maps. Each inner Map represents a single data set (row) for that test,
     * with parameter names as keys and data values as values.
     * @throws IOException              If there is an error reading the Excel file.
     * @throws IllegalArgumentException If sheets are not found, or required headers are missing/incorrect.
     */
    private static Map<String, List<Map<String, String>>> parseExcelData() throws IOException {
        String filePath = getExcelFilePathFromConfig();
        log.info("Parsing Excel data from: '{}' using two-sheet structure.", filePath);

        // Check if data is already cached and file path hasn't changed
        if (filePath.equals(lastReadFilePath) && !cachedAllTestData.isEmpty()) {
            log.info("Returning cached Excel data for: '{}'", filePath);
            return cachedAllTestData;
        }

        FileInputStream fis = null;
        Workbook workbook = null;
        Map<String, List<Map<String, String>>> allTestsData = new LinkedHashMap<>();

        try {
            File excelFile = new File(filePath);
            if (!excelFile.exists()) {
                log.error("Excel file not found at path: '{}'", filePath);
                throw new IOException("Excel file not found: " + filePath);
            }

            fis = new FileInputStream(excelFile);
            workbook = new XSSFWorkbook(fis);

            // --- Phase 1: Read TestParameters Sheet (Schema Definition) ---
            Sheet paramSheet = workbook.getSheet(PARAM_SHEET_NAME);
            if (paramSheet == null) {
                log.error("Parameter sheet '{}' not found in Excel file: '{}'", PARAM_SHEET_NAME, filePath);
                throw new IllegalArgumentException("Required sheet '" + PARAM_SHEET_NAME + "' not found.");
            }

            Map<String, List<String>> testMethodSchemaMap = new LinkedHashMap<>(); // TestName -> List of Param Keys (schema)

            Row paramHeaderRow = paramSheet.getRow(0);
            if (paramHeaderRow == null || !getCellValueAsString(paramHeaderRow.getCell(0)).equalsIgnoreCase(TEST_NAME_COLUMN)) {
                log.error("Parameter sheet '{}' must have '{}' as the first column header.", PARAM_SHEET_NAME, TEST_NAME_COLUMN);
                throw new IllegalArgumentException("Parameter sheet header invalid. First column must be '" + TEST_NAME_COLUMN + "'.");
            }

            // Read schema definitions from TestParameters sheet
            for (int i = 1; i <= paramSheet.getLastRowNum(); i++) {
                Row currentRow = paramSheet.getRow(i);
                if (currentRow == null) continue;

                Cell testNameCell = currentRow.getCell(0);
                String testName = getCellValueAsString(testNameCell).trim();

                if (testName.isEmpty()) {
                    log.warn("Skipping row {} in parameter sheet as TestName is empty.", i + 1);
                    continue;
                }

                List<String> paramKeys = new ArrayList<>();
                for (int j = 1; j < paramHeaderRow.getPhysicalNumberOfCells(); j++) { // Start from 1 to skip TestName
                    Cell paramKeyCell = paramHeaderRow.getCell(j);
                    String paramKey = getCellValueAsString(paramKeyCell).trim();
                    if (!paramKey.isEmpty()) {
                        paramKeys.add(paramKey);
                    } else {
                        // Stop reading parameters if an empty cell is encountered in the schema row
                        break;
                    }
                }
                if (!paramKeys.isEmpty()) {
                    testMethodSchemaMap.put(testName, paramKeys);
                    log.debug("Loaded schema for TestName '{}': {}", testName, paramKeys);
                } else {
                    log.warn("No parameters defined for TestName '{}' in parameter sheet. Skipping.", testName);
                }
            }
            if (testMethodSchemaMap.isEmpty()) {
                log.warn("No valid test method schema definitions found in sheet '{}'.", PARAM_SHEET_NAME);
            }

            // --- Phase 2: Read TestData Sheet (Actual Values) and Combine with Schema ---
            Sheet dataSheet = workbook.getSheet(DATA_SHEET_NAME);
            if (dataSheet == null) {
                log.error("Data sheet '{}' not found in Excel file: '{}'", DATA_SHEET_NAME, filePath);
                throw new IllegalArgumentException("Required sheet '" + DATA_SHEET_NAME + "' not found.");
            }

            Row dataHeaderRow = dataSheet.getRow(0);
            if (dataHeaderRow == null || !getCellValueAsString(dataHeaderRow.getCell(0)).equalsIgnoreCase(TEST_NAME_COLUMN)) {
                log.error("Data sheet '{}' must have '{}' as the first column header.", DATA_SHEET_NAME, TEST_NAME_COLUMN);
                throw new IllegalArgumentException("Data sheet header invalid. First column must be '" + TEST_NAME_COLUMN + "'.");
            }

            // Map of actual column headers in the data sheet to their indices
            Map<String, Integer> dataHeaderMap = new LinkedHashMap<>();
            for (int j = 0; j < dataHeaderRow.getPhysicalNumberOfCells(); j++) {
                String headerName = getCellValueAsString(dataHeaderRow.getCell(j)).trim();
                if (!headerName.isEmpty()) {
                    dataHeaderMap.put(headerName, j);
                }
            }
            log.debug("Data sheet headers: {}", dataHeaderMap.keySet());


            // Iterate through data sheet rows to get actual values
            for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
                Row currentRow = dataSheet.getRow(i);
                if (currentRow == null) continue;

                Cell testNameCell = currentRow.getCell(0);
                String testName = getCellValueAsString(testNameCell).trim();

                if (testName.isEmpty()) {
                    log.warn("Skipping row {} in data sheet as TestName is empty.", i + 1);
                    continue;
                }

                List<String> expectedParamKeys = testMethodSchemaMap.get(testName);
                if (expectedParamKeys == null || expectedParamKeys.isEmpty()) {
                    log.warn("TestName '{}' found in data sheet but no schema defined in '{}'. Skipping row {}.", testName, PARAM_SHEET_NAME, i + 1);
                    continue;
                }

                Map<String, String> rowDataMap = new LinkedHashMap<>();
                rowDataMap.put(TEST_NAME_COLUMN, testName); // Include TestName in the data map

                boolean isRowDataMeaningful = false;
                // For each expected parameter key from the schema, find its value in the data row
                for (String paramKey : expectedParamKeys) {
                    Integer colIndex = dataHeaderMap.get(paramKey);
                    String cellValue = "";
                    if (colIndex != null) {
                        Cell dataCell = currentRow.getCell(colIndex);
                        cellValue = getCellValueAsString(dataCell);
                    } else {
                        log.warn("Parameter key '{}' for TestName '{}' not found in Data sheet headers. Value will be empty.", paramKey, testName);
                    }
                    rowDataMap.put(paramKey, cellValue);
                    if (!cellValue.isEmpty()) {
                        isRowDataMeaningful = true;
                    }
                }

                if (isRowDataMeaningful) {
                    allTestsData.putIfAbsent(testName, new ArrayList<>());
                    allTestsData.get(testName).add(rowDataMap);
                    log.debug("Added data set for '{}': {}", testName, rowDataMap);
                } else {
                    log.warn("Row {} for TestName '{}' in data sheet contains only empty values for defined parameters. Skipping.", i + 1, testName);
                }
            }
            log.info("Successfully parsed {} total combined data rows from Excel.", allTestsData.values().stream().mapToInt(List::size).sum()); // Sum of all data sets

            cachedAllTestData = allTestsData; // Cache the parsed data
            lastReadFilePath = filePath;
            return allTestsData;

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error("Error closing workbook: {}", e.getMessage(), e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("Error closing FileInputStream: {}", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Retrieves test data for a specific test method from the parsed Excel data.
     * This method is designed to be called by TestNG's @DataProvider.
     *
     * @param testMethodName The name of the TestNG test method for which to retrieve data.
     * @return An Object[][] containing test data for the specified method. Each inner array
     * contains a single Map<String, String> representing one data set.
     * @throws IOException              If there is an error reading the Excel file.
     * @throws IllegalArgumentException If the Excel format is incorrect.
     */
    public static Object[][] getTestData(String testMethodName) throws IOException { // Removed filePath parameter
        log.info("Fetching data for test method: '{}' from Excel.", testMethodName);
        Map<String, List<Map<String, String>>> allData = parseExcelData(); // Call the internal helper

        List<Map<String, String>> methodData = allData.getOrDefault(testMethodName, new ArrayList<>());

        if (methodData.isEmpty()) {
            log.warn("No data found in Excel for test method: '{}'. Returning empty data set.", testMethodName);
        } else {
            log.info("Found {} data sets for test method: '{}'.", methodData.size(), testMethodName);
        }

        // Convert List<Map<String, String>> to Object[][] for DataProvider
        Object[][] result = new Object[methodData.size()][1];
        for (int i = 0; i < methodData.size(); i++) {
            result[i][0] = methodData.get(i);
        }
        return result;
    }

    /**
     * Helper method to get cell value as String, handling various cell types.
     *
     * @param cell The Excel cell. Can be null.
     * @return The cell value as a trimmed String, or an empty string if the cell is null or blank.
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new DataFormatter().formatCellValue(cell);
                } else {
                    return new DataFormatter().formatCellValue(cell);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case STRING:
                        return cellValue.getStringValue().trim();
                    case NUMERIC:
                        return new DataFormatter().formatCellValue(cell);
                    case BOOLEAN:
                        return String.valueOf(cellValue.getBooleanValue());
                    default:
                        return "";
                }
            case BLANK:
                return "";
            default:
                log.warn("Unhandled cell type for cell at row {} col {}: {}", cell.getRowIndex(), cell.getColumnIndex(), cell.getCellType());
                return "";
        }
    }
}
