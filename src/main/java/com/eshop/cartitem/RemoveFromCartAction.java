package com.eshop.cartitem;

import com.eshop.cartitem.model.CartItem;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class RemoveFromCartAction extends ActionSupport {
    private int productNo;

    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (cart != null) {
            cart.remove(productNo);
        }

        return SUCCESS;
    }

    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }
}
