package com.eshop.product.service;

import com.eshop.product.dao.ProductDAO;
import com.eshop.product.model.Product;

import java.time.LocalDateTime;
import java.util.List;

public class ProductService {

    private ProductDAO dao = new ProductDAO();

    public List<Product> getAllProducts() {
        return dao.findAll();
    }

    public Product getProductById(int id) {
        return dao.findById(id);
    }

    public void addProduct(Product product) {
        product.setProductAddTime(LocalDateTime.now());
        product.setRemainingQty(product.getProductAddQty());
        dao.insert(product);
    }

    public boolean updateProduct(Product updatedProduct) {
        Product original = dao.findById(updatedProduct.getProductNo());

        if (original == null) {
            return false;
        }

        // 保留不應更改的欄位
        updatedProduct.setProductAddTime(original.getProductAddTime());
        updatedProduct.setProductAddQty(original.getProductAddQty());
        updatedProduct.setRemainingQty(original.getRemainingQty());

        // 處理上下架邏輯
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

        dao.delete(id);
        return true;
    }
}
