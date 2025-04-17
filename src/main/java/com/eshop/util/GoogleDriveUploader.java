package com.eshop.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleDriveUploader {

    // ✅ 你的 Google Drive 資料夾 ID
    private static final String FOLDER_ID = "1nyduWdeT_6Py4YjPVM7VPS-QtCX6I7Am";

    // ✅ 金鑰檔案名稱（放在 resources 根目錄）
    private static final String CREDENTIALS_FILE_NAME = "eshopjsp-e2e5a5c16eb4.json";

    private static Drive getDriveService() throws IOException, GeneralSecurityException {
        InputStream in = GoogleDriveUploader.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_NAME);

        if (in == null) {
            throw new IOException("❌ 找不到金鑰檔案：" + CREDENTIALS_FILE_NAME);
        }

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("eShop Drive Uploader").build();
    }

    public static String uploadImage(java.io.File imageFile) throws Exception {
        Drive service = getDriveService();

        // ✅ 確保副檔名正確存在
        String originalName = imageFile.getName();
        String ext = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")).toLowerCase() : ".jpg";
        if (!ext.matches("\\.(jpg|jpeg|png|gif)")) {
            ext = ".jpg"; // 預設為 jpg
        }

        String safeFileName = originalName;
        if (!originalName.toLowerCase().endsWith(ext)) {
            safeFileName = originalName + ext;
        }

        // ✅ 設定檔案上傳資訊
        File fileMetadata = new File();
        fileMetadata.setName(safeFileName);
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        // ✅ 根據副檔名設定 MIME Type
        String mimeType = "image/jpeg";
        if (ext.equals(".png")) mimeType = "image/png";
        else if (ext.equals(".gif")) mimeType = "image/gif";

        FileContent mediaContent = new FileContent(mimeType, imageFile);
        File uploadedFile = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        // ✅ 設定公開權限
        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");
        service.permissions().create(uploadedFile.getId(), permission).execute();

        // ✅ 回傳可用圖片網址
        return "https://drive.google.com/thumbnail?id=" + uploadedFile.getId();
    }
    public static void deleteFileById(String fileId) throws Exception {
        Drive service = getDriveService();
        service.files().delete(fileId).execute();
    }


}