#!/bin/bash

# ECommerce Automation Framework Setup Script
# This script sets up the development environment

echo "🚀 Setting up ECommerce Automation Framework..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version check passed"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

echo "✅ Maven check passed"

# Create necessary directories
echo "📁 Creating project directories..."
mkdir -p reports/screenshots
mkdir -p logs
mkdir -p drivers
mkdir -p src/test/resources/testdata

echo "✅ Directories created"

# Install dependencies
echo "📦 Installing Maven dependencies..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Dependencies installed successfully"
else
    echo "❌ Failed to install dependencies"
    exit 1
fi

# Set executable permissions for scripts
chmod +x scripts/*.sh

echo "🎉 Setup completed successfully!"
echo ""
echo "Next steps:"
echo "1. Update src/main/resources/config.properties with your settings"
echo "2. Add your test data to src/test/resources/testdata/TestData.xlsx"
echo "3. Run tests: mvn test"
echo "4. View reports in the reports/ directory"
echo ""
echo "Happy Testing! 🧪"