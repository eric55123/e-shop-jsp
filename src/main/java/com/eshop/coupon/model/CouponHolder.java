package com.eshop.coupon.model;
import com.eshop.member.model.Member;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "coupon_holder")
public class CouponHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_holder_id")
    private Integer couponHolderId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "assigned_time")
    private Timestamp assignedTime;

    @Column(name = "used_status")
    private Integer usedStatus;

    @Column(name = "used_time")
    private Timestamp usedTime;

    @Column(name = "expired_time")
    private Timestamp expiredTime;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Integer getCouponHolderId() {
        return couponHolderId;
    }

    public void setCouponHolderId(Integer couponHolderId) {
        this.couponHolderId = couponHolderId;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Timestamp expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Timestamp getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Timestamp usedTime) {
        this.usedTime = usedTime;
    }

    public Integer getUsedStatus() {
        return usedStatus;
    }

    public void setUsedStatus(Integer usedStatus) {
        this.usedStatus = usedStatus;
    }

    public Timestamp getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(Timestamp assignedTime) {
        this.assignedTime = assignedTime;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
