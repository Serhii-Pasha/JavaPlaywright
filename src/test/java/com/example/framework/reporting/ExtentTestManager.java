package com.example.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public final class ExtentTestManager {

    private static final ConcurrentMap<Long, ExtentTest> TEST_MAP = new ConcurrentHashMap<>();
    private static final ExtentReports EXTENT = ExtentManager.getExtentReports();

    private ExtentTestManager() {
    }

    public static ExtentTest startTest(String testName, String description) {
        ExtentTest test = EXTENT.createTest(testName, description);
        TEST_MAP.put(Thread.currentThread().getId(), test);
        return test;
    }

    public static ExtentTest getTest() {
        return TEST_MAP.get(Thread.currentThread().getId());
    }

    public static void endTest(ITestResult result) {
        TEST_MAP.remove(Thread.currentThread().getId());
    }
}

