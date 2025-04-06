package com.eshop.product.DAO;

import com.eshop.product.model.Product;

import javax.persistence.*;
import java.util.List;

public class ProductDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    public List<Product> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Product> products = em.createQuery("FROM Product", Product.class).getResultList();
        em.close();
        return products;
    }

    public Product findById(int productNo) {
        EntityManager em = emf.createEntityManager();
        Product product = em.find(Product.class, productNo);
        em.close();
        return product;
    }

    public void insert(Product product) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(product);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void update(Product product) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(product);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(int productNo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product product = em.find(Product.class, productNo);
            if (product != null) {
                // 初始化圖片清單以避免 Lazy loading 問題
                product.getProductImgs().size();
                em.remove(product);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
