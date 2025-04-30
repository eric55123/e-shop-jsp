package com.eshop.product.dao;

import com.eshop.product.model.CommentReport;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductComment;
import com.eshop.util.HibernateUtil;

import javax.persistence.*;
import java.util.List;

public class ProductCommentDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    // 新增評論
    public void insert(ProductComment comment) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(comment);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 查詢某商品的公開評論（已解決 LazyInitializationException）
    public List<ProductComment> findByProduct(Product product) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM ProductComment c JOIN FETCH c.member " +
                                    "WHERE c.product = :product AND c.status IN (0, 1) " +
                                    "ORDER BY c.commentTime DESC", ProductComment.class)
                    .setParameter("product", product)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // 查詢單一評論
    public ProductComment findById(int commentId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ProductComment.class, commentId);
        } finally {
            em.close();
        }
    }

    // 更新評論狀態（例如 0: 刪除, -1: 封鎖）
    public void updateStatus(int commentId, byte newStatus) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery(
                            "UPDATE ProductComment c SET c.status = :status WHERE c.commentId = :id")
                    .setParameter("status", newStatus)
                    .setParameter("id", commentId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 更新留言內容與評分
    public void update(ProductComment comment) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(comment); // 使用 merge 以避免 DetachedEntity 問題
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}
