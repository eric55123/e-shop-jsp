package com.eshop.cartitem;

import com.eshop.cartitem.model.CartItem;
import com.eshop.coupon.model.Coupon;
import com.eshop.coupon.model.CouponHolder;
import com.eshop.coupon.service.CouponService;
import com.eshop.member.model.Member;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

public class ApplyCouponAction extends ActionSupport {

    private String couponCode;
    private CouponService couponService = new CouponService(); // 或使用 setter 注入

    @Override
    public String execute() throws Exception {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loginMember");
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (member == null || cart == null || cart.isEmpty()) {
            addActionError("請先登入並加入商品到購物車");
            return ERROR;
        }

        CouponHolder holder = couponService.findValidCouponHolder(couponCode, member.getMemberId());

        if (holder == null) {
            addActionError("此優惠券無效、已使用或已過期");
            return ERROR;
        }

        Coupon coupon = holder.getCoupon();
        BigDecimal total = calculateCartTotal(cart);
        BigDecimal discount = BigDecimal.ZERO;

        if ("fixed".equalsIgnoreCase(coupon.getDiscountType())) {
            discount = coupon.getDiscountValue();
        } else if ("percent".equalsIgnoreCase(coupon.getDiscountType())) {
            BigDecimal percent = coupon.getDiscountValue().divide(new BigDecimal(100));
            discount = total.multiply(percent);
        }

        if (discount.compareTo(total) > 0) {
            discount = total;
        }

        session.setAttribute("discount", discount);
        session.setAttribute("appliedCouponCode", couponCode);

        return SUCCESS;
    }

    private BigDecimal calculateCartTotal(Map<Integer, CartItem> cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.values()) {
            total = total.add(item.getProduct().getProductPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }
}
