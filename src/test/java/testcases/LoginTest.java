package testcases;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.ReuseMethods;
import pages.LoginPage;
import utility.UtilityClass;

@Listeners(base.TestListener.class)
public class LoginTest extends ReuseMethods {

    // Sheet Names
    String validSheet = "UserData";
    String invalidPwdSheet = "InvalidLoginData";
    String invalidEmailSheet = "InvalidEmailData";
    String emptySheet = "EmptyLoginData";

    // 1. Valid Login
    @Test(priority = 1, dataProvider = "LoginData", description = "Verify login with valid credentials")
    public void testLoginValid(String email, String password) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.clickLogoutLink(); 
        loginPage.clickLoginLink();
        loginPage.enterLoginDetails(email, password);
        loginPage.clickLoginButton();
        
        Assert.assertTrue(loginPage.isLogoutDisplayed(), "Login failed: Logout link not visible.");
        Assert.assertEquals(loginPage.getLoggedInEmail(), email, "Logged in user email does not match.");
        loginPage.clickLogoutLink();
    }

    // 2. Invalid Password (FIXED: Added parameters)
    @Test(priority = 2, dataProvider = "InvalidPwdData", description = "Verify login with incorrect password")
    public void testLoginInvalidPassword(String email, String password) {
        LoginPage loginPage = new LoginPage(getDriver());
        
        loginPage.clickLoginLink();
        loginPage.enterLoginDetails(email, password);
        loginPage.clickLoginButton();
        
        String error = loginPage.getMainErrorMessage();
        Assert.assertTrue(error.contains("Login was unsuccessful"), "Error message mismatch: " + error);
        Assert.assertTrue(error.contains("The credentials provided are incorrect"), "Specific error text missing.");
    }

    // 3. Empty Fields (FIXED: Added parameters)
    @Test(priority = 3, dataProvider = "EmptyData", description = "Verify login with empty fields")
    public void testLoginEmptyFields(String email, String password) {
        LoginPage loginPage = new LoginPage(getDriver());
        
        loginPage.clickLoginLink();
        loginPage.enterLoginDetails(email, password); 
        loginPage.clickLoginButton();
        
        boolean errorExists = loginPage.getMainErrorMessage().contains("unsuccessful");
        Assert.assertTrue(errorExists, "Validation message not shown for empty fields.");
    }

    // 4. Invalid Email Format (FIXED: Added parameters)
    @Test(priority = 4, dataProvider = "InvalidEmailData", description = "Verify login with invalid email format")
    public void testLoginInvalidEmailFormat(String email, String password) {
        LoginPage loginPage = new LoginPage(getDriver());
        
        loginPage.clickLoginLink();
        loginPage.enterLoginDetails(email, password);
        loginPage.clickLoginButton();
        
        String error = loginPage.getEmailValidationError();
        Assert.assertTrue(error.contains("Wrong email"), "Expected 'Wrong email' validation but got: " + error);
    }

    // 5. Password Masking
    @Test(priority = 5, description = "Verify password field masks input")
    public void testPasswordMasking() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.clickLoginLink();
        
        String typeAttribute = loginPage.getPasswordFieldType();
        Assert.assertEquals(typeAttribute, "password", "Password field is not masked (type is not 'password').");
    }

    // --- Data Providers ---

    @DataProvider(name = "LoginData")
    public Object[][] getLoginData() {
        String[] randomUser = UtilityClass.getRandomUser(validSheet);
        return new Object[][] { { randomUser[0], randomUser[1] } };
    }

    @DataProvider(name = "InvalidPwdData")
    public Object[][] getInvalidPwdData() {
        return UtilityClass.getTestData(invalidPwdSheet);
    }

    @DataProvider(name = "EmptyData")
    public Object[][] getEmptyData() {
        return UtilityClass.getTestData(emptySheet);
    }

    @DataProvider(name = "InvalidEmailData")
    public Object[][] getInvalidEmailData() {
        return UtilityClass.getTestData(invalidEmailSheet);
    }
}