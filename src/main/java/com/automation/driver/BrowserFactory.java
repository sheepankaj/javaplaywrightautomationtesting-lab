package com.automation.driver;

import com.automation.config.AppConfig;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BrowserFactory {

    private static final Logger log = LoggerFactory.getLogger(BrowserFactory.class);

    private BrowserFactory() {}

    public static Browser createBrowser(Playwright playwright, String browserType) {
        boolean headless = AppConfig.isHeadless();
        int slowMo = AppConfig.getSlowMo();

        log.info("Launching {} | headless={} | slowMo={}ms", browserType, headless, slowMo);

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo);

        return switch (browserType.toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }

    public static BrowserContext createContext(Browser browser) {
        return browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(AppConfig.getViewportWidth(), AppConfig.getViewportHeight())
                .setIgnoreHTTPSErrors(true));
    }
}
