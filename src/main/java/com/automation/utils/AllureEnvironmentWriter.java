package com.automation.utils;

import com.automation.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public final class AllureEnvironmentWriter {

    private static final Logger log = LoggerFactory.getLogger(AllureEnvironmentWriter.class);
    private static final AtomicBoolean written = new AtomicBoolean(false);

    private AllureEnvironmentWriter() {}

    public static void writeOnce() {
        if (!written.compareAndSet(false, true)) return;
        try {
            String resultsDir = System.getProperty("allure.results.directory", "target/allure-results");
            Path dir = Paths.get(resultsDir);
            Files.createDirectories(dir);

            String content = String.join(System.lineSeparator(),
                    "OS=" + System.getProperty("os.name") + " " + System.getProperty("os.version"),
                    "Java=" + System.getProperty("java.version"),
                    "Playwright=" + System.getProperty("playwright.version", "unknown"),
                    "UI.BaseURL=" + AppConfig.getUiBaseUrl(),
                    "API.BaseURL=" + AppConfig.getApiBaseUrl()
            );

            Files.writeString(dir.resolve("environment.properties"), content);
            log.info("Allure environment.properties written to {}", dir);
        } catch (IOException e) {
            log.error("Failed to write Allure environment.properties", e);
        }
    }
}
