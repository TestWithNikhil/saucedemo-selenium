import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ECommerceOrderValidationTest extends Base {

    private login loginPage;
    private Product productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void setUp() {
        beforeSuite();
        initialization();
        loginPage = new login();
        productPage = new Product();
        cartPage = new CartPage();
        checkoutPage = new CheckoutPage();
        loginPage.validateLogin();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot("ECommerceOrderValidationTest_" + result.getName() + "_FAIL");
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            captureScreenshot("ECommerceOrderValidationTest_" + result.getName() + "_PASS");
        }

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    public void validatePurchaseOfHighestAndLowestPriceProducts() throws IOException {
        List<Map<String, String>> allProducts = productPage.getAllProductsWithTitleAndPrice();
        Assert.assertFalse(allProducts.isEmpty(), "No products found on the inventory page.");

        double highestPrice = 0.0;
        double lowestPrice = Double.MAX_VALUE;
        String highestProductName = "";
        String lowestProductName = "";

        for (Map<String, String> product : allProducts) {
            double price = parseCurrencyValue(product.get("price"));
            if (price > highestPrice) {
                highestPrice = price;
                highestProductName = product.get("title");
            }
            if (price < lowestPrice) {
                lowestPrice = price;
                lowestProductName = product.get("title");
            }
        }

        Assert.assertFalse(highestProductName.isEmpty(), "Highest price product name was not identified.");
        Assert.assertFalse(lowestProductName.isEmpty(), "Lowest price product name was not identified.");
        logger.info("Highest priced product: " + highestProductName + " - $" + highestPrice);
        logger.info("Lowest priced product: " + lowestProductName + " - $" + lowestPrice);

        productPage.addHighestPriceProductToCart();
        captureScreenshot("ECommerceOrderValidationTest_AddHighestProductToCart");
        productPage.addLowestPriceProductToCart();
        captureScreenshot("ECommerceOrderValidationTest_AddLowestProductToCart");

        String cartValue = productPage.getCartBadgeText();
        Assert.assertEquals(cartValue, "2", "Only 2 products should be in cart");

        cartPage.openCart();
        Assert.assertEquals(cartPage.getItemCount(), 2, "Cart should contain exactly 2 items");

        double expectedCartTotal = highestPrice + lowestPrice;
        double actualCartTotal = cartPage.getItemTotal();
        Assert.assertEquals(actualCartTotal, expectedCartTotal, 0.01, "Cart total should equal sum of highest and lowest priced products");

        cartPage.clickCheckout();
        checkoutPage.enterCheckoutInformation();
        captureScreenshot("ECommerceOrderValidationTest_PaymentDetails");
        String checkoutUrl = checkoutPage.getOrderConfirmationUrl();
        Assert.assertTrue(checkoutUrl.contains("checkout-step-two.html"), "Checkout overview page was not reached.");

        checkoutPage.finishOrder();
        String confirmationMessage = checkoutPage.getConfirmationMessage();
        Assert.assertTrue(confirmationMessage.contains("Thank you for your order"), "Order completion confirmation was not displayed.");

        String pdfPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "receipt.pdf";
        PdfUtil.createReceiptPdf(pdfPath, highestProductName + ", " + lowestProductName, String.format("%.2f", expectedCartTotal));

        String pdfText = PdfUtil.readPdfText(pdfPath);
        Assert.assertTrue(pdfText.contains(highestProductName), "PDF is missing the highest priced product name.");
        Assert.assertTrue(pdfText.contains(lowestProductName), "PDF is missing the lowest priced product name.");
        Assert.assertTrue(pdfText.contains(String.format("%.2f", expectedCartTotal)), "PDF is missing the total amount.");
    }
}
