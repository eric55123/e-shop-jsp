package com.eshop.faq.service;

import com.eshop.faq.dao.FaqDAO;
import com.eshop.faq.model.Faq;

import java.util.List;

public class FaqService {
    private FaqDAO faqDAO = new FaqDAO();

    // 前台：僅顯示啟用中 FAQ
    public List<Faq> getAllEnabledFaqs() {
        return faqDAO.findAllEnabled();
    }

    // 後台：顯示全部 FAQ（不管有沒有啟用）
    public List<Faq> getAllFaqs() {
        return faqDAO.findAll();
    }

    public void addFaq(Faq faq) {
        faqDAO.save(faq);
    }

    public Faq getFaqById(int id) {
        return faqDAO.findById(id);
    }

    public void updateFaq(Faq faq) {
        faqDAO.update(faq);
    }

    public void deleteFaq(int id) {
        faqDAO.delete(id);
    }
}
