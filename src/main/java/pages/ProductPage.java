package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {
    
    WebDriver driver;
    WebDriverWait wait;

    // --- Locators ---
    
    @FindBy(css = ".page-title h1")
    WebElement pageTitle;
    
    // Grid for Products
    @FindBy(className = "product-grid")
    WebElement productGrid;

    // Grid for Sub-Categories (e.g., Desktops, Notebooks) - NEW LOCATOR
    @FindBy(className = "sub-category-grid")
    WebElement subCategoryGrid;

    // Product Details
    @FindBy(css = "h1[itemprop='name']")
    WebElement productDetailName;

    @FindBy(css = ".product-price")
    WebElement productDetailPrice;
    
    @FindBy(id = "bar-notification")
    WebElement notificationBar;
    
    @FindBy(css = ".content")
    WebElement notificationMessage;
    
    @FindBy(css = ".close")
    WebElement closeNotification;
    
    @FindBy(className = "ico-cart")
    WebElement shoppingCartLink;
    
    @FindBy(partialLinkText = "Books") 
    WebElement booksCategory;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // --- Actions ---

    public void clickBooksCategory() {
        booksCategory.click();
    }

    public void clickProduct(String productName) {
        driver.findElement(By.linkText(productName)).click();
    }
    
    public void clickProductFromList(String partialName) {
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(partialName))).click();
    }

    public void clickAddToCart() {
        WebElement btn = driver.findElement(By.cssSelector("input[id^='add-to-cart-button-']"));
        wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOf(pageTitle)).getText();
    }
    
    public String getProductDetailName() {
        return wait.until(ExpectedConditions.visibilityOf(productDetailName)).getText();
    }

    // FIXED: Checks for EITHER products OR sub-categories
    public boolean isProductListDisplayed() {
        try {
            return productGrid.isDisplayed();
        } catch (Exception e) {
            try {
                return subCategoryGrid.isDisplayed();
            } catch (Exception e2) {
                return false;
            }
        }
    }

    public boolean isProductDetailsVisible() {
        try {
            return productDetailName.isDisplayed() && productDetailPrice.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOf(notificationBar));
        return notificationMessage.getText();
    }
    
    public void closeNotification() {
        try {
            if (notificationBar.isDisplayed()) {
                closeNotification.click();
                wait.until(ExpectedConditions.invisibilityOf(notificationBar));
            }
        } catch (Exception e) {}
    }

    public void goToCart() {
        closeNotification(); 
        shoppingCartLink.click();
    }
}