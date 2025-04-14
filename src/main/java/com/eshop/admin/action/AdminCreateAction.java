package com.eshop.admin.action;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminLogService;
import com.eshop.admin.service.AdminService;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;

public class AdminCreateAction extends ActionSupport {

    private Admin newAdmin;
    private AdminService adminService = new AdminService();
    private AdminLogService adminLogService = new AdminLogService(); // ✅ 加入 log service

    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");

        // ✅ 權限檢查
        if (loggedInAdmin == null || !"super".equalsIgnoreCase(loggedInAdmin.getRole())) {
            addActionError("您沒有新增管理員的權限！");
            return "unauthorized";
        }

        // ✅ 第一次進入畫面（不是送出表單）
        if (newAdmin == null) {
            return SUCCESS;
        }

        // ✅ 防止新增 super 權限帳號
        if ("super".equalsIgnoreCase(newAdmin.getRole())) {
            addActionError("無法建立具有 super 權限的管理員！");
            return "unauthorized";
        }

        // ✅ 嘗試註冊
        boolean success = adminService.register(newAdmin);
        if (!success) {
            addActionError("該帳號已存在，請使用其他帳號！");
            return INPUT;
        }

        // ✅ 記錄 log
        adminLogService.log(
                loggedInAdmin.getAdminId(),
                "create_admin",
                "admin",
                null, // 尚未取得 newAdmin 的 ID（如需可先查找）
                "新增管理員帳號：" + newAdmin.getUsername(),
                RequestUtil.getClientIp()
        );

        addActionMessage("新增成功！");
        return SUCCESS;
    }

    // Getter / Setter
    public Admin getNewAdmin() {
        return newAdmin;
    }

    public void setNewAdmin(Admin newAdmin) {
        this.newAdmin = newAdmin;
    }
}
