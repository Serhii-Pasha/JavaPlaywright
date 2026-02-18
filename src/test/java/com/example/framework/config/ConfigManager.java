package com.example.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Centralized configuration management.
 * Loads properties from config-{env}.properties or config.properties.
 */
public final class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FOLDER = "/config/";
    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    private static final String CONFIG_FILE_PATTERN = "config-%s.properties";

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private ConfigManager() {
    }

    private static void loadProperties() {
        String env = System.getProperty("env");
        String fileName;

        if (env != null && !env.isBlank()) {
            fileName = String.format(CONFIG_FILE_PATTERN, env.toLowerCase());
        } else {
            fileName = DEFAULT_CONFIG_FILE;
        }

        String resourcePath = CONFIG_FOLDER + fileName;
        try (InputStream input = ConfigManager.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                LOGGER.warn("Config file {} not found, falling back to default {}", resourcePath, DEFAULT_CONFIG_FILE);
                try (InputStream defaultInput =
                             ConfigManager.class.getResourceAsStream(CONFIG_FOLDER + DEFAULT_CONFIG_FILE)) {
                    if (defaultInput == null) {
                        throw new IllegalStateException("Default config file " + DEFAULT_CONFIG_FILE + " not found on classpath");
                    }
                    PROPERTIES.load(defaultInput);
                }
            } else {
                PROPERTIES.load(input);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load configuration from " + resourcePath, e);
        }
    }

    public static String get(String key) {
        Objects.requireNonNull(key, "key must not be null");
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            LOGGER.warn("Configuration key '{}' not found", key);
        }
        return value;
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getBrowser() {
        return getOrDefault("browser", "chromium");
    }

    public static boolean isHeadless() {
        String value = getOrDefault("headless", "true");
        return Boolean.parseBoolean(value);
    }

    public static int getTimeoutMillis() {
        String value = getOrDefault("timeout", "10000");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid timeout '{}' in configuration, falling back to 10000 ms", value);
            return 10000;
        }
    }

    private static String getOrDefault(String key, String defaultValue) {
        String value = PROPERTIES.getProperty(key);
        return value != null ? value : defaultValue;
    }
}

