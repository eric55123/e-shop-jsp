package com.eshop.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            // 會讀取 src/main/resources/META-INF/persistence.xml
            return Persistence.createEntityManagerFactory("eShopPU"); // 這個名稱要和 persistence.xml 中的一致
        } catch (Throwable ex) {
            System.err.println("EntityManagerFactory 建立失敗：" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
