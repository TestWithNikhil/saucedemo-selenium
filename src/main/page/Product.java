import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Product extends Base {

    private static final Logger logger = Logger.getLogger(Product.class);

    @FindBy(css = ".inventory_item")
    private List<WebElement> productCards;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productTitles;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(css = "button[data-test^='add-to-cart']")
    private List<WebElement> addToCartButtons;

    @FindBy(css = "button[data-test^='remove-']")
    private List<WebElement> removeButtons;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    public Product() {
        PageFactory.initElements(driver, this);
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<Map<String, String>> getAllProductsWithTitleAndPrice() {
        logger.info("Fetching all products and their prices from inventory page");
        List<Map<String, String>> productList = new ArrayList<>();
        getWait().until(ExpectedConditions.visibilityOfAllElements(productCards));

        for (int i = 0; i < productCards.size(); i++) {
            WebElement card = productCards.get(i);
            String title = card.findElement(By.cssSelector(".inventory_item_name")).getText();
            String price = card.findElement(By.cssSelector(".inventory_item_price")).getText();

            Map<String, String> productMap = new HashMap<>();
            productMap.put("title", title);
            productMap.put("price", price);
            productList.add(productMap);
            logger.info("Product found: " + title + " - " + price);
        }

        return productList;
    }

    public void addHighestPriceProductToCart() {
        logger.info("Selecting highest price product");
        addProductToCart(getProductByPrice(true));
    }

    public void addLowestPriceProductToCart() {
        logger.info("Selecting lowest price product");
        addProductToCart(getProductByPrice(false));
    }

    private void addProductToCart(WebElement productCard) {
        WebElement addButton = productCard.findElement(By.cssSelector("button[data-test^='add-to-cart']"));
        int cartCountBefore = getCartCount();
        logger.info("Adding selected product to cart. Cart count before: " + cartCountBefore);

        getWait().until(ExpectedConditions.elementToBeClickable(addButton));
        safeClick(addButton);

        getWait().until(driver -> getCartCount() > cartCountBefore);
        logger.info("Product added to cart successfully. Cart count after: " + getCartCount());
    }

    private WebElement getProductByPrice(boolean highest) {
        logger.info("Finding product with " + (highest ? "highest" : "lowest") + " price");
        List<WebElement> productCards = getWait().until(ExpectedConditions.visibilityOfAllElements(this.productCards));

        Comparator<WebElement> productPriceComparator = Comparator.comparingDouble(card -> {
            String priceText = card.findElement(By.cssSelector(".inventory_item_price")).getText().replace("$", "");
            return Double.parseDouble(priceText);
        });

        WebElement selectedProduct = highest ? Collections.max(productCards, productPriceComparator) : Collections.min(productCards, productPriceComparator);
        logger.info("Selected product: " + selectedProduct.findElement(By.cssSelector(".inventory_item_name")).getText());
        return selectedProduct;
    }

    public String getCartBadgeText() {
        String badgeText = getWait().until(ExpectedConditions.visibilityOf(cartBadge)).getText();
        logger.info("Cart badge text: " + badgeText);
        return badgeText;
    }
}
