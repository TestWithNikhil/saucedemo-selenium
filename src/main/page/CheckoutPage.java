import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage extends Base {

    private static final Logger logger = Logger.getLogger(CheckoutPage.class);

    @FindBy(id = "first-name")
    private WebElement firstName;

    @FindBy(id = "last-name")
    private WebElement lastName;

    @FindBy(id = "postal-code")
    private WebElement postalCode;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(css = ".complete-header")
    private WebElement completionMessage;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    public CheckoutPage() {
        PageFactory.initElements(driver, this);
    }

    public void enterCheckoutInformation() {
        logger.info("Entering checkout customer information");
        getWait().until(ExpectedConditions.visibilityOf(firstName)).sendKeys("Test");
        lastName.sendKeys("User");
        postalCode.sendKeys("560001");
        safeClick(getWait().until(ExpectedConditions.elementToBeClickable(continueButton)));
    }

    public double getTotalAmount() {
        String totalText = getWait().until(ExpectedConditions.visibilityOf(totalLabel)).getText();
        String value = totalText.replace("Total: $", "").trim();
        double amount = parseCurrencyValue(value);
        logger.info("Checkout total amount: $" + amount);
        return amount;
    }

    public void finishOrder() {
        logger.info("Completing the order");
        safeClick(getWait().until(ExpectedConditions.elementToBeClickable(finishButton)));
        getWait().until(ExpectedConditions.visibilityOf(completionMessage));
    }

    public String getConfirmationMessage() {
        return getWait().until(ExpectedConditions.visibilityOf(completionMessage)).getText();
    }

    public String getOrderConfirmationUrl() {
        return driver.getCurrentUrl();
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }
}
