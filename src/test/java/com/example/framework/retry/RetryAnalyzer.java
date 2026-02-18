package com.example.framework.retry;

import com.example.framework.config.ConfigManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryAnalyzer.class);

    private int retryCount = 0;
    private final int maxRetryCount;

    public RetryAnalyzer() {
        String retryFromSystem = System.getProperty("retry.count");
        int configured;
        if (retryFromSystem != null && !retryFromSystem.isBlank()) {
            configured = parseRetryCount(retryFromSystem);
        } else {
            String retryFromConfig = ConfigManager.get("retry.count");
            configured = parseRetryCount(retryFromConfig);
        }
        this.maxRetryCount = configured;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            LOGGER.warn("Retrying test {} (attempt {}/{})",
                    result.getMethod().getMethodName(), retryCount, maxRetryCount);
            return true;
        }
        return false;
    }

    private int parseRetryCount(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return Math.max(parsed, 0);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid retry.count value '{}', using 0", value);
            return 0;
        }
    }
}

