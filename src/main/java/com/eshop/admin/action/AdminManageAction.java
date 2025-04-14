package com.eshop.admin.action;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminService;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;

public class AdminManageAction extends ActionSupport {

    private List<Admin> adminList;

    private AdminService adminService = new AdminService();

    @Override
    public String execute() {
        // ✅ 查出全部管理員資料
        adminList = adminService.findAll();
        return SUCCESS;
    }

    // ➕ 給 JSP 用來顯示管理員列表
    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }
}
