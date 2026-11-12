package com.automation.utils;

import com.automation.config.AppConfig;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtil() {}

    public static byte[] takeScreenshot(Page page, String testName) {
        try {
            Path screenshotDir = Paths.get(AppConfig.getScreenshotDir());
            Files.createDirectories(screenshotDir);

            String fileName = testName + "_" + LocalDateTime.now().format(FORMATTER) + ".png";
            Path screenshotPath = screenshotDir.resolve(fileName);

            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true));

            log.info("Screenshot saved: {}", screenshotPath);
            return screenshot;
        } catch (IOException e) {
            log.error("Failed to save screenshot", e);
            return new byte[0];
        }
    }
}
