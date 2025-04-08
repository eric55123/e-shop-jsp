package com.eshop.coupon.dao;

import com.eshop.coupon.model.Coupon;

import javax.persistence.*;
import java.util.List;

public class CouponDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    public void insert(Coupon coupon) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(coupon);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void update(Coupon coupon) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(coupon);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(String couponId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Coupon coupon = em.find(Coupon.class, couponId);
            if (coupon != null) {
                em.remove(coupon);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    public Coupon findById(String couponId) {
        EntityManager em = emf.createEntityManager();
        Coupon coupon = em.find(Coupon.class, couponId);
        em.close();
        return coupon;
    }

    public List<Coupon> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Coupon> list = em.createQuery("FROM Coupon", Coupon.class).getResultList();
        em.close();
        return list;
    }

    public Coupon findByCode(String couponCode) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Coupon> query = em.createQuery("FROM Coupon WHERE couponCode = :code", Coupon.class);
        query.setParameter("code", couponCode);
        Coupon result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            // 查無資料，返回 null
        } finally {
            em.close();
        }
        return result;
    }

    public List<Coupon> findEnabled() {
        EntityManager em = emf.createEntityManager();
        List<Coupon> list = em.createQuery("FROM Coupon WHERE isEnabled = true", Coupon.class).getResultList();
        em.close();
        return list;
    }
}
