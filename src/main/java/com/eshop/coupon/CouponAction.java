package com.eshop.coupon;

import com.eshop.coupon.model.Coupon;
import com.eshop.coupon.service.CouponService;
import com.eshop.member.model.Member;
import com.eshop.member.service.MemberService;
import com.opensymphony.xwork2.ActionSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CouponAction extends ActionSupport {


    private MemberService memberService = new MemberService();
    private CouponService couponService = new CouponService();
    private Coupon coupon;
    private List<Coupon> couponList;
    private String message;
    private Integer memberId;

    // 👉 新增優惠券
    public String create() {
        try {
            // 預設空值處理：沒勾選視為停用
            if (coupon.getIsEnabled() == null) {
                coupon.setIsEnabled(0);
            }

            // 清空空字串
            if (coupon.getDescription() != null && coupon.getDescription().trim().isEmpty()) {
                coupon.setDescription(null);
            }

            // 時間轉換（字串轉 java.sql.Timestamp）
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

    // 👉 修改優惠券
    public String update() {
        try {
            if (coupon.getIsEnabled() == null) {
                coupon.setIsEnabled(0); // 停用
            }

            if (coupon.getDescription() != null && coupon.getDescription().trim().isEmpty()) {
                coupon.setDescription(null);
            }

            // 轉換日期格式
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
    public String assignSelect() {
        couponList = couponService.getAllCoupons(); // 把所有優惠券帶給畫面
        return SUCCESS;
    }


    // 👉 查詢單一券
    public String find() {
        coupon = couponService.getCouponById(coupon.getCouponId());

        // 顯示時預填日期欄位
        if (coupon.getValidFrom() != null) {
            coupon.setValidFromStr(new SimpleDateFormat("yyyy-MM-dd").format(coupon.getValidFrom()));
        }
        if (coupon.getValidTo() != null) {
            coupon.setValidToStr(new SimpleDateFormat("yyyy-MM-dd").format(coupon.getValidTo()));
        }

        return SUCCESS;
    }

    public String list() {
        couponList = couponService.getAllCoupons();
        return SUCCESS;
    }
    public String showAssignForm() {
        // 可以取得 couponId 做查詢後預設處理
        if (coupon != null && coupon.getCouponId() != null) {
            coupon = couponService.getCouponById(coupon.getCouponId());
        }

        memberList = memberService.getAllMembers();
        return SUCCESS;
    }

    private List<Member> memberList;

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public String assignCoupon() {
        try {
            if (coupon != null && coupon.getCouponId() != null && memberId != null) {
                Coupon selectedCoupon = couponService.getCouponById(coupon.getCouponId());
                Member member = memberService.getMemberById(memberId);

                couponService.assignCouponToMember(selectedCoupon, member);
                message = "✅ 優惠券已成功發放給會員";
                return SUCCESS;
            } else {
                addActionError("發放資料不完整！");
                return ERROR;
            }
        } catch (Exception e) {
            addActionError("發放失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // ==== getter / setter for Struts2 表單資料綁定 ====

    public MemberService getMemberService() {
        return memberService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public CouponService getCouponService() {
        return couponService;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }



    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}