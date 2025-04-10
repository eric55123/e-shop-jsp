package com.eshop.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final Properties props = new Properties();

    static {
        try {
            InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
            if (is != null) {
                props.load(is);
            } else {
                System.out.println("⚠️ 找不到 config.properties");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
