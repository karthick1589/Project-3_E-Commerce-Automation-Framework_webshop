package testcases;

import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.ReuseMethods;
import pages.HomePage;
import utility.UtilityClass;

@Listeners(base.TestListener.class)
public class SignUpTest extends ReuseMethods {

    String sheetName = "UserData"; 

    @Test(priority = 1, description = "Verify user sign-up with unique credentials")
    public void testSignUpUnique() {
        HomePage home = new HomePage(getDriver());

        String uniqueEmail = "testuser_" + UUID.randomUUID().toString().substring(0, 5) + "@test.com";
        String password = "Pass" + UUID.randomUUID().toString().substring(0, 5);
        String fName = "Karthick";
        String lName = "Gopal";

        home.clickRegisterLink();
        home.enterRegistrationDetails(fName, lName, uniqueEmail, password);
        home.clickRegisterButton();
        
        String msg = home.getSuccessMessage();
        Assert.assertEquals(msg, "Your registration completed");
        
        UtilityClass.writeToExcel(sheetName, uniqueEmail, password);
    }

    @Test(priority = 2, dataProvider = "getUserData", description = "Verify sign-up with existing email")
    public void testSignUpExisting(String username, String password) {
        HomePage home = new HomePage(getDriver());
        
        home.clickRegisterLink();
        home.enterRegistrationDetails("Existing", "User", username, password);
        home.clickRegisterButton();
        
        String errorMsg = home.getMainErrorMessage();
        Assert.assertTrue(errorMsg.contains("The specified email already exists"), 
            "Expected 'email already exists' error but got: " + errorMsg);
    }

    @Test(priority = 3, description = "Verify sign-up with empty fields")
    public void testSignUpEmpty() {
        HomePage home = new HomePage(getDriver());
        
        home.clickRegisterLink();
        home.enterEmptyDetails();
        home.clickRegisterButton();
        
        Assert.assertTrue(home.isFieldValidationVisible(), "Field validation error should be visible");
    }

    @DataProvider(name = "getUserData")
    public Object[][] getUserData() {
        return UtilityClass.getTestData(sheetName);
    }
}