# SauceDemo Selenium Automation Project

This project automates the SauceDemo e-commerce workflow using Java, Maven, Selenium WebDriver, and TestNG. It validates the key user journey from login to product selection, cart validation, checkout, and order confirmation.

## Project Objective
The main goal is to automate the online shopping flow and verify that the selected products, pricing logic, checkout details, and generated receipt are correct.

## Included Automation
- Login to SauceDemo
- Read product list and prices from the inventory page
- Select the highest and lowest priced products
- Add both products to the cart
- Validate cart total
- Complete checkout with customer details
- Verify order confirmation
- Validate generated PDF receipt content
- Capture screenshots for pass/fail scenarios
- Generate HTML Extent Reports after execution

## Tech Stack
- Java
- Maven
- Selenium WebDriver
- TestNG
- Log4j
- PDFBox
- ExtentReports

## Project Structure
- `src/main/base/Base.java` - browser setup, shared utilities, screenshot capture
- `src/main/page/login.java` - login page object
- `src/main/page/Product.java` - inventory and product selection logic
- `src/main/page/CartPage.java` - cart validation actions
- `src/main/page/CheckoutPage.java` - checkout and order confirmation flow
- `src/main/utils/PdfUtil.java` - PDF creation and validation helper
- `src/test/LoginTest.java` - login test
- `src/test/ECommerceOrderValidationTest.java` - end-to-end purchase validation
- `src/test/ExtentReportListener.java` - report generation listener
- `src/main/resources/config.properties` - browser and credential configuration
- `testng.xml` - TestNG suite configuration

## Prerequisites
- JDK 8 or later
- Maven installed and available in PATH
- Chrome browser installed
- Internet access

## Run the Tests
From the project root, run:

```bash
mvn clean test
```

## Output Artifacts
- Test results: `target/surefire-reports`
- HTML report: `target/reports/ExtentReport.html`
- Screenshots: `target/screenshots`
- PDF receipt: `target/receipt.pdf`

## Notes
This project is structured using the Page Object Model (POM) pattern, which makes the tests cleaner, reusable, and easier to maintain.

It is suitable for interview/demo submission as a practical example of QA automation using Selenium and Java.
