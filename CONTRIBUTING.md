# Contributing to ECommerce Automation Framework

## Getting Started

1. Fork the repository
2. Clone your fork locally
3. Create a feature branch: `git checkout -b feature/your-feature-name`
4. Make your changes
5. Run tests to ensure everything works
6. Commit your changes with descriptive messages
7. Push to your fork and submit a pull request

## Development Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Chrome/Firefox/Edge browser
- Git

### Installation
```bash
git clone https://github.com/yourusername/ECommerceAutomation.git
cd ECommerceAutomation
mvn clean install
```

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LoginTests

# Run with specific browser
mvn test -Dbrowser=firefox

# Run TestNG suite
mvn test -DsuiteXmlFile=testng.xml
```

## Code Standards

### Page Object Model
- Each page should extend `BasePage`
- Use descriptive locator names
- Implement proper wait strategies
- Add comprehensive logging

### Test Classes
- Extend `BaseTest`
- Use data-driven approach with Excel
- Add proper assertions and logging
- Include ExtentReports logging

### Naming Conventions
- Test methods: `descriptiveTestNameTest()`
- Page classes: `PageNamePage`
- Locators: `ELEMENT_NAME_LOCATOR`
- Variables: `camelCase`

### Documentation
- Add JavaDoc comments for public methods
- Update README.md for new features
- Include test data examples

## Pull Request Guidelines

1. Ensure all tests pass
2. Add tests for new functionality
3. Update documentation
4. Follow existing code style
5. Write clear commit messages
6. Reference issue numbers if applicable

## Reporting Issues

When reporting bugs, please include:
- Browser and version
- Operating system
- Steps to reproduce
- Expected vs actual behavior
- Screenshots if applicable
- Log files

## Testing Guidelines

### Test Categories
- **Smoke Tests**: Critical functionality
- **Functional Tests**: Feature-specific tests
- **API Tests**: Backend service validation
- **Negative Tests**: Error handling
- **Cross-Browser**: Compatibility testing

### Test Data Management
- Use Excel files for test data
- Follow the two-sheet structure (TestParameters, TestData)
- Include both positive and negative test data
- Keep sensitive data in config.properties

## Code Review Checklist

- [ ] Code follows project standards
- [ ] Tests are comprehensive and pass
- [ ] Documentation is updated
- [ ] No hardcoded values
- [ ] Proper error handling
- [ ] Logging is appropriate
- [ ] Performance considerations addressed