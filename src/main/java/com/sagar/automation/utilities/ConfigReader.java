package com.sagar.automation.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    //Defines path to configuration file
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    //    Logger instance for internal logging within this class (e.g success/failure for loading config.properties)
    private static final Logger log = LogManager.getLogger(ConfigReader.class.getName());
    //    'Properties' will hold all the key-value pairs loaded from config.properties
//    Created static to have only one instance and shared across all calls, improving efficiency
    private static Properties properties;

    /**
     * initializes the properties object by loading the config.properties file
     * This method should be called once, typically at the start of test-suite or test-setup
     */
    public static void initializeProperties() {
        properties = new Properties();
        try(FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) { //Open file input stream
            properties.load(fis); //Load properties from file into the 'properties' object
            log.info("Configuration properties loaded successfully from : "+CONFIG_FILE_PATH);
        } catch (FileNotFoundException e) {
            //Handles the case where config.properties file does not exist
            log.error("Configuartion file not found: "+CONFIG_FILE_PATH+" . Please ensure it exists.", e);
            throw new RuntimeException("Configuration file not found: " + CONFIG_FILE_PATH, e);
        } catch (IOException e) {
            //Handles general I/O errors during file reading
            log.error("Error loading configuration properties from: "+CONFIG_FILE_PATH);
            throw new RuntimeException("Error loading configuration properties", e);
        }
    }

    /**
     * Retrieves a property value by its key from the loaded configuration
     *
     * @param key  The key of the property to retrieve
     * @return  The String value of the property, or null if the key is not found
     */
    public static String getProperty(String key){
        if(properties == null){
            log.warn("Properties are not initialized. Calling initializeProperties(). This should be ideally done once at startup.");
            initializeProperties();
        }
        String value = properties.getProperty(key);
        if(value == null){
            log.warn("Property  '" + key + "' not found in config.properties. Returning null.");
        }
        return value;
    }
}
