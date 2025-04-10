package com.eshop.coupon.service;

import com.eshop.coupon.dao.CouponHolderDAO;
import com.eshop.coupon.model.Coupon;
import com.eshop.coupon.model.CouponHolder;
import com.eshop.member.model.Member;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class CouponHolderService {

    private final CouponHolderDAO couponHolderDAO = new CouponHolderDAO();

    /**
     * 發放優惠券給會員
     */
    public void assignCouponToMember(Coupon coupon, Member member) {
        CouponHolder existing = couponHolderDAO.findByCouponAndMember(coupon.getCouponId(), member.getMemberId());
        if (existing != null) {
            throw new RuntimeException("此優惠券已發放給該會員！");
        }

        CouponHolder holder = new CouponHolder();
        holder.setCoupon(coupon);
        holder.setMember(member);
        holder.setCouponCode(coupon.getCouponCode());
        holder.setAssignedTime(new Timestamp(System.currentTimeMillis()));
        holder.setUsedStatus(0);
        holder.setExpiredTime(new Timestamp(coupon.getValidTo().getTime()));
        holder.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        couponHolderDAO.insert(holder);
    }

    /**
     * 依據會員與代碼查詢仍有效的持券紀錄
     */
    public CouponHolder findValidCouponHolder(String couponCode, Integer memberId) {
        CouponHolder holder = couponHolderDAO.findByCodeAndMember(couponCode, memberId);
        if (holder == null) return null;

        boolean isValid = holder.getUsedStatus() == 0 &&
                (holder.getExpiredTime() == null || holder.getExpiredTime().after(new Date()));

        return isValid ? holder : null;
    }

    /**
     * 查詢會員目前所有有效的持券紀錄
     */
    public List<CouponHolder> findValidCouponHolderList(Integer memberId) {
        return couponHolderDAO.findValidByMemberId(memberId);
    }

    /**
     * 檢查會員是否已持有某張優惠券
     */
    public boolean hasCoupon(Integer memberId, String couponId) {
        return couponHolderDAO.hasCoupon(memberId, couponId);
    }

    /**
     * ✅ 將持券狀態設為已使用
     */
    public void markAsUsed(String couponCode, Integer memberId) {
        EntityManager em = Persistence.createEntityManagerFactory("eShopPU").createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            CouponHolder holder = em.createQuery(
                            "SELECT ch FROM CouponHolder ch WHERE ch.couponCode = :code AND ch.member.memberId = :memberId",
                            CouponHolder.class)
                    .setParameter("code", couponCode)
                    .setParameter("memberId", memberId)
                    .getSingleResult();

            holder.setUsedStatus(1);
            holder.setUsedTime(new Timestamp(System.currentTimeMillis()));

            em.merge(holder);
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
}
