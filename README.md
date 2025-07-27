# Automation Exercise UI Automation Framework

This project provides a robust and scalable UI automation framework built with Selenium WebDriver, TestNG, and Maven, designed to automate test cases on the [Automation Exercise website](http://automationexercise.com/). It incorporates best practices such as the Page Object Model (POM), data-driven testing using Excel, comprehensive logging with Log4j, and detailed reporting with ExtentReports.

## ✨ Key Features

* **Selenium WebDriver:** Automates browser interactions for various web elements.
* **TestNG Framework:** Used for test management, parallel execution, and flexible test reporting.
* **Page Object Model (POM):** Enhances test maintainability and reduces code duplication by representing web pages as classes.
* **Data-Driven Testing:**
    * Test data is externalized in an Excel file (`.xlsx`).
    * Supports a two-sheet structure (`TestParameters` for schema, `TestData` for values) allowing flexible parameter definitions for different test cases.
    * Data is dynamically fetched by TestNG's `@DataProvider` based on the `@Test` method name.
* **ExtentReports:** Generates rich, interactive HTML reports with step-by-step execution details and screenshots.
* **Configurable Screenshots:** Option to take screenshots after each test step, controlled via `config.properties`.
* **Log4j2 Integration:** Provides comprehensive logging for debugging and traceability.
* **Maven Build Tool:** Manages project dependencies, builds, and test execution.

## 🚀 Getting Started

### Prerequisites

* **Java Development Kit (JDK) 17 or higher:** [Download](https://www.oracle.com/java/technologies/javase-downloads.html) & Install JDK
* **Apache Maven:** [Download & Install Maven](https://maven.apache.org/download.cgi)
* **Web Browser:** Chrome, Firefox, or Edge (ensure you have the corresponding browser driver).

### Project Setup

1.  **Clone the Repository:**
    ```bash
    git clone <your-repository-url>
    cd <your-project-folder>
    ```

2.  **Configure `config.properties`:**
    Navigate to `src/main/resources/config.properties` (or `src/test/resources/config.properties` if that's where you placed it) and update the following properties:
    ```properties
    # Browser to use (chrome, firefox, edge)
    browser=chrome
    # Base URL of the application under test
    url.automationExercise=[http://automationexercise.com](http://automationexercise.com)
    # Implicit wait in seconds
    implicit.wait=10
    # Page load timeout in seconds
    page.load.timeout=30
    # Set to true to capture screenshot after each step in ExtentReports
    screenshot.after.each.step=true
    # Name of your Excel test data file (e.g., LoginData.xlsx)
    test.data.excel.file=LoginData.xlsx
    # User credentials (for initial setup if not using Excel for all login tests)
    user.email=your_registered_email@example.com
    user.password=your_password
    ```

3.  **Prepare Excel Test Data:**
    Create an Excel file (e.g., `LoginData.xlsx`) in the `src/test/resources/testdata/` directory with two sheets:

    * **Sheet 1: `TestParameters`**
        This sheet defines the schema (parameter names) for each test method.
    * **Sheet 2: `TestData`**
        This sheet contains the actual data values. The headers in this sheet **must match** the parameter names defined in the `TestParameters` sheet.
        *(Remember to use unique emails for registration tests, e.g., by appending a timestamp or random string in your test code if running multiple times.)*

4.  **Install Dependencies:**
    Open your terminal or command prompt in the project's root directory and run:
    ```bash
    mvn clean install
    ```
    This will download all necessary Maven dependencies (Selenium, TestNG, Apache POI, ExtentReports, Log4j) and build the project.

### Running Tests

You can run tests using Maven commands.

* **Run all tests:**
    ```bash
    mvn test
    ```

* **Run a specific test class:**
    ```bash
    mvn test -Dtest=<YourTestClassName> # e.g., mvn test -Dtest=LoginTests
    ```

* **Run a specific test method:**
    ```bash
    mvn test -Dtest=<YourTestClassName>#<YourTestMethodName> # e.g., mvn test -Dtest=LoginTests#LoginTest
    ```

## 📊 Reporting

After test execution, ExtentReports generates a comprehensive HTML report.

* **Report Location:** The report will be generated in the `reports/` directory at your project root.
* **File Name:** `ExtentReport_YYYYMMDD_HHMMSS.html` (e.g., `ExtentReport_20240727_203000.html`).
* **To View:** Open this HTML file in any web browser.

If `screenshot.after.each.step=true` in `config.properties`, you will see screenshots embedded directly into the report for each test step.

## 📁 Project Structure (High-Level)

```

.
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── sagar
│   │               └── automation
│   │                   └── base          \# Base classes (BaseTest, BasePage)
│   │                   └── utilities     \# Helper classes (ConfigReader, ExcelReader)
│   └── test
│       ├── java
│       │   └── com
│       │       └── sagar
│       │           └── automation
│       │               └── pages         \# Page Object Model classes
│       │               └── tests         \# TestNG test classes (LoginTests, RegisterUserTests)
│       └── resources
│           ├── config.properties         \# Framework configuration
│           └── testdata                  \# Excel test data files
│               └── LoginData.xlsx
├── reports                               \# Generated ExtentReports and screenshots
│   ├── ExtentReport\_YYYYMMDD\_HHMMSS.html
│   └── screenshots
├── pom.xml                               \# Maven Project Object Model
└── README.md

```

## 🤝 Contributing

Feel free to fork this repository, submit pull requests, or open issues for any suggestions or bug reports.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. *(You might need to create a LICENSE file if you don't have one)*
