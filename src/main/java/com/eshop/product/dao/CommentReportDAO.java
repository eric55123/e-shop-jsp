package com.eshop.product.dao;



import com.eshop.product.model.CommentReport;
import com.eshop.util.HibernateUtil;

import javax.persistence.*;
import java.util.List;

public class CommentReportDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    public void insert(CommentReport report) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(report);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<CommentReport> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM CommentReport", CommentReport.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<CommentReport> findUnprocessed() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM CommentReport WHERE status = 0", CommentReport.class).getResultList();
        } finally {
            em.close();
        }
    }

    public CommentReport findById(int reportId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(CommentReport.class, reportId);
        } finally {
            em.close();
        }
    }

    public void update(CommentReport report) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(report);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    // ✅ 查詢某會員檢舉過哪些留言
    public List<Integer> findReportedCommentIdsByMember(int memberId) {
        EntityManager em = emf.createEntityManager(); // ✅ 補這行
        try {
            String jpql = "SELECT r.comment.commentId FROM CommentReport r WHERE r.reporter.memberId = :memberId";
            TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
            query.setParameter("memberId", memberId);
            return query.getResultList();
        } finally {
            em.close(); //
        }
    }
    public List<CommentReport> findPendingReports() {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        List<CommentReport> list = em.createQuery(
                "SELECT r FROM CommentReport r " +
                        "JOIN FETCH r.comment c " +
                        "JOIN FETCH r.reporter m " +
                        "WHERE r.status = 0", CommentReport.class
        ).getResultList();
        em.close();
        return list;
    }

    public boolean existsByCommentIdAndReporter(int commentId, int memberId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM CommentReport r WHERE r.comment.commentId = :commentId AND r.reporter.memberId = :memberId";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("commentId", commentId)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

}