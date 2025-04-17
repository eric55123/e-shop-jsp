package com.eshop.cartitem.action;

import com.eshop.cartitem.model.CartItem;
import com.eshop.coupon.model.CouponHolder;
import com.eshop.coupon.service.CouponService;
import com.eshop.member.model.Member;
import com.eshop.member.model.MemberAddress;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductImg;
import com.eshop.product.service.ProductImgService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class ShowCartAction extends ActionSupport {

    private CouponService couponService = new CouponService();

    @Override
    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loginMember");

        if (member != null) {
            System.out.println("[ShowCartAction] 會員已登入，會員ID：" + member.getMemberId());

            // ✅ 取得優惠券清單
            List<CouponHolder> availableCoupons = couponService.findValidCouponHolderList(member.getMemberId());
            session.setAttribute("availableCoupons", availableCoupons);

            // ✅ 取得會員唯一地址
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");
            EntityManager em = emf.createEntityManager();
            try {
                MemberAddress address = em.createQuery(
                                "FROM MemberAddress WHERE memberId = :memberId", MemberAddress.class)
                        .setParameter("memberId", member.getMemberId())
                        .setMaxResults(1)
                        .getSingleResult();

                session.setAttribute("memberAddress", address);
            } catch (NoResultException e) {
                session.setAttribute("memberAddress", null);
            } finally {
                em.close();
                emf.close();
            }
        }

        // ✅ 額外補上：幫每個商品設置主圖
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart != null && !cart.isEmpty()) {
            ProductImgService imgService = new ProductImgService();
            for (CartItem item : cart.values()) {
                Product product = item.getProduct();
                List<ProductImg> imgs = imgService.getImagesByProduct(product);
                if (imgs != null && !imgs.isEmpty()) {
                    product.setCoverImageUrl(imgs.get(0).getProductImgUrl());
                }
            }
        }

        return SUCCESS;
    }
}
