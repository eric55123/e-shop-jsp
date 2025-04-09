package com.eshop.coupon.service;

import com.eshop.coupon.dao.CouponDAO;
import com.eshop.coupon.dao.CouponHolderDAO;
import com.eshop.coupon.model.Coupon;
import com.eshop.coupon.model.CouponHolder;
import com.eshop.member.model.Member;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class CouponService {

    private CouponHolderDAO couponHolderDAO = new CouponHolderDAO();
    private CouponDAO couponDAO = new CouponDAO();

    /**
     * 新增優惠券
     * @param coupon 要新增的優惠券物件
     * @throws RuntimeException 若代碼重複
     */
    public void createCoupon(Coupon coupon) {
        // 檢查 couponCode 是否已存在
        if (couponDAO.findByCode(coupon.getCouponCode()) != null) {
            throw new RuntimeException("優惠券代碼已存在，請重新輸入！");
        }

        // 自動補上 createdAt（如果欄位未填）
        if (coupon.getCreatedAt() == null) {
            coupon.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        }

        couponDAO.insert(coupon);
    }

    /**
     * 修改優惠券
     */
    public void updateCoupon(Coupon coupon) {
        // 更新 updatedAt 時間
        coupon.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        couponDAO.update(coupon);
    }

    /**
     * 查詢單一優惠券
     */
    public Coupon getCouponById(String couponId) {
        return couponDAO.findById(couponId);
    }

    /**
     * 查全部優惠券
     */
    public java.util.List<Coupon> getAllCoupons() {
        return couponDAO.findAll();
    }

    /**
     * 查啟用中的優惠券
     */
    public java.util.List<Coupon> getEnabledCoupons() {
        return couponDAO.findEnabled();
    }

    public void deleteCoupon(String couponId) {
        couponDAO.delete(couponId);
    }

    public void assignCouponToMember(Coupon coupon, Member member) {
        // 檢查是否已經發放過這張券給該會員
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

    public CouponHolder findValidCouponHolder(String couponCode, Integer memberId) {
        CouponHolder holder = couponHolderDAO.findByCodeAndMember(couponCode, memberId);

        if (holder == null) return null;

        boolean isValid = holder.getUsedStatus() == 0 &&
                (holder.getExpiredTime() == null || holder.getExpiredTime().after(new java.util.Date()));

        return isValid ? holder : null;
    }

    public List<CouponHolder> findValidCouponHolderList(Integer memberId) {
        return couponHolderDAO.findValidByMemberId(memberId);
    }

    public boolean hasCoupon(Integer memberId, String couponId) {
        return couponHolderDAO.hasCoupon(memberId, couponId);
    }

}
