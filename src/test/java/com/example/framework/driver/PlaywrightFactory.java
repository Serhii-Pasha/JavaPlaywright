package com.example.framework.driver;

import com.example.framework.config.ConfigManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for creating Playwright, Browser, Context and Page instances.
 * Supports browser selection and headless/headed mode.
 */
public final class PlaywrightFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaywrightFactory.class);

    private PlaywrightFactory() {
    }

    public static void init() {
        Playwright playwright = Playwright.create();
        DriverManager.setPlaywright(playwright);

        String browserName = System.getProperty("browser", ConfigManager.getBrowser()).toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless",
                String.valueOf(ConfigManager.isHeadless())));

        BrowserType browserType;
        switch (browserName) {
            case "firefox":
                browserType = playwright.firefox();
                break;
            case "webkit":
                browserType = playwright.webkit();
                break;
            case "chromium":
            default:
                browserType = playwright.chromium();
                break;
        }

        LOGGER.info("Launching browser: {}, headless: {}", browserName, headless);

        LaunchOptions options = new LaunchOptions().setHeadless(headless);
        Browser browser = browserType.launch(options);
        DriverManager.setBrowser(browser);

        // Enable video recording for each context; videos will be kept/used only for failed tests.
        NewContextOptions contextOptions = new NewContextOptions()
                .setRecordVideoDir(java.nio.file.Paths.get("videos"));

        BrowserContext context = browser.newContext(contextOptions);
        context.setDefaultTimeout(ConfigManager.getTimeoutMillis());
        DriverManager.setContext(context);

        Page page = context.newPage();
        DriverManager.setPage(page);
    }
}

