package com.eshop.coupon.service;

import com.eshop.coupon.dao.CouponHolderDAO;
import com.eshop.coupon.dao.CouponUsedLogDAO;
import com.eshop.coupon.model.CouponHolder;
import com.eshop.coupon.model.CouponUsedLog;
import com.eshop.member.model.Member;
import com.eshop.orders.model.Orders;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CouponUsedLogService {

    private final CouponUsedLogDAO couponUsedLogDAO = new CouponUsedLogDAO();
    private final CouponHolderDAO couponHolderDAO = new CouponHolderDAO();

    /**
     * 建立一筆使用紀錄（根據優惠券代碼自動查詢 CouponHolder）
     */
    public void logUsage(Orders order, String couponCode, BigDecimal discount, Member member) {
        if (couponCode == null || discount == null || discount.compareTo(BigDecimal.ZERO) <= 0) return;

        CouponHolder holder = getHolderByCode(couponCode, member.getMemberId());
        if (holder == null) return;

        CouponUsedLog log = new CouponUsedLog();
        log.setOrder(order);
        log.setCouponHolder(holder);
        log.setDiscountAmount(discount);
        log.setMember(member);
        log.setAppliedTime(new Timestamp(System.currentTimeMillis()));
        log.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        log.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        couponUsedLogDAO.insert(log);
    }

    /**
     * 建立一筆使用紀錄（直接傳入 CouponHolder）
     */
    public void logCouponUsage(Orders order, CouponHolder holder, BigDecimal discount, Member member) {
        if (holder == null || discount == null || discount.compareTo(BigDecimal.ZERO) <= 0) return;

        CouponUsedLog log = new CouponUsedLog();
        log.setOrder(order);
        log.setCouponHolder(holder);
        log.setDiscountAmount(discount);
        log.setMember(member);
        log.setAppliedTime(new Timestamp(System.currentTimeMillis()));
        log.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        log.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        couponUsedLogDAO.insert(log);
    }

    /**
     * 查詢指定會員的指定代碼的 CouponHolder
     */
    private CouponHolder getHolderByCode(String couponCode, Integer memberId) {
        try {
            return couponHolderDAO.findByCodeAndMember(couponCode, memberId);
        } catch (Exception e) {
            return null;
        }
    }
}
