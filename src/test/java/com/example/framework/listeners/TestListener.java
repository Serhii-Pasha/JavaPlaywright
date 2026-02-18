package com.example.framework.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.example.framework.driver.DriverManager;
import com.example.framework.reporting.ExtentManager;
import com.example.framework.reporting.ExtentTestManager;
import com.example.framework.utils.AllureUtils;
import com.example.framework.utils.FileUtils;
import com.example.framework.utils.ScreenshotUtils;
import com.example.framework.utils.TraceUtils;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import org.testng.*;
import org.testng.internal.IResultListener2;

import java.nio.file.Path;

/**
 * TestNG listener to integrate ExtentReports, tracing, video and screenshots on failure.
 */
public class TestListener implements ITestListener, ISuiteListener, IResultListener2 {

    @Override
    public void onStart(ISuite suite) {
        // Ensure ExtentReports is initialized
        ExtentManager.getExtentReports();
        // Clean artifacts folders at the start of suite
        FileUtils.cleanDirectory("screenshots");
        FileUtils.cleanDirectory("videos");
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentTestManager.startTest(testName, description != null ? description : "");
        ExtentTestManager.getTest().log(Status.INFO, "Test started: " + testName);

        // Start Playwright tracing for this test
        BrowserContext context = DriverManager.getContext();
        if (context != null) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Stop tracing without exporting for successful tests
        BrowserContext context = DriverManager.getContext();
        if (context != null) {
            context.tracing().stop();
        }

        ExtentTestManager.getTest().log(Status.PASS, "Test passed");
        ExtentTestManager.endTest(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        // Take screenshot
        String screenshotPath = ScreenshotUtils.takeScreenshot(testName);

        // Export trace only for failed tests
        String tracePath = null;
        BrowserContext context = DriverManager.getContext();
        if (context != null) {
            tracePath = TraceUtils.exportTrace(context, testName);
        }

        // Try to resolve video path if available
        String videoPathStr = null;
        Page page = DriverManager.getPage();
        if (page != null && page.video() != null) {
            Path videoPath = page.video().path();
            if (videoPath != null) {
                videoPathStr = videoPath.toAbsolutePath().toString();
            }
        }

        if (screenshotPath != null) {
            ExtentTestManager.getTest().fail(result.getThrowable(),
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            ExtentTestManager.getTest().log(Status.FAIL, "Test failed with screenshot attached");
        } else {
            ExtentTestManager.getTest().fail(result.getThrowable());
        }
        // Attach to Extent as concise text logs (only file names)
        if (tracePath != null) {
            String name = java.nio.file.Paths.get(tracePath).getFileName().toString();
            ExtentTestManager.getTest().log(Status.INFO, "Playwright trace: " + name);
        }
        if (videoPathStr != null) {
            String name = java.nio.file.Paths.get(videoPathStr).getFileName().toString();
            ExtentTestManager.getTest().log(Status.INFO, "Playwright video: " + name);
        }

        // Attach screenshot, trace and video to Allure (auto content type)
        if (screenshotPath != null) {
            AllureUtils.attachFile("Screenshot on failure", screenshotPath);
        }
        if (tracePath != null) {
            AllureUtils.attachFile("Playwright trace", tracePath);
        }
        if (videoPathStr != null) {
            AllureUtils.attachFile("Playwright video", videoPathStr);
        }

        ExtentTestManager.endTest(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Stop tracing if test was skipped
        BrowserContext context = DriverManager.getContext();
        if (context != null) {
            context.tracing().stop();
        }

        ExtentTestManager.getTest().log(Status.SKIP, "Test skipped");
        ExtentTestManager.endTest(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not used
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onStart(ITestContext context) {
        // no-op
    }

    @Override
    public void onFinish(ITestContext context) {
        // no-op
    }
}

