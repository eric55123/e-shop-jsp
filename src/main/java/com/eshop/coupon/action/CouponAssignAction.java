package com.eshop.coupon.action;

import com.eshop.admin.service.AdminLogService;
import com.eshop.coupon.model.Coupon;
import com.eshop.coupon.service.CouponService;
import com.eshop.member.model.Member;
import com.eshop.member.service.MemberService;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CouponAssignAction extends ActionSupport {

    private AdminLogService adminLogService = new AdminLogService();
    private CouponService couponService = new CouponService();
    private MemberService memberService = new MemberService();

    private List<Coupon> couponList;
    private Coupon coupon;
    private List<Member> memberList;
    private Integer memberId;
    private List<Integer> memberIds;
    private String message;

    // 篩選條件
    private String filterType;
    private Date startDate;
    private Date endDate;
    private String keyword;

    // 顯示發放選擇券列表
    public String assignSelect() {
        this.couponList = couponService.getAllCoupons();
        return SUCCESS;
    }

    // 顯示發放表單（含篩選會員）
    public String showAssignForm() {
        if (coupon != null && coupon.getCouponId() != null) {
            coupon = couponService.getCouponById(coupon.getCouponId());
        }
        memberList = getFilteredMembers();
        return SUCCESS;
    }

    // 發放給單一或多位會員
    public String assignCoupon() {
        try {
            if (coupon != null && coupon.getCouponId() != null) {
                Coupon selectedCoupon = couponService.getCouponById(coupon.getCouponId());

                int success = 0;
                int skip = 0;

                if (memberIds != null && !memberIds.isEmpty()) {
                    for (Integer id : memberIds) {
                        Member member = memberService.getMemberById(id);
                        if (!couponService.hasCoupon(member.getMemberId(), selectedCoupon.getCouponId())) {
                            couponService.assignCouponToMember(selectedCoupon, member);
                            success++;
                        } else {
                            skip++;
                        }
                    }
                } else if (memberId != null) {
                    Member member = memberService.getMemberById(memberId);
                    if (!couponService.hasCoupon(member.getMemberId(), selectedCoupon.getCouponId())) {
                        couponService.assignCouponToMember(selectedCoupon, member);
                        success++;
                    } else {
                        skip++;
                    }
                } else {
                    addActionError("請選擇要發放的會員");
                    return ERROR;
                }

                message = String.format("成功發放 %d 位，略過 %d 位（已擁有）", success, skip);
                memberList = getFilteredMembers();

                adminLogService.log(
                        RequestUtil.getLoggedInAdmin().getAdminId(),
                        "assign_coupon",
                        "coupon",
                        coupon.getCouponId(),
                        String.format("發放優惠券「%s」給 %d 位會員（略過 %d 位）", selectedCoupon.getName(), success, skip),
                        RequestUtil.getClientIp()
                );
                return SUCCESS;
            } else {
                addActionError("發放資料不完整");
                return ERROR;
            }
        } catch (Exception e) {
            addActionError("發放失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // 發放給全部符合篩選的會員
    public String assignSelectedCoupon() {
        try {
            if (coupon != null && coupon.getCouponId() != null) {
                Coupon selectedCoupon = couponService.getCouponById(coupon.getCouponId());
                List<Member> targetMembers = getFilteredMembers();

                int success = 0;
                int skip = 0;

                for (Member member : targetMembers) {
                    if (!couponService.hasCoupon(member.getMemberId(), selectedCoupon.getCouponId())) {
                        couponService.assignCouponToMember(selectedCoupon, member);
                        success++;
                    } else {
                        skip++;
                    }
                }
                message = String.format("成功發放 %d 位，略過 %d 位（已擁有）", success, skip);
                memberList = targetMembers;

                // 發放完成後記錄 log
                adminLogService.log(
                        RequestUtil.getLoggedInAdmin().getAdminId(),
                        "assign_coupon_all",
                        "coupon",
                        coupon.getCouponId(),
                        String.format("批次發放優惠券「%s」給 %d 位會員（略過 %d 位）", selectedCoupon.getName(), success, skip),
                        RequestUtil.getClientIp()
                );

                return SUCCESS;
            } else {
                addActionError("發放資料不完整");
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

    // Getter / Setter
    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }
    public List<Coupon> getCouponList() { return couponList; }
    public void setCouponList(List<Coupon> couponList) { this.couponList = couponList; }
    public List<Member> getMemberList() { return memberList; }
    public void setMemberList(List<Member> memberList) { this.memberList = memberList; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public List<Integer> getMemberIds() { return memberIds; }
    public void setMemberIds(List<Integer> memberIds) { this.memberIds = memberIds; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getFilterType() { return filterType; }
    public void setFilterType(String filterType) { this.filterType = filterType; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
}