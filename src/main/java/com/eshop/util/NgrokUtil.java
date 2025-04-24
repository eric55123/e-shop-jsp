package com.eshop.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NgrokUtil {

    private static final String NGROK_FILE_PATH = "/var/lib/tomcat9/ngrok_url.txt";

    public static String getNgrokUrl() {
        try (BufferedReader reader = new BufferedReader(new FileReader(NGROK_FILE_PATH))) {
            String url = reader.readLine();
            if (url == null || url.trim().isEmpty()) {
                System.out.println("⚠️ ngrok_url.txt 是空的！");
                return null;
            }
            System.out.println("✅ Ngrok URL 讀取成功：" + url);
            return url.trim();
        } catch (IOException e) {
            System.out.println("❌ 無法讀取 ngrok_url.txt：" + e.getMessage());
            return null;
        }
    }
}
