package com.eshop.cartitem.action;

import com.eshop.cartitem.model.CartItem;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class UpdateCartAction extends ActionSupport {
    private int productNo;
    private int quantity;

    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (cart != null && cart.containsKey(productNo)) {
            if (quantity <= 0) {
                cart.remove(productNo); // 數量 <= 0 則當作刪除
            } else {
                CartItem item = cart.get(productNo);
                item.setQuantity(quantity);
            }
        }

        return SUCCESS;
    }

    // Getter / Setter
    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
