package com.example.framework.driver;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Thread-safe holder for Playwright primitives.
 * Enables parallel execution via ThreadLocal isolation.
 */
public final class DriverManager {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private DriverManager() {
    }

    public static Playwright getPlaywright() {
        return PLAYWRIGHT.get();
    }

    public static void setPlaywright(Playwright playwright) {
        PLAYWRIGHT.set(playwright);
    }

    public static Browser getBrowser() {
        return BROWSER.get();
    }

    public static void setBrowser(Browser browser) {
        BROWSER.set(browser);
    }

    public static BrowserContext getContext() {
        return CONTEXT.get();
    }

    public static void setContext(BrowserContext context) {
        CONTEXT.set(context);
    }

    public static Page getPage() {
        return PAGE.get();
    }

    public static void setPage(Page page) {
        PAGE.set(page);
    }

    public static void cleanup() {
        Page page = PAGE.get();
        if (page != null) {
            page.close();
        }

        BrowserContext context = CONTEXT.get();
        if (context != null) {
            context.close();
        }

        Browser browser = BROWSER.get();
        if (browser != null) {
            browser.close();
        }

        Playwright playwright = PLAYWRIGHT.get();
        if (playwright != null) {
            playwright.close();
        }

        PAGE.remove();
        CONTEXT.remove();
        BROWSER.remove();
        PLAYWRIGHT.remove();
    }
}

