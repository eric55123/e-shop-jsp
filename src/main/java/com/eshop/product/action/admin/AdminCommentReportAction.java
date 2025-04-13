package com.eshop.product.action.admin;

import com.eshop.admin.model.Admin;
import com.eshop.product.model.CommentReport;
import com.eshop.product.service.CommentReportService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class AdminCommentReportAction extends ActionSupport {

    private List<CommentReport> pendingReports;
    private CommentReportService reportService = new CommentReportService();

    @Override
    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession(false);
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");

        if (loggedInAdmin == null) {
            return "login";
        }

        pendingReports = reportService.findPendingReports();

        // ✅ 預先載入 LAZY 關聯資料
        for (CommentReport report : pendingReports) {
            if (report.getComment() != null) {
                report.getComment().getCommentText(); // 觸發 LAZY 載入
            }
            if (report.getReporter() != null) {
                report.getReporter().getName(); // 觸發 LAZY 載入
            }
        }

        return SUCCESS;
    }


    public List<CommentReport> getPendingReports() {
        return pendingReports;
    }
}
