import org.testng.Assert;
import org.testng.ITestResult;
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
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = captureScreenshot("LoginTest_" + result.getName() + "_FAIL");
            result.setAttribute("screenshotPath", screenshotPath);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            String screenshotPath = captureScreenshot("LoginTest_" + result.getName() + "_PASS");
            result.setAttribute("screenshotPath", screenshotPath);
        }

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
}
