package com.eshop.admin.action;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class AdminCreateAction extends ActionSupport {

    private Admin newAdmin;
    private List<Admin> adminList;

    private AdminService adminService = new AdminService();

    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");

        // ✅ 權限檢查
        if (loggedInAdmin == null || !"super".equalsIgnoreCase(loggedInAdmin.getRole())) {
            addActionError("您沒有新增管理員的權限！");
            return "unauthorized";
        }

        // ✅ 預設撈出所有管理員供 JSP 顯示
        adminList = adminService.findAll();

        // ✅ 第一次進入頁面（不是提交）
        if (newAdmin == null) {
            return SUCCESS;
        }

        // ✅ 防止新增 super 權限
        if ("super".equalsIgnoreCase(newAdmin.getRole())) {
            addActionError("無法建立具有 super 權限的管理員！");
            return "unauthorized";
        }

        // ✅ 儲存資料
        boolean success = adminService.register(newAdmin);
        if (!success) {
            addActionError("該帳號已存在，請使用其他帳號！");
            return INPUT;
        }

        addActionMessage("新增成功！");
        adminList = adminService.findAll(); // 重新撈一次顯示最新資料
        return SUCCESS;
    }

    // Getter / Setter
    public Admin getNewAdmin() {
        return newAdmin;
    }

    public void setNewAdmin(Admin newAdmin) {
        this.newAdmin = newAdmin;
    }

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }
}
