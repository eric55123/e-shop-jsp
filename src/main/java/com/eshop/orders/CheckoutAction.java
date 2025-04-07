package com.eshop.orders;

import com.eshop.cartitem.model.CartItem;
import com.eshop.member.model.Member;
import com.eshop.orders.model.Orders;
import com.eshop.orders.model.OrderItem;
import com.eshop.product.model.Product;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

public class CheckoutAction extends ActionSupport {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;

    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loginMember");
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        // ✅ 檢查登入，沒登入就導向 login
        if (member == null) {
            return "login"; // ➜ 回傳 login result 名稱
        }

        if (cart == null || cart.isEmpty()) {
            addActionError("購物車是空的");
            return ERROR;
        }

        // 建立訂單流程
        EntityManager em = Persistence.createEntityManagerFactory("eShopPU").createEntityManager();
        em.getTransaction().begin();

        Orders orders = new Orders();
        orders.setMember(member);
        orders.setReceiverName(receiverName);
        orders.setReceiverPhone(receiverPhone);
        orders.setReceiverAddress(receiverAddress);
        orders.setNote(note);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.values()) {
            Product p = item.getProduct();
            Integer qty = item.getQuantity();
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

        orders.setTotalAmount(total);
        em.persist(orders);
        em.getTransaction().commit();
        em.close();

        session.removeAttribute("cart"); // ✅ 清空購物車

        return SUCCESS;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
}
