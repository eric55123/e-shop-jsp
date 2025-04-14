package com.eshop.admin.service;

import com.eshop.admin.dao.AdminDAO;
import com.eshop.admin.model.Admin;

import java.util.List;

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

    // 新增註冊
    public boolean register(Admin admin) {
        if (adminDAO.findByUsername(admin.getUsername()) != null) {
            return false;
        }
        admin.setStatus((byte) 1);
        adminDAO.insert(admin);
        return true;
    }

    // 修改管理員
    public void updateAdmin(Admin admin) {
        adminDAO.update(admin);
    }

    // 查詢單一管理員
    public Admin findById(int adminId) {
        return adminDAO.findById(adminId);
    }

    // 查詢所有管理員（給 adminList 用）
    public List<Admin> findAll() {
        return adminDAO.findAll();
    }

    // 刪除管理員
    public void deleteById(int adminId) {
        adminDAO.deleteById(adminId);
    }

    // 單純儲存（可選）
    public void save(Admin admin) {
        adminDAO.insert(admin);
    }
}
