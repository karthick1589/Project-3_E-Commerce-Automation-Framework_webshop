package base;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {

	@Override
	public void onTestFailure(ITestResult result) {
		if (ReuseMethods.getTest() != null) {
			ReuseMethods.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable());

			String path = ReuseMethods.captureScreenshot(result.getName());

			if (path != null && !path.isEmpty()) {
				try {
					ReuseMethods.getTest().addScreenCaptureFromPath(path);
				} catch (Exception e) {
					ReuseMethods.getTest().info("Could not attach screenshot.");
				}
			} else {
				ReuseMethods.getTest().info("Screenshot failed (Browser likely crashed).");
			}
		}
	}

	@Override
	public void onTestStart(ITestResult result) {

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		if (ReuseMethods.getTest() != null) {
			ReuseMethods.getTest().log(Status.PASS, "Test Passed");

			String path = ReuseMethods.captureScreenshot(result.getName());

			if (path != null) {
				ReuseMethods.getTest().addScreenCaptureFromPath(path);
			} else {
				ReuseMethods.getTest().info("Screenshot could not be captured (Driver might be closed).");
			}
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
	}

	@Override
	public void onFinish(ITestContext context) {
	}
}
