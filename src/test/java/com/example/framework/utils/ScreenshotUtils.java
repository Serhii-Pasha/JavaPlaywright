package com.example.framework.utils;

import com.example.framework.driver.DriverManager;
import com.microsoft.playwright.Page;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class ScreenshotUtils {

    private static final String SCREENSHOTS_DIR = "screenshots";

    private ScreenshotUtils() {
    }

    public static String takeScreenshot(String testName) {
        Page page = DriverManager.getPage();
        if (page == null) {
            return null;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testName + "_" + timestamp + ".png";
        Path dirPath = Paths.get(SCREENSHOTS_DIR);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create screenshots directory", e);
            }
        }

        Path filePath = dirPath.resolve(fileName);
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(filePath)
                .setFullPage(true));

        return filePath.toAbsolutePath().toString();
    }
}

