package com.eshop.coupon.model;


import com.eshop.member.model.Member;
import com.eshop.orders.model.Orders;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "coupon_used_log")
public class CouponUsedLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "used_id")
    private Integer usedId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "coupon_holder_id")
    private CouponHolder couponHolder;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "applied_time")
    private Timestamp appliedTime;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
