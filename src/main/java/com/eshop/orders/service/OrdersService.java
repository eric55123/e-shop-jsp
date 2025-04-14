package com.eshop.orders.service;

import com.eshop.cartitem.model.CartItem;
import com.eshop.coupon.model.CouponHolder;
import com.eshop.coupon.model.CouponUsedLog;
import com.eshop.member.model.Member;
import com.eshop.member.model.MemberAddress;
import com.eshop.orders.model.Orders;
import com.eshop.orders.model.OrdersItem;
import com.eshop.product.model.Product;
import com.eshop.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class OrdersService {

    private final InventoryService inventoryService = new InventoryService();

    /**
     * 建立訂單（自動處理 EntityManager）
     */
    public Orders createOrder(Member member, Map<Integer, CartItem> cart, String receiverName,
                              String receiverPhone, String receiverAddress, String note,
                              BigDecimal discount, String appliedCouponCode) {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        Orders order = new Orders();

        try {
            tx.begin();

            order.setMember(member);
            order.setReceiverName(receiverName);
            order.setReceiverPhone(receiverPhone);
            order.setReceiverAddress(receiverAddress);
            order.setNote(note);

            BigDecimal total = BigDecimal.ZERO;

            for (CartItem item : cart.values()) {
                Product p = item.getProduct();
                int qty = item.getQuantity();
                BigDecimal price = p.getProductPrice();
                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(qty));

                inventoryService.reserveStock(em, p.getProductNo(), qty);

                OrdersItem orderItem = new OrdersItem();
                orderItem.setOrder(order);
                orderItem.setProduct(p);
                orderItem.setQuantity(qty);
                orderItem.setUnitPrice(price);
                orderItem.setSubtotal(subtotal);

                order.getItems().add(orderItem);
                total = total.add(subtotal);
            }

            if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                total = total.subtract(discount);
                if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;

                order.setDiscountAmount(discount);
                order.setAppliedCouponCode(appliedCouponCode);
            }

            order.setTotalAmount(total);
            em.persist(order);

            tx.commit();
            return order;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * 處理優惠券使用狀態與紀錄
     */
    public void processCouponUsage(Member member, Orders order, String appliedCouponCode, BigDecimal discount) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            CouponHolder holder = em.createQuery(
                            "SELECT ch FROM CouponHolder ch WHERE ch.couponCode = :code AND ch.member.memberId = :memberId",
                            CouponHolder.class)
                    .setParameter("code", appliedCouponCode)
                    .setParameter("memberId", member.getMemberId())
                    .getSingleResult();

            holder.setUsedStatus((byte)1);
            holder.setUsedTime(new Timestamp(System.currentTimeMillis()));
            em.merge(holder);

            CouponUsedLog log = new CouponUsedLog();
            log.setOrder(order);
            log.setCouponHolder(holder);
            log.setDiscountAmount(discount);
            log.setMember(member);
            log.setAppliedTime(new Timestamp(System.currentTimeMillis()));
            log.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            log.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            em.persist(log);

            tx.commit();
        } catch (NoResultException e) {
            System.err.println("⚠ 無法找到對應的 CouponHolder");
            if (tx.isActive()) tx.rollback();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * 儲存或更新會員地址
     */
    public void saveOrUpdateMemberAddress(Member member, String receiverName, String receiverPhone, String receiverAddress) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            MemberAddress addr;
            try {
                addr = em.createQuery("FROM MemberAddress WHERE memberId = :memberId", MemberAddress.class)
                        .setParameter("memberId", member.getMemberId())
                        .setMaxResults(1)
                        .getSingleResult();

                addr.setRecipientName(receiverName);
                addr.setRecipientPhone(receiverPhone);
                addr.setAddress(receiverAddress);
                addr.setUpdatedAt(LocalDateTime.now());
                em.merge(addr);
            } catch (NoResultException e) {
                addr = new MemberAddress();
                addr.setMemberId(member.getMemberId());
                addr.setRecipientName(receiverName);
                addr.setRecipientPhone(receiverPhone);
                addr.setAddress(receiverAddress);
                addr.setCreatedAt(LocalDateTime.now());
                addr.setUpdatedAt(LocalDateTime.now());
                em.persist(addr);
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