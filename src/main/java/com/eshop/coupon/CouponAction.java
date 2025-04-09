package com.eshop.coupon;

import com.eshop.coupon.model.Coupon;
import com.eshop.coupon.service.CouponService;
import com.eshop.member.model.Member;
import com.eshop.member.service.MemberService;
import com.opensymphony.xwork2.ActionSupport;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CouponAction extends ActionSupport {

    private CouponService couponService = new CouponService();
    private MemberService memberService = new MemberService();

    private Coupon coupon;
    private List<Coupon> couponList;
    private List<Member> memberList;
    private Integer memberId;
    private List<Integer> memberIds;
    private String message;

    // 篩選條件
    private String filterType;
    private Date startDate;
    private Date endDate;
    private String keyword;

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

    // ✅ 全部券
    public String list() {
        couponList = couponService.getAllCoupons();
        return SUCCESS;
    }

    // ✅ 顯示新增表單
    public String showAddForm() {
        return SUCCESS;
    }

    public String assignSelect() {
        couponList = couponService.getAllCoupons();
        return SUCCESS;
    }

    // ✅ 顯示發放券選單（有篩選）
    public String showAssignForm() {
        if (coupon != null && coupon.getCouponId() != null) {
            coupon = couponService.getCouponById(coupon.getCouponId());
        }
        memberList = getFilteredMembers();
        return SUCCESS;
    }

    // ✅ 發放給單一會員
    public String assignCoupon() {
        try {
            if (coupon != null && coupon.getCouponId() != null) {
                Coupon selectedCoupon = couponService.getCouponById(coupon.getCouponId());

                // 如果有多選名單，就逐一處理
                if (memberIds != null && !memberIds.isEmpty()) {
                    for (Integer id : memberIds) {
                        Member member = memberService.getMemberById(id);
                        couponService.assignCouponToMember(selectedCoupon, member);
                    }
                    message = "✅ 已成功發放給勾選的會員";
                }
                // 如果只有單一會員 ID
                else if (memberId != null) {
                    Member member = memberService.getMemberById(memberId);
                    couponService.assignCouponToMember(selectedCoupon, member);
                    message = "✅ 優惠券已成功發放給該會員";
                }
                // 如果都沒有
                else {
                    addActionError("⚠️ 請至少選擇一位會員發送！");
                    return ERROR;
                }

                memberList = getFilteredMembers();
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


    // ✅ 發放給全部會員（後端重新以篩選條件取得）
    public String assignSelectedCoupon() {
        try {
            if (coupon != null && coupon.getCouponId() != null) {
                Coupon selectedCoupon = couponService.getCouponById(coupon.getCouponId());
                List<Member> targetMembers = getFilteredMembers();

                for (Member member : targetMembers) {
                    couponService.assignCouponToMember(selectedCoupon, member);
                }
                message = "✅ 已成功發放給篩選條件的會員";
                memberList = targetMembers;
                return SUCCESS;
            } else {
                addActionError("⚠️ 發放資料不完整");
                return ERROR;
            }
        } catch (Exception e) {
            addActionError("發放失敗：" + e.getMessage());
            return ERROR;
        }
    }

    private List<Member> getFilteredMembers() {
        List<Member> baseList;

        if ("birthday".equals(filterType)) {
            baseList = memberService.getMembersWithBirthdayInCurrentMonth();
        } else if ("registerRange".equals(filterType) && startDate != null && endDate != null) {
            LocalDateTime startLdt = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime endLdt = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            baseList = memberService.getMembersByRegisterDateRange(startLdt, endLdt);
        } else {
            baseList = memberService.getAllMembers();
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            return memberService.filterMembersByKeyword(baseList, keyword);
        }

        return baseList;
    }

    // ==== Getter / Setter ====
    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }
    public List<Coupon> getCouponList() { return couponList; }
    public void setCouponList(List<Coupon> couponList) { this.couponList = couponList; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public List<Member> getMemberList() { return memberList; }
    public void setMemberList(List<Member> memberList) { this.memberList = memberList; }
    public CouponService getCouponService() { return couponService; }
    public void setCouponService(CouponService couponService) { this.couponService = couponService; }
    public MemberService getMemberService() { return memberService; }
    public void setMemberService(MemberService memberService) { this.memberService = memberService; }
    public String getFilterType() { return filterType; }
    public void setFilterType(String filterType) { this.filterType = filterType; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public List<Integer> getMemberIds() { return memberIds; }
    public void setMemberIds(List<Integer> memberIds) { this.memberIds = memberIds; }
}
