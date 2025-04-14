package com.eshop.admin.service;

import com.eshop.admin.dao.AdminDAO;
import com.eshop.admin.model.Admin;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class AdminService {

    private AdminDAO adminDAO = new AdminDAO();

    // ✅ 登入驗證（使用加密驗證）
    public Admin login(String username, String password) {
        Admin admin = adminDAO.findByUsername(username);
        if (admin != null) {
            // ✅ 使用 BCrypt 驗證加密密碼
            if (BCrypt.checkpw(password, admin.getPassword())) {
                if (admin.getStatus() != null && admin.getStatus() == 1) {
                    adminDAO.updateLastLogin(admin.getAdminId());
                    return admin;
                }
            }
        }
        return null;
    }

    // ✅ 註冊/新增管理員（密碼加密）
    public boolean register(Admin admin) {
        if (adminDAO.findByUsername(admin.getUsername()) != null) {
            return false; // 帳號已存在
        }

        // ✅ 密碼加密處理
        String hashedPassword = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
        admin.setPassword(hashedPassword);

        admin.setStatus((byte) 1); // 預設啟用
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
