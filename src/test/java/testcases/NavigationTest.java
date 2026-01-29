package testcases;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.ReuseMethods;
import pages.HomePage;
import pages.ProductPage;

@Listeners(base.TestListener.class)
public class NavigationTest extends ReuseMethods {

    @Test(priority = 1, description = "Browse product categories")
    public void testBrowseCategories() {
        HomePage home = new HomePage(getDriver());
        ProductPage productPage = new ProductPage(getDriver());

        home.clickCategory("Computers");

        String title = productPage.getPageTitle();
        Assert.assertEquals(title, "Computers", "Category Page Title mismatch");
        
        // This will now pass because we added the subCategoryGrid check
        Assert.assertTrue(productPage.isProductListDisplayed(), "Product/Sub-category list is not displayed.");
    }

    @Test(priority = 2, description = "View product details")
    public void testViewProductDetails() {
        HomePage home = new HomePage(getDriver());
        ProductPage productPage = new ProductPage(getDriver());

        home.clickCategory("Books");

        String targetProduct = "Fiction";
        productPage.clickProductFromList(targetProduct);

        Assert.assertEquals(productPage.getProductDetailName(), targetProduct, "Product Name mismatch on details page");
        Assert.assertTrue(productPage.isProductDetailsVisible(), "Product details (Image/Price/Desc) are missing.");
    }

    @Test(priority = 3, description = "Verify home navigation")
    public void testHomeNavigation() {
        HomePage home = new HomePage(getDriver());
        ProductPage productPage = new ProductPage(getDriver());

        // FIXED: Use the correct full category name
        String category = "Apparel & Shoes";
        home.clickCategory(category);
        
        // FIXED: Assert the correct full title
        Assert.assertEquals(productPage.getPageTitle(), category);

        home.clickHomeLogo();

        Assert.assertTrue(home.isFeaturedProductsDisplayed(), "Failed to return to Homepage; Featured Products not visible.");
    }
}