package com.eshop.product.service;

import com.eshop.product.dao.ProductImgDAO;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductImg;
import com.eshop.util.GoogleDriveUploader; // ⬅️ 你剛剛那個 uploader 工具類別

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class ProductImgService {
    private ProductImgDAO dao = new ProductImgDAO();

    public List<ProductImg> getImagesByProduct(Product product) {
        return dao.findByProduct(product);
    }

    // ✅ 改為自動上傳到 Google Drive 並取得連結
    public void uploadImage(File imageFile, int imgOrder, Product product) {
        try {
            // ⬇️ 使用 Google Drive API 上傳圖片，取得雲端圖片連結
            String imageUrl = GoogleDriveUploader.uploadImage(imageFile);

            ProductImg img = new ProductImg();
            img.setProduct(product);
            img.setProductImgUrl(imageUrl); // 存 Google Drive 網址
            img.setImgOrder(imgOrder);
            img.setCreatedAt(LocalDateTime.now());
            dao.insert(img);

        } catch (Exception e) {
            System.err.println("❌ 上傳圖片到 Google Drive 失敗：" + e.getMessage());
        }
    }
    public void uploadImage(String imageUrl, int imgOrder, Product product) {
        ProductImg img = new ProductImg();
        img.setProduct(product);
        img.setProductImgUrl(imageUrl);
        img.setImgOrder(imgOrder);
        img.setCreatedAt(java.time.LocalDateTime.now());
        dao.insert(img);
    }




    public void deleteImg(int imgNo) {
        dao.deleteById(imgNo);
    }

    public int getNextImageOrder(Product product) {
        List<ProductImg> existingImgs = dao.findByProduct(product);
        return existingImgs.size() + 1;
    }

    public int getNextImgOrder(Product product) {
        List<ProductImg> imgList = dao.findByProduct(product);
        int maxOrder = 0;
        for (ProductImg img : imgList) {
            if (img.getImgOrder() > maxOrder) {
                maxOrder = img.getImgOrder();
            }
        }
        return maxOrder + 1;
    }

    public ProductImg getImgById(int imgNo) {
        return dao.findById(imgNo);
    }

    // ✅ 之後可改為刪除 Google Drive 上的圖片
    public void deleteAllImagesWithFilesByProduct(Product product) {
        List<ProductImg> imgs = getImagesByProduct(product);

        for (ProductImg img : imgs) {
            // ✅ 從圖片 URL 抽出 Google Drive 檔案 ID
            String url = img.getProductImgUrl();
            if (url != null && url.contains("id=")) {
                String fileId = url.substring(url.indexOf("id=") + 3);
                try {
                    GoogleDriveUploader.deleteFileById(fileId);
                    System.out.println("🗑️ 已刪除 Google Drive 圖片：" + fileId);
                } catch (Exception e) {
                    System.err.println("❌ 無法刪除 Google Drive 圖片：" + fileId);
                    e.printStackTrace();
                }
            }

            // ✅ 刪除資料庫紀錄
            deleteImg(img.getProductImgNo());
            System.out.println("🗑️ 已刪除資料庫圖片記錄：imgNo = " + img.getProductImgNo());
        }
    }

    public void deleteImageWithFile(int imgNo) {
        ProductImg img = dao.findById(imgNo);

        if (img != null && img.getProductImgUrl() != null) {
            String url = img.getProductImgUrl();

            if (url.contains("id=")) {
                String fileId = url.substring(url.indexOf("id=") + 3);
                try {
                    GoogleDriveUploader.deleteFileById(fileId);
                    System.out.println("✅ 已刪除 Google Drive 圖片：" + fileId);
                } catch (Exception e) {
                    System.err.println("❌ 無法刪除 Google Drive 圖片：" + fileId);
                    e.printStackTrace();
                }
            }

            dao.deleteById(imgNo);
            System.out.println("✅ 已刪除資料庫圖片紀錄：imgNo = " + imgNo);
        } else {
            System.out.println("⚠️ 找不到圖片或圖片網址為空：imgNo = " + imgNo);
        }
    }

    public void deleteImageAndDriveFile(ProductImg img) {
        try {
            // 解析圖片 ID
            String url = img.getProductImgUrl();
            if (url != null && url.contains("id=")) {
                String fileId = url.substring(url.indexOf("id=") + 3);
                GoogleDriveUploader.deleteFileById(fileId); // ⬅️ 加上這個方法
            }
        } catch (Exception e) {
            System.err.println("⚠️ 刪除 Google Drive 圖片失敗：" + e.getMessage());
        }

        dao.deleteById(img.getProductImgNo());
    }



}