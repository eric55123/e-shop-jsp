package com.eshop.admin.service;

import com.eshop.admin.model.AdminLog;
import com.eshop.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;

public class AdminLogService {

    public void log(Integer adminId, String actionType, String targetTable, String targetId, String actionDesc, String ip) {
        System.out.println("📝 Log 寫入中：adminId=" + adminId + ", action=" + actionType + ", targetId=" + targetId);
        EntityManager em = JPAUtil.getEntityManager(); // ✅ 統一使用共用 EntityManager
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            AdminLog log = new AdminLog();
            log.setAdminId(adminId);
            log.setActionType(actionType);
            log.setTargetTable(targetTable);
            log.setTargetId(targetId);
            log.setActionDesc(actionDesc);
            log.setIpAddress(ip);
            log.setCreatedAt(LocalDateTime.now());

            em.persist(log);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
