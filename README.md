# Java Playwright Test Framework

Java-based Playwright automation framework with TestNG, Allure, and ExtentReports.

## Stack

- **Java 17**
- **Playwright for Java**
- **TestNG** — test runner
- **Allure** — reporting
- **ExtentReports** — reporting

## Project Structure

```
src/test/java/com/example/framework/
├── base/         # BaseTest
├── config/       # ConfigManager
├── driver/       # DriverManager, PlaywrightFactory
├── listeners/    # TestListener
├── pages/        # Page Objects
├── reporting/    # ExtentManager
├── retry/        # RetryAnalyzer
└── utils/        # Screenshot, Trace, Allure utils
```

## Prerequisites

- Java 17+
- Maven 3.8+

## Quick Start

1. **Install dependencies**

   ```bash
   mvn clean install
   ```

2. **Install Playwright browsers (first run)**

   ```bash
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
   ```

3. **Run tests**

   ```bash
   mvn test
   ```

## Configuration

Edit `src/test/resources/config/config.properties` or `config-local.properties` for:

- Base URL
- Browser (chromium, firefox, webkit)
- Headless mode
