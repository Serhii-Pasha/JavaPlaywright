package com.example.framework.utils;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class TraceUtils {

    private static final String TRACES_DIR = "traces";

    private TraceUtils() {
    }

    public static String exportTrace(BrowserContext context, String testName) {
        if (context == null) {
            return null;
        }

        Path dirPath = Paths.get(TRACES_DIR);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create traces directory", e);
            }
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path tracePath = dirPath.resolve("trace_" + testName + "_" + timestamp + ".zip");

        context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
        return tracePath.toAbsolutePath().toString();
    }
}

