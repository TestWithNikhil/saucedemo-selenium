import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Base{

    public static WebDriver driver;
    public static Properties prop;
    public static Logger logger = Logger.getLogger(Base.class);

    public void beforeSuite() {
        try {
            logger.info("Loading configuration from config.properties");
            prop = new Properties();
            String configPath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
            FileInputStream fis = new FileInputStream(configPath);
            prop.load(fis);
            fis.close();
            logger.info("Configuration loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load config.properties", e);
            e.printStackTrace();
        }
    }

    public static void initialization() {
        String browserName = prop.getProperty("browser");
        logger.info("Initializing browser: " + browserName);

        if (browserName.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("profile.password_manager_leak_detection", false);
            options.setExperimentalOption("prefs", prefs);

            options.addArguments("--incognito");
            options.addArguments("--disable-features=PasswordLeakDetection,PasswordCheck");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-infobars");
            options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });

            driver = new ChromeDriver(options);
            logger.info("ChromeDriver created successfully");
        } else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
            logger.info("FirefoxDriver created successfully");
        } else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
            logger.info("EdgeDriver created successfully");
        } else {
            logger.error("Unsupported browser requested: " + browserName);
            throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        driver.manage().window().maximize();
        driver.get(prop.getProperty("url"));
        logger.info("Navigated to URL: " + prop.getProperty("url"));
    }

    public static int getCartCount() {
        if (driver == null) {
            logger.warn("Cart count requested while driver is null");
            return 0;
        }
        try {
            WebElement badge = driver.findElement(By.cssSelector(".shopping_cart_badge"));
            String text = badge.getText().trim();
            int count = text.isEmpty() ? 0 : Integer.parseInt(text);
            logger.info("Current cart count: " + count);
            return count;
        } catch (Exception e) {
            logger.debug("No cart badge present yet; current count is 0");
            return 0;
        }
    }

    public static void safeClick(WebElement element) {
        try {
            logger.info("Clicking element: " + element);
            element.click();
        } catch (Exception e) {
            try {
                logger.warn("Standard click failed; retrying using JavaScript click");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (Exception ignored) {
                logger.error("Click failed for element: " + element, ignored);
                throw e;
            }
        }
    }

    public static double parseCurrencyValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        String normalized = value.replace("$", "").replace(",", "").trim();
        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            logger.warn("Unable to parse currency value: '" + value + "'");
            return 0.0;
        }
    }
}