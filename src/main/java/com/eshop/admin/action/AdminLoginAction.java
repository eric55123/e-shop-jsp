package com.eshop.admin.action;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AdminLoginAction extends ActionSupport {

    private String username;
    private String password;

    private AdminService adminService = new AdminService();

    // 登入
    @Override
    public String execute() {
        Admin admin = adminService.login(username, password);

        if (admin != null) {
            // 登入成功，存入 session
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("loggedInAdmin", admin);
            return SUCCESS;
        } else {
            // 登入失敗
            addActionError("帳號或密碼錯誤，或帳號已停用！");
            return INPUT;
        }
    }

    // 登出
    public String logout() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession(false); // false 表示如果沒有 session 就不建立
        if (session != null) {
            session.invalidate();
        }
        return SUCCESS;
    }

    // Getter & Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
