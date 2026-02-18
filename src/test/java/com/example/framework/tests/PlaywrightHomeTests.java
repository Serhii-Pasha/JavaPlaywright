package com.example.framework.tests;

import com.example.framework.base.BaseTest;
import com.example.framework.driver.DriverManager;
import com.example.framework.pages.DocsPage;
import com.example.framework.pages.HomePage;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Example UI tests using Page Object Model.
 */
public class PlaywrightHomeTests extends BaseTest {

    @Test(groups = {"smoke"}, description = "Verify that clicking Get started opens docs page with heading")
    public void verifyGetStartedOpensDocs() {
        Page page = DriverManager.getPage();
        HomePage homePage = new HomePage(page);
        DocsPage docsPage = homePage.clickGetStarted();
        String heading = docsPage.getHeadingText();
        Assert.assertTrue(heading != null && !heading.isBlank(), "Heading should not be blank");
    }

    @Test(groups = {"regression"}, description = "Intentional failure example to demonstrate screenshot on failure",
            retryAnalyzer = com.example.framework.retry.RetryAnalyzer.class)
    public void failingTestForScreenshotDemo() {
        Page page = DriverManager.getPage();
        HomePage homePage = new HomePage(page);
        homePage.search("Playwright");
        // This assertion is intentionally wrong to trigger failure and screenshot
        Assert.assertTrue(false, "This test is expected to fail to demo screenshots");
    }
}

