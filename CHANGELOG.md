# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2024-01-XX

### Added
- **Product Testing Suite**: Complete product browsing, search, and cart functionality
- **API Testing Framework**: REST Assured integration for backend API validation
- **Contact Us Testing**: Form submission and validation testing
- **Navigation Testing**: Comprehensive header and browser navigation tests
- **Security Testing**: SQL injection and XSS attack prevention tests
- **Cross-Browser Testing**: Multi-browser compatibility testing
- **Responsive Design Testing**: Viewport size validation
- **Enhanced Page Objects**: ProductsPage, CartPage, ContactUsPage, ProductDetailPage
- **TestNG Suite Configuration**: Organized test execution with testng.xml
- **Negative Testing Suite**: Edge cases and error handling validation
- **Performance Testing**: Network condition and load time testing

### Enhanced
- **BaseTest Class**: Improved screenshot handling and ExtentReports integration
- **Data-Driven Testing**: Enhanced Excel reader with two-sheet structure
- **Logging Framework**: Comprehensive Log4j2 integration across all components
- **Error Handling**: Robust exception handling and validation
- **Documentation**: Updated README with comprehensive setup instructions

### Fixed
- Screenshot capture timing issues in BaseTest
- WebDriver initialization sequence
- Excel data reading for complex test scenarios
- ExtentReports thread safety issues

## [1.0.0] - 2024-01-XX

### Added
- **Initial Framework Setup**: Maven-based Selenium WebDriver framework
- **Page Object Model**: BasePage and initial page implementations
- **Login Testing**: Valid and invalid credential testing
- **User Registration**: Complete signup flow with account deletion
- **Configuration Management**: Properties-based configuration system
- **Reporting**: ExtentReports integration with screenshots
- **Data Management**: Excel-based test data management
- **Logging**: Log4j2 integration for comprehensive logging

### Framework Components
- **BaseTest**: Core test setup and teardown functionality
- **BasePage**: Common page interaction methods
- **ConfigReader**: Configuration file management
- **ExcelReader**: Test data extraction from Excel files
- **HomePage, LoginPage, SignupPage**: Initial page object implementations

### Dependencies
- Selenium WebDriver 4.34.0
- TestNG 7.11.0
- ExtentReports 5.1.2
- Apache POI 5.4.1
- Log4j2 2.25.0
- REST Assured 5.5.5