package com.eshop.member.dao;

import com.eshop.member.model.Member;

import javax.persistence.*;
import java.util.List;

public class MemberDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    // 用 ID 查詢會員
    public Member findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Member.class, id);
        } finally {
            em.close();
        }
    }

    // 用 Email 查詢會員（註冊與登入都會用到）
    public Member findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Member> query = em.createQuery(
                    "FROM Member WHERE email = :email", Member.class);
            query.setParameter("email", email);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    // 新增會員
    public void insert(Member member) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(member);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 更新會員資訊
    public void update(Member member) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(member);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // 查詢所有會員
    public List<Member> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Member> list = em.createQuery("FROM Member", Member.class).getResultList();
        em.close();
        return list;
    }

    // 根據 Email 和 Google Sub 查詢會員
    public Member findByEmailAndGoogleSub(String email, String googleSub) {
        EntityManager em = emf.createEntityManager(); // 使用 entityManager
        try {
            TypedQuery<Member> query = em.createQuery(
                    "FROM Member m WHERE m.email = :email AND m.googleSub = :googleSub", Member.class);
            query.setParameter("email", email);
            query.setParameter("googleSub", googleSub);
            return query.getSingleResult();  // 若查無結果，會丟出 NoResultException
        } catch (NoResultException e) {
            return null;  // 如果查無資料，回傳 null
        } finally {
            em.close();
        }
    }

    // 根據姓名或帳號模糊查詢
    public List<Member> findByNameOrUsernameLike(String keyword) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Member> query = em.createQuery(
                    "FROM Member WHERE LOWER(name) LIKE :kw OR LOWER(username) LIKE :kw", Member.class);
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Member member) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(member);  // 使用 JPA 的 persist 方法來儲存資料
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();  // 出現錯誤時回滾
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
