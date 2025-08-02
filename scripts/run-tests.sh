#!/bin/bash

# Test execution script with various options

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}"
}

# Default values
BROWSER="chrome"
SUITE="testng.xml"
PARALLEL="false"
THREAD_COUNT="1"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -b|--browser)
            BROWSER="$2"
            shift 2
            ;;
        -s|--suite)
            SUITE="$2"
            shift 2
            ;;
        -p|--parallel)
            PARALLEL="true"
            THREAD_COUNT="$2"
            shift 2
            ;;
        -t|--test)
            TEST_CLASS="$2"
            shift 2
            ;;
        -h|--help)
            echo "Usage: $0 [OPTIONS]"
            echo "Options:"
            echo "  -b, --browser BROWSER     Browser to use (chrome, firefox, edge)"
            echo "  -s, --suite SUITE         TestNG suite file to run"
            echo "  -p, --parallel COUNT      Run tests in parallel with specified thread count"
            echo "  -t, --test CLASS          Run specific test class"
            echo "  -h, --help               Show this help message"
            echo ""
            echo "Examples:"
            echo "  $0 -b firefox -s testng.xml"
            echo "  $0 -t LoginTests -b chrome"
            echo "  $0 -p 3 -b chrome"
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            exit 1
            ;;
    esac
done

print_header "ECommerce Automation Test Execution"

# Validate browser
case $BROWSER in
    chrome|firefox|edge)
        print_status "Browser: $BROWSER"
        ;;
    *)
        print_error "Invalid browser: $BROWSER. Supported browsers: chrome, firefox, edge"
        exit 1
        ;;
esac

# Clean previous reports
print_status "Cleaning previous test reports..."
rm -rf reports/screenshots/*
rm -rf test-output/*
rm -rf logs/*

# Create timestamp for this run
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
print_status "Test execution started at: $(date)"

# Build Maven command
MVN_CMD="mvn clean test"

if [ ! -z "$TEST_CLASS" ]; then
    MVN_CMD="$MVN_CMD -Dtest=$TEST_CLASS"
    print_status "Running specific test class: $TEST_CLASS"
elif [ ! -z "$SUITE" ]; then
    MVN_CMD="$MVN_CMD -DsuiteXmlFile=$SUITE"
    print_status "Running test suite: $SUITE"
fi

MVN_CMD="$MVN_CMD -Dbrowser=$BROWSER"

if [ "$PARALLEL" = "true" ]; then
    MVN_CMD="$MVN_CMD -DthreadCount=$THREAD_COUNT"
    print_status "Parallel execution enabled with $THREAD_COUNT threads"
fi

# Execute tests
print_header "Executing Tests"
print_status "Command: $MVN_CMD"

eval $MVN_CMD

# Check test results
if [ $? -eq 0 ]; then
    print_status "✅ Tests completed successfully!"
else
    print_error "❌ Some tests failed. Check the reports for details."
fi

# Display report information
print_header "Test Reports"
REPORT_DIR="reports"
if [ -d "$REPORT_DIR" ]; then
    LATEST_REPORT=$(find $REPORT_DIR -name "ExtentReport_*.html" -type f -printf '%T@ %p\n' | sort -n | tail -1 | cut -d' ' -f2-)
    if [ ! -z "$LATEST_REPORT" ]; then
        print_status "📊 Latest HTML Report: $LATEST_REPORT"
        print_status "🖼️  Screenshots: $REPORT_DIR/screenshots/"
    fi
fi

# Display TestNG report
if [ -d "test-output" ]; then
    print_status "📋 TestNG Report: test-output/index.html"
fi

print_status "📝 Logs: logs/"
print_status "Test execution completed at: $(date)"

print_header "Summary"
echo "Browser: $BROWSER"
echo "Execution Time: $(date)"
if [ ! -z "$TEST_CLASS" ]; then
    echo "Test Class: $TEST_CLASS"
elif [ ! -z "$SUITE" ]; then
    echo "Test Suite: $SUITE"
fi
if [ "$PARALLEL" = "true" ]; then
    echo "Parallel Execution: $THREAD_COUNT threads"
fi