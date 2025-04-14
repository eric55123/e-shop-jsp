package com.eshop.faq.action;

import com.eshop.admin.service.AdminLogService;
import com.eshop.faq.model.Faq;
import com.eshop.faq.service.FaqService;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class FaqAction extends ActionSupport {
    private FaqService faqService = new FaqService();
    private AdminLogService adminLogService = new AdminLogService(); // ✅ 加入 log 服務

    private Map<String, List<Faq>> faqMap;
    private List<Faq> faqList;
    private Faq faq;
    private int faqId;

    // ✅ 前台使用，顯示啟用中的 FAQ
    public String list() {
        List<Faq> allFaqs = faqService.getAllEnabledFaqs();
        faqMap = allFaqs.stream()
                .collect(Collectors.groupingBy(Faq::getCategory, LinkedHashMap::new, Collectors.toList()));
        return SUCCESS;
    }

    public String adminList() {
        faqList = faqService.getAllFaqs();
        return "admin";
    }

    public String edit() {
        faq = faqService.getFaqById(faqId);
        faqList = faqService.getAllFaqs();
        return "admin";
    }

    public String add() {
        faqService.addFaq(faq); // 確保 addFaq() 是用 persist，且 transaction commit 前已產生 ID

        // 寫入 admin log，這時應該可以取得 faqId
        adminLogService.log(
                RequestUtil.getLoggedInAdmin().getAdminId(),
                "add_faq",
                "faq",
                faq.getFaqId().toString(),
                "新增 FAQ：" + faq.getQuestion(),
                RequestUtil.getClientIp()
        );

        faq = null;
        faqList = faqService.getAllFaqs();
        return "admin";
    }


    public String update() {
        faqService.updateFaq(faq);
        adminLogService.log(
                RequestUtil.getLoggedInAdmin().getAdminId(),
                "update_faq",
                "faq",
                faq.getFaqId().toString(),
                "修改 FAQ：" + faq.getQuestion(),
                RequestUtil.getClientIp()
        );
        faq = null;
        faqList = faqService.getAllFaqs();
        return "admin";
    }

    public String delete() {
        Faq toDelete = faqService.getFaqById(faqId); // 🔍 先取得原始資訊以便記錄內容
        faqService.deleteFaq(faqId);
        adminLogService.log(
                RequestUtil.getLoggedInAdmin().getAdminId(),
                "delete_faq",
                "faq",
                faq.getFaqId().toString(),
                "刪除 FAQ：" + (toDelete != null ? toDelete.getQuestion() : "ID: " + faqId),
                RequestUtil.getClientIp()
        );
        faq = null;
        faqList = faqService.getAllFaqs();
        return "admin";
    }

    // ✅ Getter / Setter
    public Map<String, List<Faq>> getFaqMap() { return faqMap; }
    public List<Faq> getFaqList() { return faqList; }
    public void setFaqList(List<Faq> faqList) { this.faqList = faqList; }
    public Faq getFaq() { return faq; }
    public void setFaq(Faq faq) { this.faq = faq; }
    public int getFaqId() { return faqId; }
    public void setFaqId(int faqId) { this.faqId = faqId; }
}
