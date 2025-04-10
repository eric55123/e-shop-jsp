package com.eshop.orders.dao;

import com.eshop.orders.model.Orders;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class OrdersDAO {

    public void insert(EntityManager em, Orders orders) {
        em.persist(orders);
    }

    public Orders findById(EntityManager em, Integer id) {
        return em.find(Orders.class, id);
    }

    public void update(EntityManager em, Orders orders) {
        em.merge(orders);
    }

    public void delete(EntityManager em, Orders orders) {
        em.remove(orders);
    }

    public List<Orders> findByMemberId(EntityManager em, Integer memberId) {
        return em.createQuery("FROM Orders WHERE member.memberId = :memberId", Orders.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public Orders findByMerchantTradeNo(EntityManager em, String merchantTradeNo) {
        try {
            int orderId = Integer.parseInt(merchantTradeNo.replace("ESHOP", ""));
            return em.find(Orders.class, orderId);
        } catch (NumberFormatException | NoResultException e) {
            return null;
        }
    }
}
