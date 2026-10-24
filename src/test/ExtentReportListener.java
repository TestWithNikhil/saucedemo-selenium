import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private static final String REPORT_PATH = System.getProperty("user.dir") + File.separator + "target"
            + File.separator + "reports" + File.separator + "ExtentReport.html";

    public static ExtentReports getExtent() {
        if (extent == null) {
            File reportDir = new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "reports");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
            sparkReporter.config().setDocumentTitle("SauceDemo Automation Report");
            sparkReporter.config().setReportName("SauceDemo Regression Suite");
            sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
        return extent;
    }

    @Override
    public void onStart(ITestContext context) {
        getExtent();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = getExtent().createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String screenshotPath = getScreenshotPath(result);
        if (screenshotPath != null && Files.exists(Paths.get(screenshotPath))) {
            try {
                test.get().pass("Test passed", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.get().pass("Test passed");
            }
        } else {
            test.get().pass("Test passed");
        }
        getExtent().flush();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String screenshotPath = getScreenshotPath(result);
        if (screenshotPath != null && Files.exists(Paths.get(screenshotPath))) {
            try {
                test.get().fail("Test failed", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.get().fail(result.getThrowable());
            }
        } else {
            test.get().fail(result.getThrowable());
        }
        getExtent().flush();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test skipped");
        getExtent().flush();
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    private String getScreenshotPath(ITestResult result) {
        Object path = result.getAttribute("screenshotPath");
        return path == null ? null : path.toString();
    }
}
