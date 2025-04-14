package com.eshop.admin.action;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class UpdateAdminAction extends ActionSupport {

    private Admin admin;
    private List<Admin> adminList; // 提供給 JSP
    private AdminService adminService = new AdminService();

    @Override
    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");

        // 查詢原始資料
        Admin original = adminService.findById(admin.getAdminId());

        if (original == null) {
            reloadAdminList();
            addActionError("找不到此管理員！");
            return ERROR;
        }

        // ✅ 保留密碼，避免 Hibernate 更新失敗
        admin.setPassword(original.getPassword());

        // ✅ 防止將 super 管理員降級
        if ("super".equalsIgnoreCase(original.getRole()) && !"super".equalsIgnoreCase(admin.getRole())) {
            reloadAdminList();
            addActionError("無法修改 super 權限者的角色！");
            return ERROR;
        }

        // ✅ 防止自己把自己停用
        if (loggedInAdmin != null &&
                loggedInAdmin.getAdminId() == admin.getAdminId() &&
                admin.getStatus() != null && admin.getStatus() == 0) {
            reloadAdminList();
            addActionError("無法將自己設為停用狀態！");
            return ERROR;
        }

        // ✅ 執行更新
        adminService.updateAdmin(admin);
        addActionMessage("修改成功！");
        return SUCCESS;
    }

    // 🔁 當錯誤發生時重新撈列表，避免 JSP 下方列表為空
    private void reloadAdminList() {
        adminList = adminService.findAll();
    }

    // Getter / Setter
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }
}
