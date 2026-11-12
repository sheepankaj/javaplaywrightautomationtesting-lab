package com.automation.pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private static final String USERNAME_INPUT = "#user-name";
    private static final String PASSWORD_INPUT = "#password";
    private static final String LOGIN_BUTTON = "#login-button";
    private static final String ERROR_MESSAGE = "[data-test='error']";

    public LoginPage(Page page) {
        super(page);
    }

    public LoginPage open(String baseUrl) {
        navigateTo(baseUrl);
        waitForVisible(LOGIN_BUTTON);
        log.info("Login page opened");
        return this;
    }

    public void login(String username, String password) {
        fill(USERNAME_INPUT, username);
        fill(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }

    public InventoryPage loginSuccessfully(String username, String password) {
        fill(USERNAME_INPUT, username);
        fill(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        log.info("Logged in as '{}'", username);
        return new InventoryPage(page);
    }

    public String getErrorMessage() {
        waitForVisible(ERROR_MESSAGE);
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }
}
