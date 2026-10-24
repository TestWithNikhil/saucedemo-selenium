import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class login extends Base {

    public login() {
        super();
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "user-name")
    private WebElement username;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    public void validateLogin() {
        username.sendKeys("standard_user");
        password.sendKeys("secret_sauce");
        loginButton.click();
    }

    public void validateLoginWithInvalidCredentials() {
        username.sendKeys("invalid_user");
        password.sendKeys("invalid_password");
        loginButton.click();
    }
}
