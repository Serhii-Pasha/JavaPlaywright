package com.example.framework.base;

import com.example.framework.config.ConfigManager;
import com.example.framework.driver.DriverManager;
import com.example.framework.driver.PlaywrightFactory;
import com.microsoft.playwright.Page;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        PlaywrightFactory.init();
        Page page = DriverManager.getPage();
        page.navigate(ConfigManager.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.cleanup();
    }
}

