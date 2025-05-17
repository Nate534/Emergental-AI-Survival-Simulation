package com.simulation.shared;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private Properties properties;

    public Config(String filePath) {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key)); // Convert to int
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key)); // Convert to boolean
    }

    public double getDouble(String key) {
        return Double.parseDouble(properties.getProperty(key)); // Convert to double
    }
}
