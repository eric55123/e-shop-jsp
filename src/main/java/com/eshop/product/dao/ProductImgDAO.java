package com.eshop.product.dao;

import com.eshop.product.model.Product;
import com.eshop.product.model.ProductImg;

import javax.persistence.*;
import java.util.List;

public class ProductImgDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    // ✅ 查詢：找出某個商品的所有圖片（依順序）
    public List<ProductImg> findByProduct(Product product) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM ProductImg p WHERE p.product = :product ORDER BY p.imgOrder",
                            ProductImg.class)
                    .setParameter("product", product)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // ✅ 新增圖片記錄
    public void insert(ProductImg img) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(img);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ✅ 刪除圖片（by 主鍵）
    public void deleteById(int imgNo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ProductImg img = em.find(ProductImg.class, imgNo);
            if (img != null) {
                em.remove(img);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ✅ 查單一圖片（by 主鍵）
    public ProductImg findById(int imgNo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ProductImg.class, imgNo);
        } finally {
            em.close();
        }
    }
}
