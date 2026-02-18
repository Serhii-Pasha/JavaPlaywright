package com.example.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Example Page Object for https://playwright.dev home page.
 */
public class HomePage {

    private final Page page;

    // Locators
    private final Locator getStartedLink;
    private final Locator searchInput;

    public HomePage(Page page) {
        this.page = page;
        this.getStartedLink = page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Get started"));
        this.searchInput = page.getByPlaceholder("Search");
    }

    public HomePage search(String query) {
        searchInput.click();
        searchInput.fill(query);
        return this;
    }

    public DocsPage clickGetStarted() {
        getStartedLink.click();
        return new DocsPage(page);
    }
}

