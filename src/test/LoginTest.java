import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends Base {
    
    login loginPage;

    @BeforeMethod
    public void setUp() {
        beforeSuite();
        initialization();
        loginPage = new login();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test(priority = 1)
    public void validateLogin() {
        loginPage.validateLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Login failed");
    }

    @Test(priority = 2)
    public void validateLoginWithInvalidCredentials() {
        loginPage.validateLoginWithInvalidCredentials();
        String errorMessage = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(errorMessage.contains("Username and password do not match any user in this service"), "Error message not displayed for invalid login");
    }
}
