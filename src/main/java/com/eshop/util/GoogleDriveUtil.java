package com.eshop.util;

public class GoogleDriveUtil {

    /**
     * 將 Google Drive 分享連結轉換成可用於 <img> 的圖片連結
     * 範例：
     * 輸入：https://drive.google.com/file/d/1ABCdEfGhIJKLmnopQ12345XYZ678/view?usp=sharing
     * 回傳：https://drive.google.com/uc?export=view&id=1ABCdEfGhIJKLmnopQ12345XYZ678
     */
    public static String convertToImageUrl(String googleDriveUrl) {
        try {
            String id = null;

            // 匹配 ID 的正規表示式
            String regex = "https://drive.google.com/file/d/([a-zA-Z0-9_-]+)";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(googleDriveUrl);

            if (matcher.find()) {
                id = matcher.group(1);
            }

            if (id != null) {
                return "https://drive.google.com/uc?export=view&id=" + id;
            } else {
                return "⚠️ 不是有效的 Google Drive 連結格式";
            }
        } catch (Exception e) {
            return "⚠️ 發生錯誤：" + e.getMessage();
        }
    }

    // 可直接測試看看
    public static void main(String[] args) {
        String originalUrl = "https://drive.google.com/file/d/1ABCdEfGhIJKLmnopQ12345XYZ678/view?usp=sharing";
        String imgUrl = convertToImageUrl(originalUrl);
        System.out.println("✅ 可用圖片網址：" + imgUrl);
    }
}

