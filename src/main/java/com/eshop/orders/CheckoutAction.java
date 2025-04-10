package com.eshop.orders;

import com.eshop.cartitem.model.CartItem;
import com.eshop.coupon.service.CouponUsedLogService;
import com.eshop.ecpay.ECPayService;
import com.eshop.member.model.Member;
import com.eshop.member.service.MemberAddressService;
import com.eshop.orders.model.Orders;
import com.eshop.orders.service.OrdersService;
import com.eshop.coupon.service.CouponHolderService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

public class CheckoutAction extends ActionSupport {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;
    private String saveAddress;

    // 注入服務
    private final OrdersService ordersService = new OrdersService();
    private final CouponHolderService couponHolderService = new CouponHolderService();
    private final CouponUsedLogService couponUsedLogService = new CouponUsedLogService();
    private final MemberAddressService memberAddressService = new MemberAddressService();
    private final ECPayService ecPayService = new ECPayService();

    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loginMember");
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        BigDecimal discount = (BigDecimal) session.getAttribute("discount");
        String appliedCouponCode = (String) session.getAttribute("appliedCouponCode");

        if (member == null) return "login";
        if (cart == null || cart.isEmpty()) {
            addActionError("購物車是空的");
            return ERROR;
        }

        try {
            Orders order = ordersService.createOrder(
                    member, cart, receiverName, receiverPhone, receiverAddress, note, discount, appliedCouponCode);

            if (appliedCouponCode != null && discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                couponHolderService.markAsUsed(appliedCouponCode, member.getMemberId());
                couponUsedLogService.logUsage(order, appliedCouponCode, discount, member);
            }

            if ("true".equals(saveAddress)) {
                memberAddressService.saveOrUpdateAddress(
                        member.getMemberId(), receiverName, receiverPhone, receiverAddress);
            }

            // 清空 session 購物資料
            session.removeAttribute("cart");
            session.removeAttribute("discount");
            session.removeAttribute("appliedCouponCode");

            // 轉向綠界付款頁面
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/html;charset=UTF-8");
            String formHtml = ecPayService.generateCheckoutForm(order);
            response.getWriter().write(formHtml);
            response.getWriter().flush();

            return NONE;

        } catch (Exception e) {
            e.printStackTrace();
            addActionError("建立訂單時發生錯誤！");
            return ERROR;
        }
    }

    // Getters and Setters
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getReceiverPhone() { return receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }

    public String getReceiverAddress() { return receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getSaveAddress() { return saveAddress; }
    public void setSaveAddress(String saveAddress) { this.saveAddress = saveAddress; }
}