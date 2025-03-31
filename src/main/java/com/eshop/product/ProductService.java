package com.eshop.product;

import com.eshop.product.DAO.ProductDAO;
import com.eshop.product.model.Product;

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
        dao.insert(product);
    }

    public void updateProduct(Product product) {
        dao.update(product);
    }

    public void deleteProduct(int id) {
        dao.delete(id);
    }
}
