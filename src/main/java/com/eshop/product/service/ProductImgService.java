package com.eshop.product.service;

import com.eshop.product.dao.ProductImgDAO;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductImg;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class ProductImgService {
    private ProductImgDAO dao = new ProductImgDAO();

    public List<ProductImg> getImagesByProduct(Product product) {
        return dao.findByProduct(product);
    }

    public void uploadImage(String imageUrl, int imgOrder, Product product) {
        ProductImg img = new ProductImg();
        img.setProduct(product);
        img.setProductImgUrl(imageUrl);
        img.setImgOrder(imgOrder);
        img.setCreatedAt(LocalDateTime.now());
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

    // ✅ 將圖片資料 + 實體圖片一起刪除
    public void deleteAllImagesWithFilesByProduct(Product product) {
        List<ProductImg> imgs = getImagesByProduct(product);
        String uploadBasePath = "/opt/tomcat/webapps/ROOT/uploads/";

        for (ProductImg img : imgs) {
            String imgUrl = img.getProductImgUrl();
            String fileNameOnly = new File(imgUrl).getName();
            String fullPath = uploadBasePath + fileNameOnly;

            File file = new File(fullPath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("✅ 實體圖片已刪除：" + fullPath);
                } else {
                    System.out.println("❌ 無法刪除圖片檔案：" + fullPath);
                }
            } else {
                System.out.println("⚠️ 找不到圖片檔案：" + fullPath);
            }

            deleteImg(img.getProductImgNo());
            System.out.println("🗑️ 已刪除資料庫圖片記錄：imgNo = " + img.getProductImgNo());
        }
    }
}