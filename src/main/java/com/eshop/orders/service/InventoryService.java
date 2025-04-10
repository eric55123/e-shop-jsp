package com.eshop.orders.service;

import com.eshop.product.model.Product;

import javax.persistence.EntityManager;

public class InventoryService {
    public void reserveStock(EntityManager em, Integer productNo, int quantity) {
        Product product = em.find(Product.class, productNo);
        if (product == null || product.getRemainingQty() < quantity) {
            throw new IllegalArgumentException("庫存不足或商品不存在！");
        }
        product.setRemainingQty(product.getRemainingQty() - quantity);
        em.merge(product);
    }
}
