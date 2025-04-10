package com.eshop.member.service;

import com.eshop.member.model.MemberAddress;

import javax.persistence.*;
import java.time.LocalDateTime;

public class MemberAddressService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    /**
     * 根據會員 ID 取得一筆會員地址
     */
    public MemberAddress findByMemberId(Integer memberId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM MemberAddress WHERE memberId = :memberId", MemberAddress.class)
                    .setParameter("memberId", memberId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * 儲存或更新會員地址
     */
    public void saveOrUpdateAddress(Integer memberId, String name, String phone, String address) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            MemberAddress existingAddress;
            try {
                existingAddress = em.createQuery("FROM MemberAddress WHERE memberId = :memberId", MemberAddress.class)
                        .setParameter("memberId", memberId)
                        .setMaxResults(1)
                        .getSingleResult();
                existingAddress.setRecipientName(name);
                existingAddress.setRecipientPhone(phone);
                existingAddress.setAddress(address);
                existingAddress.setUpdatedAt(LocalDateTime.now());
                em.merge(existingAddress);
            } catch (NoResultException e) {
                MemberAddress newAddress = new MemberAddress();
                newAddress.setMemberId(memberId);
                newAddress.setRecipientName(name);
                newAddress.setRecipientPhone(phone);
                newAddress.setAddress(address);
                newAddress.setCreatedAt(LocalDateTime.now());
                newAddress.setUpdatedAt(LocalDateTime.now());
                em.persist(newAddress);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
