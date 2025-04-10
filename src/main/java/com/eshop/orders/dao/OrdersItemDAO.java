package com.eshop.orders.dao;

import com.eshop.orders.model.OrdersItem;

import javax.persistence.EntityManager;
import java.util.List;

public class OrdersItemDAO {

    public void insert(EntityManager em, OrdersItem item) {
        em.persist(item);
    }

    public void update(EntityManager em, OrdersItem item) {
        em.merge(item);
    }

    public void delete(EntityManager em, Integer itemId) {
        OrdersItem item = em.find(OrdersItem.class, itemId);
        if (item != null) {
            em.remove(item);
        }
    }

    public OrdersItem findById(EntityManager em, Integer itemId) {
        return em.find(OrdersItem.class, itemId);
    }

    public List<OrdersItem> findByOrderId(EntityManager em, Integer orderId) {
        return em.createQuery("FROM OrdersItem WHERE order.orderId = :orderId", OrdersItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
