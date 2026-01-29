package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

	WebDriver driver;
	WebDriverWait wait;

	@FindBy(className = "cart-item-row")
	List<WebElement> cartRows;

	@FindBy(css = ".product-name")
	List<WebElement> productNames;

	@FindBy(name = "updatecart")
	WebElement updateCartButton;

	@FindBy(className = "order-summary-content")
	WebElement emptyCartMessage;

	public CartPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	public boolean isProductInCart(String expectedProductName) {
		for (WebElement name : productNames) {
			if (name.getText().trim().equals(expectedProductName)) {
				return true;
			}
		}
		return false;
	}

	public double getTotalPrice() {
		WebElement priceElement = null;

		try {
			priceElement = driver.findElement(By.cssSelector(".product-price.order-total strong"));
		} catch (Exception e1) {
			try {
				priceElement = driver.findElement(
						By.xpath("//span[contains(text(),'Total:')]/ancestor::tr//span[@class='product-price']"));
			} catch (Exception e2) {
				try {
					priceElement = driver.findElement(By.cssSelector(".cart-total-right .product-price"));
				} catch (Exception e3) {
					System.out.println("FATAL: Could not find Total Price using any strategy.");
					return 0.0;
				}
			}
		}

		if (priceElement != null) {
			String totalText = priceElement.getText().trim();
			System.out.println("Found Total Price Text: " + totalText);
			return Double.parseDouble(totalText.replaceAll("[^0-9.]", ""));
		}

		return 0.0;
	}

	public void removeProduct(String productName) {

		String xpath = "//a[@class='product-name' and text()='" + productName
				+ "']/../..//input[@name='removefromcart']";

		try {
			WebElement removeCheckbox = driver.findElement(By.xpath(xpath));
			if (!removeCheckbox.isSelected()) {
				removeCheckbox.click();
			}
			updateCartButton.click();
		} catch (Exception e) {
			System.out.println("Could not find remove checkbox for: " + productName);
		}
	}

	public String getEmptyCartMessage() {
		return emptyCartMessage.getText();
	}

	public int getUniqueItemCount() {
		return cartRows.size();
	}
}