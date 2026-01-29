package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    
    WebDriver driver;
    WebDriverWait wait;

    
    @FindBy(className = "ico-login")
    WebElement loginLink;
    
    @FindBy(className = "ico-logout")
    WebElement logoutLink;

    @FindBy(id = "Email")
    WebElement emailInput;

    @FindBy(id = "Password")
    WebElement passwordInput;

    @FindBy(css = ".login-button")
    WebElement loginButton;

    @FindBy(css = ".validation-summary-errors")
    WebElement mainErrorMessage;

    @FindBy(css = ".field-validation-error")
    WebElement fieldErrorMessage;
    
    @FindBy(css = "span[for='Email']")
    WebElement emailValidationError;

    @FindBy(className = "account")
    WebElement userAccountEmail;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void clickLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }
    
    public void clickLogoutLink() {
        if(isLogoutDisplayed()) {
            logoutLink.click();
        }
    }

    public void enterLoginDetails(String email, String password) {
        emailInput.clear();
        emailInput.sendKeys(email);
        
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }
    
    public void clearFields() {
        emailInput.clear();
        passwordInput.clear();
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public boolean isLogoutDisplayed() {
        try {
            return logoutLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getLoggedInEmail() {
        try {
            return userAccountEmail.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getMainErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(mainErrorMessage));
        return mainErrorMessage.getText();
    }
    
    public boolean isFieldValidationVisible() {
        try {
            return fieldErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getEmailValidationError() {
         wait.until(ExpectedConditions.visibilityOf(emailValidationError));
         return emailValidationError.getText();
    }

    public String getPasswordFieldType() {
        return passwordInput.getAttribute("type");
    }
}
