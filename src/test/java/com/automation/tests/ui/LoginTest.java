package com.automation.tests.ui;

import com.automation.base.BaseTest;
import com.automation.config.AppConfig;
import com.automation.constants.AppConstants;
import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("SauceDemo E-Commerce")
@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(groups = "ui", description = "Verify successful login with valid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that standard_user can log in with valid credentials and land on the inventory page")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(page).open(AppConfig.getUiBaseUrl());

        InventoryPage inventoryPage = loginPage.loginSuccessfully(
                AppConfig.getUiUsername(),
                AppConfig.getUiPassword()
        );

        inventoryPage.waitForPageLoad();

        assertThat(inventoryPage.getPageTitle())
                .as("Inventory page title should be 'Products'")
                .isEqualToIgnoringCase(AppConstants.PRODUCTS_TITLE);

        assertThat(inventoryPage.getCurrentUrl())
                .as("URL should contain inventory path")
                .contains(AppConstants.INVENTORY_PATH);

        log.info("Login test passed — user is on inventory page");
    }

    @Test(groups = "ui", description = "Verify error message for locked out user")
    @Story("User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that locked_out_user receives the appropriate error message")
    public void testLockedOutUserLogin() {
        LoginPage loginPage = new LoginPage(page).open(AppConfig.getUiBaseUrl());

        loginPage.login("locked_out_user", AppConfig.getUiPassword());

        assertThat(loginPage.isErrorDisplayed())
                .as("Error message should be displayed for locked user")
                .isTrue();

        assertThat(loginPage.getErrorMessage())
                .as("Error message should indicate user is locked out")
                .contains("locked out");

        log.info("Locked-out user error validated");
    }

    @Test(groups = "ui", description = "Verify error for invalid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that invalid credentials show the appropriate error message")
    public void testInvalidCredentialsLogin() {
        LoginPage loginPage = new LoginPage(page).open(AppConfig.getUiBaseUrl());

        loginPage.login("invalid_user", "wrong_password");

        assertThat(loginPage.isErrorDisplayed())
                .as("Error message should be displayed for invalid credentials")
                .isTrue();

        assertThat(loginPage.getErrorMessage())
                .as("Error should mention username and password do not match")
                .contains("Username and password do not match");

        log.info("Invalid credentials error validated");
    }
}
