package com.eshop.admin.action;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminLogService;
import com.eshop.admin.service.AdminService;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AdminLoginAction extends ActionSupport {

    private String username;
    private String password;

    private AdminLogService adminLogService = new AdminLogService();
    private AdminService adminService = new AdminService();

    // 登入
    @Override
    public String execute() {
        Admin admin = adminService.login(username, password);

        if (admin != null) {
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("loggedInAdmin", admin);

            // ✅ 登入成功紀錄 log
            String ip = RequestUtil.getClientIp();
            adminLogService.log(admin.getAdminId(), "login", null, null, "管理員登入成功", ip);

            return SUCCESS;
        } else {
            addActionError("帳號或密碼錯誤，或帳號已停用！");
            return INPUT;
        }
    }

    // 登出
    public String logout() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            Admin admin = (Admin) session.getAttribute("loggedInAdmin");

            // ✅ 登出 log
            if (admin != null) {
                String ip = RequestUtil.getClientIp();
                adminLogService.log(admin.getAdminId(), "logout", null, null, "管理員登出", ip);
            }

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
