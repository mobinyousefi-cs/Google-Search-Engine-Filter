/*
 * =====================================================================================================================
 * Project: Google Search Engine Filter
 * File: Config.java
 * Author: Mobin Yousefi (GitHub: https://github.com/mobinyousefi-cs)
 * Created: 2025-11-19
 * Updated: 2025-11-19
 * License: MIT License (see LICENSE file for details)
 * =====================================================================================================================
 */

package com.mobinyousefi.googlesearchfilter.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Thin wrapper around java.util.Properties with a few convenience methods.
 */
public class Config {

    private final Properties properties = new Properties();

    public Config(String resourceName) throws IOException {
        Objects.requireNonNull(resourceName, "resourceName must not be null");
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IOException("Configuration resource not found on classpath: " + resourceName);
            }
            properties.load(in);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getRequired(String key) throws IOException {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IOException("Required config key missing or blank: " + key);
        }
        return value.trim();
    }
}
