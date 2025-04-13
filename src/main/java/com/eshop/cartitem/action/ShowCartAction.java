package com.eshop.cartitem.action;

import com.eshop.coupon.model.CouponHolder;
import com.eshop.coupon.service.CouponService;
import com.eshop.member.model.Member;
import com.eshop.member.model.MemberAddress;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.persistence.*;
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

            // ✅ 取得優惠券清單
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

            // ✅ 取得會員唯一地址（自動帶入表單）
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");
            EntityManager em = emf.createEntityManager();
            try {
                MemberAddress address = em.createQuery(
                                "FROM MemberAddress WHERE memberId = :memberId", MemberAddress.class)
                        .setParameter("memberId", member.getMemberId())
                        .setMaxResults(1)
                        .getSingleResult();

                session.setAttribute("memberAddress", address);
                System.out.println("[ShowCartAction] 已載入會員地址：" + address.getAddress());
            } catch (NoResultException e) {
                session.setAttribute("memberAddress", null);
                System.out.println("[ShowCartAction] 尚無會員地址記錄");
            } finally {
                em.close();
                emf.close();
            }

        } else {
            System.out.println("[ShowCartAction] 尚未登入，無法載入優惠券或地址");
        }

        return SUCCESS;
    }
}
