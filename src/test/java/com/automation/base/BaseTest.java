package com.automation.base;

import com.automation.config.AppConfig;
import com.automation.driver.BrowserFactory;
import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import com.automation.listeners.AllureBrowserListener;
import com.automation.utils.AllureEnvironmentWriter;
import com.automation.utils.ScreenshotUtil;
import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.ByteArrayInputStream;

public abstract class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional String browserParam) {
        String browserType = (browserParam != null && !browserParam.isBlank())
                ? browserParam
                : AppConfig.getBrowser();

        AllureEnvironmentWriter.writeOnce();
        AllureBrowserListener.set(browserType);

        log.info("=== Test Setup [{}] ===", browserType);
        playwright = Playwright.create();
        browser = BrowserFactory.createBrowser(playwright, browserType);
        context = BrowserFactory.createContext(browser);
        page = context.newPage();
        page.setDefaultTimeout(AppConfig.getTimeout());
        log.info("Browser session initialized [{}]", browserType);
    }

    protected InventoryPage loginAsStandardUser() {
        return new LoginPage(page)
                .open(AppConfig.getUiBaseUrl())
                .loginSuccessfully(AppConfig.getUiUsername(), AppConfig.getUiPassword())
                .waitForPageLoad();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && AppConfig.isScreenshotOnFailure()) {
            log.warn("Test FAILED: {} — capturing screenshot", result.getName());
            byte[] screenshot = ScreenshotUtil.takeScreenshot(page, result.getName());
            if (screenshot.length > 0) {
                Allure.addAttachment("Failure Screenshot", "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
            }
        }

        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();

        log.info("=== Test Teardown Complete: {} ===", result.getName());
    }
}
