package com.eshop.faq.dao;

import com.eshop.faq.model.Faq;

import javax.persistence.*;
import java.util.List;

public class FaqDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    // ✅ 查詢啟用中 FAQ（前台用）
    public List<Faq> findAllEnabled() {
        EntityManager em = emf.createEntityManager();
        List<Faq> result = em.createQuery("FROM Faq WHERE isEnabled = 1 ORDER BY sortOrder ASC", Faq.class)
                .getResultList();
        em.close();
        return result;
    }

    // ✅ 查詢所有 FAQ（後台用）
    public List<Faq> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Faq> result = em.createQuery("FROM Faq ORDER BY sortOrder ASC", Faq.class)
                .getResultList();
        em.close();
        return result;
    }

    public void save(Faq faq) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(faq);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Faq findById(int id) {
        EntityManager em = emf.createEntityManager();
        Faq faq = em.find(Faq.class, id);
        em.close();
        return faq;
    }

    public void update(Faq faq) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(faq);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Faq faq = em.find(Faq.class, id);
            if (faq != null) {
                em.remove(faq);
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
