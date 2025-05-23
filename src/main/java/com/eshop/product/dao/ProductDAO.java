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
        Product product = null;
        try {
            product = em.createQuery(
                            "SELECT p FROM Product p LEFT JOIN FETCH p.productImgs WHERE p.productNo = :productNo", Product.class)
                    .setParameter("productNo", productNo)
                    .getSingleResult();
        } catch (NoResultException e) {
            // 可能找不到商品
        } finally {
            em.close();
        }
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

    public List<Product> findByCategoryId(Integer categoryId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Product WHERE productCategory.productCategoryId = :categoryId", Product.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Product> findByPage(int pageNo, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Product ORDER BY productNo", Product.class)
                    .setFirstResult((pageNo - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public int getTotalPages(int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class)
                    .getSingleResult();
            return (int) Math.ceil((double) count / pageSize);
        } finally {
            em.close();
        }
    }

    public List<Product> findByCategoryWithPage(int categoryId, int pageNo, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "FROM Product WHERE productCategory.productCategoryId = :categoryId ORDER BY productNo", Product.class)
                    .setParameter("categoryId", categoryId)
                    .setFirstResult((pageNo - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public int getTotalPagesByCategory(int categoryId, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Product p WHERE p.productCategory.productCategoryId = :categoryId", Long.class)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
            return (int) Math.ceil((double) count / pageSize);
        } finally {
            em.close();
        }
    }



}
