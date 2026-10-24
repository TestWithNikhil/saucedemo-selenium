# SauceDemo Selenium Automation

A Java + Maven + Selenium + TestNG project for automating the SauceDemo e-commerce flow.

## Tools used
- Java
- Maven
- Selenium WebDriver
- TestNG
- Log4j
- PDFBox

## Prerequisites
- JDK 8 or later
- Maven installed and available on PATH
- Google Chrome installed
- Internet access

## Project structure
- `src/main/base/Base.java` - driver setup and shared utilities
- `src/main/page/login.java` - login page object
- `src/main/page/Product.java` - product inventory actions
- `src/main/page/CartPage.java` - cart page actions
- `src/main/page/CheckoutPage.java` - checkout actions
- `src/main/utils/PdfUtil.java` - PDF validation helper
- `src/test/ECommerceOrderValidationTest.java` - end-to-end purchase flow
- `testng.xml` - test suite configuration

## Run tests
From the project root:

```bash
mvn clean test
```
