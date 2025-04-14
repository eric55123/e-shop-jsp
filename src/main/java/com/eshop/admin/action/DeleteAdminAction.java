package com.eshop.admin.action;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class DeleteAdminAction extends ActionSupport {

    private int adminId;
    private List<Admin> adminList;

    private AdminService adminService = new AdminService();

    @Override
    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");

        Admin target = adminService.findById(adminId);
        adminList = adminService.findAll(); // ✅ 錯誤時提前載入列表

        if (target == null) {
            addActionError("找不到該管理員！");
            return ERROR;
        }

        if ("super".equalsIgnoreCase(target.getRole())) {
            addActionError("無法刪除擁有 super 權限的管理員！");
            return ERROR;
        }

        if (loggedInAdmin != null && loggedInAdmin.getAdminId() == adminId) {
            addActionError("無法刪除自己的帳號！");
            return ERROR;
        }

        adminService.deleteById(adminId);
        addActionMessage("刪除成功！");
        return SUCCESS;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }
}
