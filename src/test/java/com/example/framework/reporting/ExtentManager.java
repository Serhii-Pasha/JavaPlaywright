package com.example.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class ExtentManager {

    private static final String REPORTS_DIR = "reports";
    private static ExtentReports extentReports;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            createInstance();
        }
        return extentReports;
    }

    private static void createInstance() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportFilePath = REPORTS_DIR + File.separator + "ExtentReport_" + timestamp + ".html";

        File reportsDir = new File(REPORTS_DIR);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
        sparkReporter.config().setDocumentTitle("Playwright Test Automation Report");
        sparkReporter.config().setReportName("Playwright TestNG Suite");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Framework", "Playwright Java + TestNG");
        extentReports.setSystemInfo("Author", "Automation Framework");
    }

    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}

