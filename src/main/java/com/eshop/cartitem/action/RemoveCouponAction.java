package com.eshop.cartitem.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;

public class RemoveCouponAction extends ActionSupport {

    @Override
    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();

        session.removeAttribute("discount");
        session.removeAttribute("appliedCouponCode");

        return SUCCESS;
    }
}
