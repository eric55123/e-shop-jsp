package com.eshop.admin.service;

import com.eshop.admin.model.AdminLog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class AdminLogService {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    public void log(Integer adminId, String actionType, String targetTable, String targetId, String actionDesc, String ip) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            AdminLog log = new AdminLog();
            log.setAdminId(adminId);
            log.setActionType(actionType);
            log.setTargetTable(targetTable);
            log.setTargetId(targetId);  // 改為 String
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
