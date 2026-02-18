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

public class TestListener implements ITestListener, ISuiteListener, IResultListener2 {

    @Override
    public void onStart(ISuite suite) {
        ExtentManager.getExtentReports();
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
        String screenshotPath = ScreenshotUtils.takeScreenshot(testName);

        String tracePath = null;
        BrowserContext context = DriverManager.getContext();
        if (context != null) {
            tracePath = TraceUtils.exportTrace(context, testName);
        }

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
       
        if (tracePath != null) {
            String name = java.nio.file.Paths.get(tracePath).getFileName().toString();
            ExtentTestManager.getTest().log(Status.INFO, "Playwright trace: " + name);
        }
        if (videoPathStr != null) {
            String name = java.nio.file.Paths.get(videoPathStr).getFileName().toString();
            ExtentTestManager.getTest().log(Status.INFO, "Playwright video: " + name);
        }

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
        BrowserContext context = DriverManager.getContext();
        if (context != null) {
            context.tracing().stop();
        }

        ExtentTestManager.getTest().log(Status.SKIP, "Test skipped");
        ExtentTestManager.endTest(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onStart(ITestContext context) {
        
    }

    @Override
    public void onFinish(ITestContext context) {
       
    }
}

