package testcases;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.ReuseMethods;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductPage;
import utility.UtilityClass;

@Listeners(base.TestListener.class)
public class CheckoutTest extends ReuseMethods {

    // Helper method to setup the state (Login + Add item to cart)
    public void setupPreConditions() {
        String[] user = UtilityClass.getRandomUser("UserData");
        LoginPage loginPage = new LoginPage(getDriver());
        ProductPage productPage = new ProductPage(getDriver());
        
        // 1. Login
        loginPage.clickLoginLink();
        loginPage.enterLoginDetails(user[0], user[1]);
        loginPage.clickLoginButton();
        
        // 2. Add Item to Cart
        productPage.clickBooksCategory();
        productPage.clickProduct("Computing and Internet");
        
        // Click add and WAIT for the success message to ensure it's actually added
        productPage.clickAddToCart();
        String msg = productPage.getSuccessMessage(); 
        Assert.assertEquals(msg, "The product has been added to your shopping cart", "Product add failed!");
        
        // 3. Go to Cart (Handle notification bar blocking the link)
        productPage.closeNotification(); 
        productPage.goToCart();
    }

    @Test(priority = 1, description = "Place order with valid details")
    public void testPlaceOrderValid() {
        setupPreConditions(); // Ensure user is logged in and has item
        
        CheckoutPage checkout = new CheckoutPage(getDriver());
        
        // 1. Start Checkout (This is where it was failing - it will pass now)
        checkout.startCheckout();
        
        // 2. Fill Billing Address
        checkout.fillBillingAddress("John", "Doe", "United States", "New York", "123 Test St", "10001", "1234567890");
        checkout.clickBillingContinue();
        
        // 3. Handle intermediate steps
        checkout.clickShippingAddressContinue();
        checkout.selectShippingMethod();
        checkout.selectPaymentMethod();
        checkout.confirmPaymentInfo();
        
        // 4. Confirm Order
        checkout.confirmOrder();
        
        // 5. Verify Success
        String title = checkout.getSuccessTitle();
        Assert.assertEquals(title, "Your order has been successfully processed!", "Order success message mismatch");
        
        String orderNum = checkout.getOrderNumber();
        System.out.println("Order Placed: " + orderNum);
        Assert.assertTrue(orderNum.contains("Order number:"), "Order number missing.");
    }

    @Test(priority = 2, description = "Place order with empty form (Negative Test)")
    public void testPlaceOrderEmpty() {
        setupPreConditions(); 
        
        CheckoutPage checkout = new CheckoutPage(getDriver());
        checkout.startCheckout();
        
        // 1. Ensure fields are empty (Select 'New Address' and clear Name)
        checkout.selectNewAddress();
        
        // 2. Try to click continue without filling details
        checkout.clickBillingContinue();
        
        // 3. Expect Validation Errors
        boolean hasErrors = checkout.areValidationErrorsDisplayed();
        Assert.assertTrue(hasErrors, "Validation errors should be displayed when submitting empty billing form.");
    }
}
