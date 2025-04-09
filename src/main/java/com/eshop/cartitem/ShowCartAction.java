package com.eshop.cartitem;

import com.eshop.coupon.model.CouponHolder;
import com.eshop.coupon.service.CouponService;
import com.eshop.member.model.Member;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class ShowCartAction extends ActionSupport {

    private CouponService couponService = new CouponService(); // 或用 setter 注入

    @Override
    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loginMember");

        if (member != null) {
            System.out.println("[ShowCartAction] 會員已登入，會員ID：" + member.getMemberId());

            List<CouponHolder> availableCoupons = couponService.findValidCouponHolderList(member.getMemberId());
            session.setAttribute("availableCoupons", availableCoupons);

            System.out.println("[ShowCartAction] 找到 " + (availableCoupons != null ? availableCoupons.size() : "0") + " 張可用優惠券");

            if (availableCoupons != null) {
                for (CouponHolder ch : availableCoupons) {
                    System.out.println("　→ 優惠券代碼：" + ch.getCouponCode() +
                            "｜名稱：" + (ch.getCoupon() != null ? ch.getCoupon().getName() : "無名稱") +
                            "｜使用狀態：" + ch.getUsedStatus());
                }
            }
        } else {
            System.out.println("[ShowCartAction] 尚未登入，無法載入優惠券");
        }

        return SUCCESS;
    }
}
