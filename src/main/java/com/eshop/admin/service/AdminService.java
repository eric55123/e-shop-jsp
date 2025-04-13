package com.eshop.admin.service;

import com.eshop.admin.dao.AdminDAO;
import com.eshop.admin.model.Admin;

public class AdminService {

    private AdminDAO adminDAO = new AdminDAO();

    // 登入驗證
    public Admin login(String username, String password) {
        Admin admin = adminDAO.findByUsername(username);
        if (admin != null) {
            if (admin.getPassword().equals(password)) { // ❗目前未加密
                if (admin.getStatus() != null && admin.getStatus() == 1) {
                    // 更新登入時間
                    adminDAO.updateLastLogin(admin.getAdminId());
                    return admin;
                }
            }
        }
        return null;
    }

    // 註冊/新增管理員（可擴充）
    public boolean register(Admin admin) {
        if (adminDAO.findByUsername(admin.getUsername()) != null) {
            return false; // 帳號已存在
        }
        admin.setStatus(1); // 預設啟用
        adminDAO.insert(admin);
        return true;
    }

    // 修改個人資料
    public void updateAdmin(Admin admin) {
        adminDAO.update(admin);
    }

    // 查詢管理員（未來可用於列表）
    public Admin findById(int adminId) {
        return adminDAO.findById(adminId);
    }
}
