package com.eshop.cartitem;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;

public class ClearCartAction extends ActionSupport {
    @Override
    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.removeAttribute("cart"); // ✅ 清除購物車
        return SUCCESS;
    }
}

