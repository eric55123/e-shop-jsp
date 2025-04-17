package com.eshop.product.service;

import com.eshop.product.dao.ProductDAO;
import com.eshop.product.dao.ProductImgDAO;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductImg;
import com.eshop.util.GoogleDriveUploader;

import java.time.LocalDateTime;
import java.util.List;

public class ProductService {

    private ProductDAO dao = new ProductDAO();
    private ProductImgDAO imgDAO = new ProductImgDAO(); // ✅ 加上圖片 DAO

    // ✅ 查詢全部商品，並補上主圖（封面圖）
    public List<Product> getAllProducts() {
        List<Product> list = dao.findAll();

        for (Product p : list) {
            List<ProductImg> imgs = imgDAO.findByProduct(p);
            p.setProductImgs(imgs); // 若你希望詳情頁可用

            if (imgs != null && !imgs.isEmpty()) {
                p.setCoverImageUrl(imgs.get(0).getProductImgUrl()); // ✅ 主圖：第一張圖
            }
        }

        return list;
    }

    // 查詢單一商品
    public Product getProductById(int id) {
        return dao.findById(id);
    }

    // ✅ 新增商品並回傳主鍵
    public int addProduct(Product product) {
        product.setProductAddTime(LocalDateTime.now());
        product.setRemainingQty(product.getProductAddQty());
        return dao.insertAndReturnId(product);
    }

    // ✅ 修改商品
    public boolean updateProduct(Product updatedProduct) {
        Product original = dao.findById(updatedProduct.getProductNo());
        if (original == null) return false;

        updatedProduct.setProductAddTime(original.getProductAddTime());
        updatedProduct.setProductAddQty(original.getProductAddQty());
        updatedProduct.setRemainingQty(original.getRemainingQty());

        if (original.getProductStatus() == 1 && updatedProduct.getProductStatus() == 0) {
            updatedProduct.setProductRemoveTime(LocalDateTime.now());
        } else {
            updatedProduct.setProductRemoveTime(original.getProductRemoveTime());
        }

        dao.update(updatedProduct);
        return true;
    }

    public boolean deleteProduct(int id) {
        if (id <= 0) return false;

        // 先查詢商品
        Product product = dao.findById(id);
        if (product == null) return false;

        // 查詢所有圖片
        List<ProductImg> imgs = imgDAO.findByProduct(product);

        for (ProductImg img : imgs) {
            String url = img.getProductImgUrl();
            if (url != null && url.contains("id=")) {
                String fileId = url.substring(url.indexOf("id=") + 3);
                try {
                    GoogleDriveUploader.deleteFileById(fileId); // 呼叫刪除 API
                } catch (Exception e) {
                    System.err.println("❌ 刪除 Google Drive 圖片失敗: " + fileId);
                    e.printStackTrace();
                }
            }
        }

        // 最後刪除商品（會因為 cascade = ALL 順便刪圖片記錄）
        dao.delete(id);
        return true;
    }


    // 依主鍵查詢
    public Product findById(int productNo) {
        return dao.findById(productNo);
    }
}
