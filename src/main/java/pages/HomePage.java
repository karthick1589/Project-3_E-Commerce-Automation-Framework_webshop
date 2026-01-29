package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    
    WebDriver driver;
    WebDriverWait wait;

    // --- Locators ---
    
    // Header Links
    @FindBy(className = "ico-register")
    WebElement registerLink;
    
    @FindBy(className = "ico-login")
    WebElement loginLink;

    // Register Elements
    @FindBy(id = "gender-male")
    WebElement genderMale;
    @FindBy(id = "FirstName")
    WebElement firstNameInput;
    @FindBy(id = "LastName")
    WebElement lastNameInput;
    @FindBy(id = "Email")
    WebElement emailInput;
    @FindBy(id = "Password")
    WebElement passwordInput;
    @FindBy(id = "ConfirmPassword")
    WebElement confirmPasswordInput;
    @FindBy(id = "register-button")
    WebElement registerButton;
    @FindBy(className = "result")
    WebElement successMessage;
    @FindBy(css = ".validation-summary-errors")
    WebElement mainErrorMessage;
    @FindBy(css = ".field-validation-error")
    WebElement fieldErrorMessage;

    // Featured Products (Validation for Home Page)
    @FindBy(className = "product-grid")
    WebElement featuredProductsGrid;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // --- Actions ---

    public void clickRegisterLink() {
        registerLink.click();
    }
    
    public void clickLoginLink() {
        loginLink.click();
    }

    public void enterRegistrationDetails(String fName, String lName, String email, String password) {
        genderMale.click();
        firstNameInput.clear(); firstNameInput.sendKeys(fName);
        lastNameInput.clear(); lastNameInput.sendKeys(lName);
        emailInput.clear(); emailInput.sendKeys(email);
        passwordInput.clear(); passwordInput.sendKeys(password);
        confirmPasswordInput.clear(); confirmPasswordInput.sendKeys(password);
    }

    public void enterEmptyDetails() {
        firstNameInput.clear();
        lastNameInput.clear();
        emailInput.clear();
        passwordInput.clear();
        confirmPasswordInput.clear();
    }

    public void clickRegisterButton() {
        registerButton.click();
    }

    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOf(successMessage));
        return successMessage.getText();
    }

    public String getMainErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(mainErrorMessage));
        return mainErrorMessage.getText();
    }
    
    public boolean isFieldValidationVisible() {
        try { return fieldErrorMessage.isDisplayed(); } catch (Exception e) { return false; }
    }

    // --- NAVIGATION METHODS ---
    
    public void clickHomeLogo() {
        // FIX: Using explicit By locator to prevent "Locator must be set" error with PageFactory
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".header-logo a"))).click();
    }
    
    public void clickCategory(String categoryName) {
        // Dynamic XPath to find any category by text in the Left Menu
        WebElement category = driver.findElement(By.xpath("//div[@class='block block-category-navigation']//a[contains(text(),'" + categoryName + "')]"));
        wait.until(ExpectedConditions.elementToBeClickable(category)).click();
    }
    
    public boolean isFeaturedProductsDisplayed() {
        try { return featuredProductsGrid.isDisplayed(); } catch (Exception e) { return false; }
    }
}