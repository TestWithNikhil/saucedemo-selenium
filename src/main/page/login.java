import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class login extends Base {

    private static final Logger logger = Logger.getLogger(login.class);

    @FindBy(id = "user-name")
    private WebElement username;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    public login() {
        super();
        PageFactory.initElements(driver, this);
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void validateLogin() {
        String usernameValue = Base.getRequiredConfigValue("username");
        String passwordValue = Base.getRequiredConfigValue("password");

        logger.info("Attempting login with configured user credentials");
        getWait().until(ExpectedConditions.visibilityOf(username));
        getWait().until(ExpectedConditions.visibilityOf(password));
        username.clear();
        password.clear();
        username.sendKeys(usernameValue);
        password.sendKeys(passwordValue);
        loginButton.click();
        logger.info("Login button clicked successfully");
    }
}
