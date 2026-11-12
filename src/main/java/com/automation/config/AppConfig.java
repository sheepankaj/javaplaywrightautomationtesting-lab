package com.automation.config;

public final class AppConfig {

    private static final ConfigManager config = ConfigManager.getInstance();

    private AppConfig() {}

    public static String getBrowser() { return config.get("browser"); }
    public static boolean isHeadless() { return config.getBoolean("headless"); }
    public static int getSlowMo() { return config.getInt("slowMo"); }
    public static int getTimeout() { return config.getInt("timeout"); }
    public static int getViewportWidth() { return config.getInt("viewport.width"); }
    public static int getViewportHeight() { return config.getInt("viewport.height"); }

    public static String getUiBaseUrl() { return config.get("ui.base.url"); }
    public static String getUiUsername() { return config.get("ui.username"); }
    public static String getUiPassword() { return config.get("ui.password"); }

    public static String getApiBaseUrl() { return config.get("api.base.url"); }
    public static int getApiTestUserId() { return config.getInt("api.test.user.id"); }
    public static String getExpectedUserName() { return config.get("api.expected.name"); }
    public static String getExpectedUsername() { return config.get("api.expected.username"); }
    public static String getExpectedEmail() { return config.get("api.expected.email"); }

    public static boolean isScreenshotOnFailure() { return config.getBoolean("screenshot.on.failure"); }
    public static String getScreenshotDir() { return config.get("screenshot.dir"); }
}
