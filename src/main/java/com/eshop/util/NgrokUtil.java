package com.eshop.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NgrokUtil {

    private static final String NGROK_FILE_PATH = "C:\\Users\\ken09\\Downloads\\ngrok_url.txt"; // ⚠️ 改為你的 Windows 路徑

    public static String getNgrokUrl() {
        try (BufferedReader reader = new BufferedReader(new FileReader(NGROK_FILE_PATH))) {
            String json = reader.readLine();
            if (json == null || json.trim().isEmpty()) {
                System.out.println("⚠️ ngrok_url.txt 是空的！");
                return null;
            }

            // 從 JSON 中擷取 "public_url":"..." 的值
            int start = json.indexOf("\"public_url\":\"") + 14;
            int end = json.indexOf("\"", start);
            if (start < 14 || end == -1) {
                System.out.println("⚠️ 無法從 JSON 中解析出網址！");
                return null;
            }

            String url = json.substring(start, end);
            System.out.println("✅ Ngrok URL 讀取成功：" + url);
            return url;
        } catch (IOException e) {
            System.out.println("❌ 無法讀取 ngrok_url.txt：" + e.getMessage());
            return null;
        }
    }
}
