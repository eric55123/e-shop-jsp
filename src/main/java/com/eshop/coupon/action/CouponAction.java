package com.eshop.coupon.action;

import com.eshop.coupon.model.Coupon;
import com.eshop.coupon.service.CouponService;
import com.opensymphony.xwork2.ActionSupport;

import java.text.SimpleDateFormat;

public class CouponAction extends ActionSupport {

    private CouponService couponService = new CouponService();

    private Coupon coupon;
    private String message;

    // ✅ 顯示新增表單
    public String showAddForm() {
        return SUCCESS;
    }

    // ✅ 新增優惠券
    public String create() {
        try {
            if (coupon.getIsEnabled() == null) {
                coupon.setIsEnabled(0);
            }
            if (coupon.getDescription() != null && coupon.getDescription().trim().isEmpty()) {
                coupon.setDescription(null);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (coupon.getValidFromStr() != null)
                coupon.setValidFrom(new java.sql.Timestamp(sdf.parse(coupon.getValidFromStr()).getTime()));
            if (coupon.getValidToStr() != null)
                coupon.setValidTo(new java.sql.Timestamp(sdf.parse(coupon.getValidToStr()).getTime()));

            couponService.createCoupon(coupon);
            message = "新增成功";
            return SUCCESS;

        } catch (Exception e) {
            addActionError("新增失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // ✅ 修改優惠券
    public String update() {
        try {
            if (coupon.getIsEnabled() == null) {
                coupon.setIsEnabled(0);
            }
            if (coupon.getDescription() != null && coupon.getDescription().trim().isEmpty()) {
                coupon.setDescription(null);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (coupon.getValidFromStr() != null)
                coupon.setValidFrom(new java.sql.Timestamp(sdf.parse(coupon.getValidFromStr()).getTime()));
            if (coupon.getValidToStr() != null)
                coupon.setValidTo(new java.sql.Timestamp(sdf.parse(coupon.getValidToStr()).getTime()));

            couponService.updateCoupon(coupon);
            message = "修改成功";
            return SUCCESS;

        } catch (Exception e) {
            addActionError("修改失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // ✅ 刪除
    public String delete() {
        try {
            couponService.deleteCoupon(coupon.getCouponId());
            message = "刪除成功";
            return SUCCESS;
        } catch (Exception e) {
            addActionError("刪除失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // ✅ 查單一券
    public String find() {
        coupon = couponService.getCouponById(coupon.getCouponId());
        if (coupon.getValidFrom() != null) {
            coupon.setValidFromStr(new SimpleDateFormat("yyyy-MM-dd").format(coupon.getValidFrom()));
        }
        if (coupon.getValidTo() != null) {
            coupon.setValidToStr(new SimpleDateFormat("yyyy-MM-dd").format(coupon.getValidTo()));
        }
        return SUCCESS;
    }

    // ==== Getter / Setter ====
    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public CouponService getCouponService() { return couponService; }
    public void setCouponService(CouponService couponService) { this.couponService = couponService; }
}
