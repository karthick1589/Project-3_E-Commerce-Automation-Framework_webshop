package base;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import utilityclass.ConfigReader;


public class ReuseMethods {

	public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	public static ExtentSparkReporter spark;
	public static ExtentReports extent;

	@BeforeSuite
	public void setUpReport() {
		spark = new ExtentSparkReporter("target/ExtentReport.html");
		extent = new ExtentReports();
		extent.attachReporter(spark);
	}

	@BeforeMethod
    public void setup(Method method) {

        ExtentTest extentTest = extent.createTest(method.getName());
        test.set(extentTest);

        String browser = null;
        try {
            browser = ConfigReader.getProperty("browser");
        } catch (Exception e) {
        }
        
        // Default to chrome if config is missing
        if (browser == null) {
            browser = "chrome";
        }

        System.out.println("Launching Browser: " + browser);

        WebDriver localDriver;
        if (browser.equalsIgnoreCase("edge")) {
            localDriver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            localDriver = new FirefoxDriver();
        } else {
            localDriver = new ChromeDriver();
        }

        driver.set(localDriver);
        getDriver().manage().window().maximize();

        long wait = 20;
        try {
            String waitTime = ConfigReader.getProperty("implicitWait");
            if (waitTime != null)
                wait = Long.parseLong(waitTime);
        } catch (Exception e) {
        }

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

        try {
            String appUrl = ConfigReader.getProperty("testurl");
            if (appUrl != null)
                getDriver().get(appUrl);
            else
                getDriver().get("https://demowebshop.tricentis.com/");
        } catch (Exception e) {
            getDriver().get("https://demowebshop.tricentis.com/");
        }
    }

	@AfterMethod
	public void tearDown() {
		if (getDriver() != null) {

			try {
				getDriver().switchTo().alert().accept();
				System.out.println("Handled unexpected alert in tearDown");
			} catch (Exception e) {
			}

			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Error while quitting driver: " + e.getMessage());
			}
			driver.remove();
		}
	}

	@AfterSuite
	public void flushReport() {
		extent.flush();
	}

	public static WebDriver getDriver() {
		return driver.get();
	}

	public static ExtentTest getTest() {
		return test.get();
	}

	public static String captureScreenshot(String testName) {
		if (getDriver() == null) {
			return null;
		}

		try {
			File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
			String timestamp = String.valueOf(System.currentTimeMillis());
			String path = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + timestamp + ".png";

			File dest = new File(path);
			if (!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();
			}

			java.nio.file.Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			return path;
		} catch (Exception e) {
			System.out.println("Screenshot Failed: " + e.getMessage());
			return null;
		}
	}

}