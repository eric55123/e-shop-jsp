package com.eshop.admin.dao;

import com.eshop.admin.model.Admin;
import com.eshop.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

public class AdminDAO {

    // 查詢全部管理員
    public List<Admin> findAll() {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        List<Admin> list = em.createQuery("FROM Admin", Admin.class).getResultList();
        em.close();
        return list;
    }

    // 依 ID 查找
    public Admin findById(int adminId) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        Admin admin = em.find(Admin.class, adminId);
        em.close();
        return admin;
    }

    // 依帳號查找（登入）
    public Admin findByUsername(String username) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("FROM Admin WHERE username = :username", Admin.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    // 新增
    public void insert(Admin admin) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(admin);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 更新
    public void update(Admin admin) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(admin);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 刪除
    public void delete(int adminId) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Admin admin = em.find(Admin.class, adminId);
            if (admin != null) {
                em.remove(admin);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 更新登入時間
    public void updateLastLogin(int adminId) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Admin admin = em.find(Admin.class, adminId);
            if (admin != null) {
                admin.setLastLogin(LocalDateTime.now());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
