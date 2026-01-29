package testcases;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import base.ReuseMethods;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductPage;
import utility.UtilityClass;

@Listeners({base.TestListener.class})
public class CartTest extends ReuseMethods {

    String productName1 = "Computing and Internet"; 
    String productName2 = "Fiction"; 

    @BeforeMethod
    public void login() {

        String[] user = UtilityClass.getRandomUser("UserData");
        LoginPage loginPage = new LoginPage(getDriver());
        
        loginPage.clickLoginLink();
        loginPage.enterLoginDetails(user[0], user[1]);
        loginPage.clickLoginButton();
    }

    @Test(priority = 1, description = "Scenario: Add product to cart")
    public void testAddProductToCart() {
        ProductPage productPage = new ProductPage(getDriver());
        
        productPage.clickBooksCategory();
        
        productPage.clickProduct(productName1);
        
        productPage.clickAddToCart();
        
        String msg = productPage.getSuccessMessage();
        AssertJUnit.assertEquals(msg, "The product has been added to your shopping cart", "Success message mismatch!");
        
        productPage.closeNotification();
    }

    @Test(priority = 2, description = "Scenario: View cart with added products")
    public void testViewCart() {
        ProductPage productPage = new ProductPage(getDriver());
        productPage.clickBooksCategory();
        productPage.clickProduct(productName1);
        productPage.clickAddToCart();
        productPage.closeNotification();
        
        CartPage cartPage = new CartPage(getDriver());
        productPage.goToCart();
        
        boolean isPresent = cartPage.isProductInCart(productName1);
        AssertJUnit.assertTrue(isPresent);
    }

    @Test(priority = 3, description = "Scenario: Add multiple products to cart")
    public void testAddMultipleProducts() {
        ProductPage productPage = new ProductPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());
        
        productPage.clickBooksCategory();
        productPage.clickProduct(productName1);
        productPage.clickAddToCart();
        productPage.closeNotification();
        
        productPage.clickBooksCategory();
        productPage.clickProduct(productName2);
        productPage.clickAddToCart();
        productPage.closeNotification();
        
        productPage.goToCart();
        
        AssertJUnit.assertTrue(productName1, cartPage.isProductInCart(productName1));
        AssertJUnit.assertTrue(productName1, cartPage.isProductInCart(productName2));
        
        AssertJUnit.assertTrue(cartPage.getTotalPrice() > 0);
    }

    @Test(priority = 4, description = "Scenario: Remove product from cart")
    public void testRemoveProduct() {
        ProductPage productPage = new ProductPage(getDriver());
        productPage.clickBooksCategory();
        productPage.clickProduct(productName2);
        productPage.clickAddToCart();
        productPage.closeNotification();
        
        productPage.goToCart();
        
        CartPage cartPage = new CartPage(getDriver());
        
        int initialCount = cartPage.getUniqueItemCount();
        
        cartPage.removeProduct(productName2);
        
        boolean isPresent = cartPage.isProductInCart(productName2);
        AssertJUnit.assertFalse(isPresent);
        
        AssertJUnit.assertEquals(cartPage.getUniqueItemCount(), initialCount - 1);
    }
}