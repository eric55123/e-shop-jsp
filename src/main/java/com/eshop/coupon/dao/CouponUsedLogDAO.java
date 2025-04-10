package com.eshop.coupon.dao;

import com.eshop.coupon.model.CouponUsedLog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CouponUsedLogDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    public void insert(CouponUsedLog log) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(log);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 若未來有需要查詢紀錄，可以再加上 findByOrderId 或 findByMemberId 等方法
}
