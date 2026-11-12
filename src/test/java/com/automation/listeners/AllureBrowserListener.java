package com.automation.listeners;

import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class AllureBrowserListener extends TestListenerAdapter {

    private static final ThreadLocal<String> browserName = new ThreadLocal<>();

    public static void set(String browser) {
        browserName.set(browser);
    }

    @Override
    public void onTestStart(ITestResult result) {
        String browser = browserName.get();
        if (browser == null) return;

        Allure.label("browser", browser);

        Allure.getLifecycle().updateTestCase(tc -> {
            tc.setName(tc.getName() + " [" + browser + "]");
            if (tc.getHistoryId() != null) {
                tc.setHistoryId(tc.getHistoryId() + "_" + browser);
            }
        });
    }
}
