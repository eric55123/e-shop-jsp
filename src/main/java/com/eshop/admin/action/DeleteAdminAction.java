package com.eshop.admin.action;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminLogService;
import com.eshop.admin.service.AdminService;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class DeleteAdminAction extends ActionSupport {

    private int adminId;
    private List<Admin> adminList;

    private AdminService adminService = new AdminService();
    private AdminLogService adminLogService = new AdminLogService(); // ✅ 新增

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

        // ✅ 執行刪除
        adminService.deleteById(adminId);

        // ✅ Log 紀錄
        if (loggedInAdmin != null) {
            adminLogService.log(
                    loggedInAdmin.getAdminId(),
                    "delete_admin",
                    "admin",
                    String.valueOf(adminId),
                    "刪除管理員帳號：" + target.getUsername(),
                    RequestUtil.getClientIp()
            );
        }

        addActionMessage("刪除成功！");
        return SUCCESS;
    }

    // Getter / Setter
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
