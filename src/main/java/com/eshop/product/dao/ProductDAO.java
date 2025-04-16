package com.eshop.product.dao;

import com.eshop.product.model.Product;
import com.eshop.util.JPAUtil;

import javax.persistence.*;
import java.util.List;

public class ProductDAO {

    public List<Product> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Product> products = em.createQuery("FROM Product", Product.class).getResultList();
        em.close();
        return products;
    }

    public Product findById(int productNo) {
        EntityManager em = JPAUtil.getEntityManager();
        Product product = em.find(Product.class, productNo);
        em.close();
        return product;
    }

    public void insert(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
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
        EntityManager em = JPAUtil.getEntityManager();
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
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product product = em.find(Product.class, productNo);
            if (product != null) {
                product.getProductImgs().size(); // 預載圖片防 LazyException
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

    public int insertAndReturnId(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(product); // 主鍵會由 JPA 自動生成並回填
            tx.commit();
            return product.getProductNo(); // 回傳產生的主鍵
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return -1;
        } finally {
            em.close();
        }
    }

}
