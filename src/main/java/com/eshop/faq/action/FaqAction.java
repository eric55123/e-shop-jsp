package com.eshop.faq.action;

import com.eshop.faq.model.Faq;
import com.eshop.faq.service.FaqService;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class FaqAction extends ActionSupport {
    private FaqService faqService = new FaqService();

    private Map<String, List<Faq>> faqMap;
    private List<Faq> faqList;
    private Faq faq;
    private int faqId;

    // ✅ 前台使用，顯示啟用中的 FAQ
    public String list() {
        List<Faq> allFaqs = faqService.getAllEnabledFaqs();

        // 分類分群
        faqMap = allFaqs.stream()
                .collect(Collectors.groupingBy(Faq::getCategory, LinkedHashMap::new, Collectors.toList()));

        return SUCCESS;
    }

    public Map<String, List<Faq>> getFaqMap() {
        return faqMap;
    }

    // ✅ 後台：顯示全部 FAQ + 顯示表單（新增或編輯）
    public String adminList() {
        faqList = faqService.getAllFaqs();  // 包含所有 FAQ
        return "admin";
    }

    public String edit() {
        faq = faqService.getFaqById(faqId);
        faqList = faqService.getAllFaqs();
        return "admin";
    }

    public String add() {
        faqService.addFaq(faq);
        faq = null; // ✅ 清除表單，改為新增模式
        faqList = faqService.getAllFaqs();
        return "admin";
    }

    public String update() {
        faqService.updateFaq(faq);
        faq = null;
        faqList = faqService.getAllFaqs();
        return "admin";
    }

    public String delete() {
        faqService.deleteFaq(faqId);
        faq = null;
        faqList = faqService.getAllFaqs();
        return "admin";
    }

    // ✅ Getter / Setter
    public List<Faq> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<Faq> faqList) {
        this.faqList = faqList;
    }

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
    }

    public int getFaqId() {
        return faqId;
    }

    public void setFaqId(int faqId) {
        this.faqId = faqId;
    }
}
