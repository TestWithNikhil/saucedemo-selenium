import java.time.Duration;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage extends Base {

    private static final Logger logger = Logger.getLogger(CartPage.class);

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".cart_item .inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage() {
        PageFactory.initElements(driver, this);
    }

    public void openCart() {
        logger.info("Opening cart page");
        getWait().until(ExpectedConditions.visibilityOf(cartLink));
        safeClick(cartLink);
        getWait().until(ExpectedConditions.urlContains("cart.html"));
    }

    public int getItemCount() {
        logger.info("Checking cart item count");
        return getWait().until(ExpectedConditions.visibilityOfAllElements(cartItems)).size();
    }

    public double getItemTotal() {
        List<WebElement> prices = getWait().until(ExpectedConditions.visibilityOfAllElements(cartItemPrices));
        double total = 0.0;
        for (WebElement priceElement : prices) {
            total += parseCurrencyValue(priceElement.getText());
        }
        logger.info("Cart item total derived from cart items: $" + total);
        return total;
    }

    public void clickCheckout() {
        logger.info("Clicking checkout from cart");
        safeClick(getWait().until(ExpectedConditions.elementToBeClickable(checkoutButton)));
        getWait().until(ExpectedConditions.urlContains("checkout-step-one.html"));
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }
}
