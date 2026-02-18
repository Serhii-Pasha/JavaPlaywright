package com.example.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;


public class DocsPage {

    private final Page page;
    private final Locator heading;

    public DocsPage(Page page) {
        this.page = page;
        this.heading = page.locator("h1");
    }

    public String getHeadingText() {
        return heading.textContent();
    }
}

