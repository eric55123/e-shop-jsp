package com.eshop.orders;

import com.eshop.cartitem.model.CartItem;
import com.eshop.coupon.model.CouponHolder;
import com.eshop.coupon.model.CouponUsedLog;

import com.eshop.member.model.Member;
import com.eshop.member.model.MemberAddress;
import com.eshop.orders.model.Orders;
import com.eshop.orders.model.OrderItem;
import com.eshop.orders.service.InventoryService;
import com.eshop.product.model.Product;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

import javax.persistence.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Map;

public class CheckoutAction extends ActionSupport {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;
    private String saveAddress;

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

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Orders orders = new Orders();
            orders.setMember(member);
            orders.setReceiverName(receiverName);
            orders.setReceiverPhone(receiverPhone);
            orders.setReceiverAddress(receiverAddress);
            orders.setNote(note);

            BigDecimal total = BigDecimal.ZERO;
            InventoryService inventoryService = new InventoryService();

            for (CartItem item : cart.values()) {
                Product p = item.getProduct();
                Integer qty = item.getQuantity();

                inventoryService.reserveStock(em, p.getProductNo(), qty);

                BigDecimal price = p.getProductPrice();
                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(qty));

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(orders);
                orderItem.setProduct(p);
                orderItem.setQuantity(qty);
                orderItem.setUnitPrice(price);
                orderItem.setSubtotal(subtotal);

                orders.getItems().add(orderItem);
                total = total.add(subtotal);
            }

            if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                total = total.subtract(discount);
                if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;

                orders.setDiscountAmount(discount);
                orders.setAppliedCouponCode(appliedCouponCode);
            }

            orders.setTotalAmount(total);
            em.persist(orders);

            handleCouponUsage(em, member, appliedCouponCode, discount, orders);
            handleAddressSave(em, member);

            tx.commit();
            session.removeAttribute("cart");
            session.removeAttribute("discount");
            session.removeAttribute("appliedCouponCode");

            return redirectToECPay(orders, total);

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            addActionError("建立訂單時發生錯誤！");
            return ERROR;
        } finally {
            em.close();
            emf.close();
        }
    }

    private void handleCouponUsage(EntityManager em, Member member, String code, BigDecimal discount, Orders order) {
        if (code != null && discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
            try {
                CouponHolder holder = em.createQuery("SELECT ch FROM CouponHolder ch WHERE ch.couponCode = :code AND ch.member.memberId = :memberId", CouponHolder.class)
                        .setParameter("code", code)
                        .setParameter("memberId", member.getMemberId())
                        .getSingleResult();

                holder.setUsedStatus(1);
                holder.setUsedTime(new Timestamp(System.currentTimeMillis()));
                em.merge(holder);

                CouponUsedLog log = new CouponUsedLog();
                log.setOrder(order);
                log.setCouponHolder(holder);
                log.setDiscountAmount(discount);
                log.setMember(member);
                log.setAppliedTime(new Timestamp(System.currentTimeMillis()));
                log.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                log.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

                em.persist(log);
            } catch (NoResultException e) {
                System.err.println("⚠ 無法找到對應的 CouponHolder");
            }
        }
    }

    private void handleAddressSave(EntityManager em, Member member) {
        if ("true".equals(saveAddress)) {
            try {
                MemberAddress addr;
                try {
                    addr = em.createQuery("FROM MemberAddress WHERE memberId = :memberId", MemberAddress.class)
                            .setParameter("memberId", member.getMemberId())
                            .setMaxResults(1)
                            .getSingleResult();
                    addr.setRecipientName(receiverName);
                    addr.setRecipientPhone(receiverPhone);
                    addr.setAddress(receiverAddress);
                    addr.setUpdatedAt(LocalDateTime.now());
                    em.merge(addr);
                } catch (NoResultException e) {
                    addr = new MemberAddress();
                    addr.setMemberId(member.getMemberId());
                    addr.setRecipientName(receiverName);
                    addr.setRecipientPhone(receiverPhone);
                    addr.setAddress(receiverAddress);
                    addr.setCreatedAt(LocalDateTime.now());
                    addr.setUpdatedAt(LocalDateTime.now());
                    em.persist(addr);
                }
            } catch (Exception e) {
                System.err.println("⚠ 儲存地址失敗：" + e.getMessage());
            }
        }
    }

    private String redirectToECPay(Orders orders, BigDecimal total) throws Exception {
        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo("ESHOP" + orders.getOrderId());
        obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date()));
        obj.setTotalAmount(String.valueOf(total.intValue()));
        obj.setTradeDesc("E-shop 訂單");
        obj.setItemName("商品共 " + orders.getItems().size() + " 項");
        obj.setReturnURL("https://5987-36-227-210-125.ngrok-free.app/ecpayReturn.action");
        obj.setOrderResultURL("https://5987-36-227-210-125.ngrok-free.app/ecpay/result.jsp");
        obj.setNeedExtraPaidInfo("N");

        String configPath = this.getClass().getClassLoader().getResource("payment_conf.xml").getPath();
        AllInOne all = new AllInOne(configPath);
        String htmlForm = all.aioCheckOut(obj, null);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(htmlForm);
        response.getWriter().flush();

        return NONE;
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
