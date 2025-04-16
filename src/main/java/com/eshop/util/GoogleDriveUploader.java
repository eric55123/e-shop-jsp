package com.eshop.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleDriveUploader {

    // ✅ 替換成你實際的資料夾 ID
    private static final String FOLDER_ID = "1nyduWdeT_6Py4YjPVM7VPS-QtCX6I7Am";

    // ✅ 替換為你的金鑰檔案路徑
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials/你的-json金鑰檔案.json";

    private static Drive getDriveService() throws IOException, GeneralSecurityException {
        GoogleCredential credential = GoogleCredential.fromStream(
                        java.nio.file.Files.newInputStream(Paths.get(CREDENTIALS_FILE_PATH)))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("eShop Drive Uploader").build();
    }

    public static String uploadImage(java.io.File imageFile) throws Exception {
        Drive service = getDriveService();

        File fileMetadata = new File();
        fileMetadata.setName(imageFile.getName());
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        FileContent mediaContent = new FileContent("image/jpeg", imageFile); // 可改判斷副檔名
        File uploadedFile = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        // 設定圖片公開權限
        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");
        service.permissions().create(uploadedFile.getId(), permission).execute();

        // 回傳 <img> 可用網址
        return "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();
    }
}

