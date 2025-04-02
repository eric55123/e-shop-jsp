package com.eshop.product.DAO;

import com.eshop.product.Model.Product;
import com.eshop.product.Model.ProductComment;

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
                                    "WHERE c.product = :product AND c.status = 1 " +
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

    // 更新評論狀態（0: 使用者刪除，-1: 管理員封鎖）
    public void updateStatus(int commentId, int newStatus) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ProductComment comment = em.find(ProductComment.class, commentId);
            if (comment != null) {
                comment.setStatus(newStatus);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
