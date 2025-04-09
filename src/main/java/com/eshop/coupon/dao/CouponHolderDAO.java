package com.eshop.coupon.dao;

import com.eshop.coupon.model.CouponHolder;

import javax.persistence.*;
import java.util.List;

public class CouponHolderDAO {

    private EntityManagerFactory emf;

    public CouponHolderDAO() {
        emf = Persistence.createEntityManagerFactory("eShopPU"); // ⚠️ 換成你自己的名稱
    }

    public CouponHolder findByCouponAndMember(String couponId, Integer memberId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ch FROM CouponHolder ch WHERE ch.coupon.couponId = :couponId AND ch.member.memberId = :memberId",
                            CouponHolder.class)
                    .setParameter("couponId", couponId)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    public void insert(CouponHolder holder) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(holder);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public CouponHolder findByCodeAndMember(String couponCode, Integer memberId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ch FROM CouponHolder ch " +
                                    "JOIN FETCH ch.coupon " +
                                    "WHERE ch.couponCode = :couponCode AND ch.member.memberId = :memberId",
                            CouponHolder.class)
                    .setParameter("couponCode", couponCode)
                    .setParameter("memberId", memberId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<CouponHolder> findValidByMemberId(Integer memberId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT ch FROM CouponHolder ch JOIN FETCH ch.coupon " +
                                    "WHERE ch.member.memberId = :memberId AND ch.usedStatus = 0 " +
                                    "AND (ch.expiredTime IS NULL OR ch.expiredTime > CURRENT_TIMESTAMP)",
                            CouponHolder.class)
                    .setParameter("memberId", memberId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean hasCoupon(Integer memberId, String couponId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COUNT(ch) FROM CouponHolder ch WHERE ch.member.memberId = :memberId AND ch.coupon.couponId = :couponId";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("memberId", memberId)
                    .setParameter("couponId", couponId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}
