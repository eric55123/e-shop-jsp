package com.eshop.product.service;

import com.eshop.product.dao.ProductDAO;
import com.eshop.product.model.Product;

import java.time.LocalDateTime;
import java.util.List;

public class ProductService {

    private ProductDAO dao = new ProductDAO();

    // 查詢全部商品
    public List<Product> getAllProducts() {
        return dao.findAll();
    }

    // 查詢單一商品
    public Product getProductById(int id) {
        return dao.findById(id);
    }

    // ✅ 新增商品並回傳主鍵
    public int addProduct(Product product) {
        // 補充欄位
        product.setProductAddTime(LocalDateTime.now());
        product.setRemainingQty(product.getProductAddQty());
        return dao.insertAndReturnId(product); // 改用 DAO 的 persist 方法
    }

    // ✅ 修改商品（更新欄位後交由 DAO 更新）
    public boolean updateProduct(Product updatedProduct) {
        Product original = dao.findById(updatedProduct.getProductNo());
        if (original == null) return false;

        // 不可被前端修改的欄位保留
        updatedProduct.setProductAddTime(original.getProductAddTime());
        updatedProduct.setProductAddQty(original.getProductAddQty());
        updatedProduct.setRemainingQty(original.getRemainingQty());

        // 處理上下架時間邏輯
        if (original.getProductStatus() == 1 && updatedProduct.getProductStatus() == 0) {
            updatedProduct.setProductRemoveTime(LocalDateTime.now());
        } else {
            updatedProduct.setProductRemoveTime(original.getProductRemoveTime());
        }

        dao.update(updatedProduct);
        return true;
    }

    // ✅ 刪除商品
    public boolean deleteProduct(int id) {
        if (id <= 0) return false;
        dao.delete(id);
        return true;
    }

    // 依主鍵查詢
    public Product findById(int productNo) {
        return dao.findById(productNo);
    }
}
