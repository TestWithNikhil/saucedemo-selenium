import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    public void validateLogin() {
        logger.info("Attempting login with standard user credentials");
        username.sendKeys("standard_user");
        password.sendKeys("secret_sauce");
        loginButton.click();
        logger.info("Login button clicked successfully");
    }

    public void validateLoginWithInvalidCredentials() {
        logger.info("Attempting login with invalid credentials");
        username.sendKeys("invalid_user");
        password.sendKeys("invalid_password");
        loginButton.click();
        logger.info("Invalid login attempt submitted");
    }
}
