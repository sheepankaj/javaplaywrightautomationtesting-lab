package com.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {

    protected final Page page;
    protected final Logger log;

    protected BasePage(Page page) {
        this.page = page;
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    protected void click(String selector) {
        log.debug("Clicking: {}", selector);
        page.locator(selector).click();
    }

    protected void fill(String selector, String value) {
        log.debug("Filling '{}' into: {}", value, selector);
        page.locator(selector).fill(value);
    }

    protected String getText(String selector) {
        return page.locator(selector).textContent();
    }

    protected boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    protected void waitForVisible(String selector) {
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
    }

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        page.navigate(url);
    }

    public String getPageTitle() {
        return getText(".title");
    }

    public String getCurrentUrl() {
        return page.url();
    }
}
