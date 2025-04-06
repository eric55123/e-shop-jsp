package com.eshop.product.DAO;

import com.eshop.product.model.Product;
import com.eshop.product.model.ProductImg;

import javax.persistence.*;
import java.util.List;

public class ProductImgDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    public List<ProductImg> findByProduct(Product product) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "FROM ProductImg WHERE product = :product ORDER BY imgOrder", ProductImg.class)
                    .setParameter("product", product)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void insert(ProductImg img) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(img);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

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
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public ProductImg findById(int imgNo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ProductImg.class, imgNo); // 透過主鍵查詢
        } finally {
            em.close();
        }
    }

}
