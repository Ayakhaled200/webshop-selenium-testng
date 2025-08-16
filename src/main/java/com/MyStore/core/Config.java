package com.MyStore.core;

import java.io.InputStream;
import java.util.Properties;

public final class Config {
    private static final Properties P = new Properties();

    // Load src/test/resources/config.properties once when the class is first used
    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) P.load(in);
        } catch (Exception ignored) { }
    }

    // Each getter prefers a JVM -D property, falls back to the file, then a hard default
    public static String baseUrl() {
        return System.getProperty("baseUrl",
                P.getProperty("baseUrl", "https://automationexercise.com/"));
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless",
                P.getProperty("headless", "true")));
    }

    public static int timeoutSeconds() {
        return Integer.parseInt(System.getProperty("timeoutSeconds",
                P.getProperty("timeoutSeconds", "10")));
    }

    private Config() {} // utility class, no instances
}
