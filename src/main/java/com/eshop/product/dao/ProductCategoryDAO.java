package com.eshop.product.dao;

import com.eshop.product.model.ProductCategory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ProductCategoryDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");

    public List<ProductCategory> findAll() {
        EntityManager em = emf.createEntityManager();
        List<ProductCategory> result = em.createQuery("FROM ProductCategory", ProductCategory.class).getResultList();
        em.close();
        return result;
    }
}
