package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage {
    
    WebDriver driver;
    WebDriverWait wait;

    // --- Locators ---

    // Cart Page Elements
    @FindBy(id = "termsofservice")
    WebElement termsCheckbox;

    @FindBy(id = "checkout")
    WebElement checkoutBtn;

    // Step 1: Billing Address
    @FindBy(id = "billing-address-select")
    WebElement billingAddressDropdown; 

    @FindBy(id = "BillingNewAddress_FirstName")
    WebElement firstName;
    
    @FindBy(id = "BillingNewAddress_LastName")
    WebElement lastName;
    
    @FindBy(id = "BillingNewAddress_Email")
    WebElement email;
    
    @FindBy(id = "BillingNewAddress_CountryId")
    WebElement countryDropdown;
    
    @FindBy(id = "BillingNewAddress_City")
    WebElement city;
    
    @FindBy(id = "BillingNewAddress_Address1")
    WebElement address1;
    
    @FindBy(id = "BillingNewAddress_ZipPostalCode")
    WebElement zipCode;
    
    @FindBy(id = "BillingNewAddress_PhoneNumber")
    WebElement phoneNumber;

    // Buttons (Using specific IDs or consistent CSS)
    @FindBy(css = "#billing-buttons-container .new-address-next-step-button")
    WebElement billingContinueBtn;

    @FindBy(css = "#shipping-buttons-container .new-address-next-step-button")
    WebElement shippingAddressContinueBtn;

    @FindBy(css = "#shipping-method-buttons-container .shipping-method-next-step-button")
    WebElement shippingMethodContinueBtn;

    @FindBy(css = "#payment-method-buttons-container .payment-method-next-step-button")
    WebElement paymentMethodContinueBtn;

    @FindBy(css = "#payment-info-buttons-container .payment-info-next-step-button")
    WebElement paymentInfoContinueBtn;

    @FindBy(css = "#confirm-order-buttons-container .confirm-order-next-step-button")
    WebElement confirmOrderBtn;

    // Checkboxes/Options
    @FindBy(id = "shippingoption_0") // Ground
    WebElement groundShipping;
    
    @FindBy(id = "paymentmethod_0") // Cash On Delivery
    WebElement codPayment;

    // Validation
    @FindBy(css = ".field-validation-error")
    List<WebElement> fieldValidationErrors;

    // Success Page
    @FindBy(css = ".title strong")
    WebElement successTitle; 
    
    @FindBy(css = ".details li")
    WebElement orderNumber; 

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // --- Actions ---

    public void startCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox)).click();
        checkoutBtn.click();
    }

    public void fillBillingAddress(String fName, String lName, String country, String cityVal, String addr, String zip, String phone) {
        // Wait for billing container to be active
        wait.until(ExpectedConditions.visibilityOf(billingContinueBtn));

        try {
            if (billingAddressDropdown.isDisplayed()) {
                Select select = new Select(billingAddressDropdown);
                select.selectByVisibleText("New Address");
            }
        } catch (Exception e) {
            // Dropdown not present, ignore
        }

        wait.until(ExpectedConditions.visibilityOf(firstName)).clear();
        firstName.sendKeys(fName);
        lastName.clear();
        lastName.sendKeys(lName);
        
        Select countrySelect = new Select(countryDropdown);
        countrySelect.selectByVisibleText(country);
        
        city.clear();
        city.sendKeys(cityVal);
        address1.clear();
        address1.sendKeys(addr);
        zipCode.clear();
        zipCode.sendKeys(zip);
        phoneNumber.clear();
        phoneNumber.sendKeys(phone);
    }
    
    public void clickBillingContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(billingContinueBtn)).click();
    }

    public void clickShippingAddressContinue() {
        // Wait to see if Shipping Address section loads (it might be skipped)
        try {
            // Short wait to check if the button appears
            new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOf(shippingAddressContinueBtn));
            shippingAddressContinueBtn.click();
        } catch (Exception e) {
            System.out.println("Shipping Address step skipped (Same as billing).");
        }
    }
    
    public void selectShippingMethod() {
        // CRITICAL: Wait for the next section to be visible before interacting
        wait.until(ExpectedConditions.visibilityOf(groundShipping));
        groundShipping.click();
        wait.until(ExpectedConditions.elementToBeClickable(shippingMethodContinueBtn)).click();
    }
    
    public void selectPaymentMethod() {
        // CRITICAL: Wait for Payment Method section
        wait.until(ExpectedConditions.visibilityOf(codPayment));
        codPayment.click();
        wait.until(ExpectedConditions.elementToBeClickable(paymentMethodContinueBtn)).click();
    }
    
    public void confirmPaymentInfo() {
        // CRITICAL: Wait for Payment Info section
        wait.until(ExpectedConditions.elementToBeClickable(paymentInfoContinueBtn));
        paymentInfoContinueBtn.click();
    }
    
    public void confirmOrder() {
        // CRITICAL: Wait for Confirm Order section
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderBtn));
        confirmOrderBtn.click();
    }

    public String getSuccessTitle() {
        // Wait for URL to change or Success Title to appear
        wait.until(ExpectedConditions.urlContains("completed"));
        wait.until(ExpectedConditions.visibilityOf(successTitle));
        return successTitle.getText();
    }
    
    public String getOrderNumber() {
        return orderNumber.getText();
    }
    
    public void selectNewAddress() {
        wait.until(ExpectedConditions.visibilityOf(billingContinueBtn));
        try {
            Select select = new Select(billingAddressDropdown);
            select.selectByVisibleText("New Address");
        } catch (Exception e) {
            // Already on new address
        }
        firstName.clear(); 
    }
    
    public boolean areValidationErrorsDisplayed() {
        return fieldValidationErrors.size() > 0;
    }
}