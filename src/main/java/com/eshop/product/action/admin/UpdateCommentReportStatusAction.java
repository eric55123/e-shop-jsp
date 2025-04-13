package com.eshop.product.action.admin;

import com.eshop.admin.model.Admin;
import com.eshop.product.model.CommentReport;
import com.eshop.product.model.ProductComment;
import com.eshop.product.service.CommentReportService;
import com.eshop.product.service.ProductCommentService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public class UpdateCommentReportStatusAction extends ActionSupport {

    private int reportId;
    private int status; // 1 = 駁回, 2 = 封鎖
    private String reply; // 管理員處理意見

    private CommentReportService reportService = new CommentReportService();
    private ProductCommentService commentService = new ProductCommentService();

    @Override
    public String execute() {
        // 取得登入管理員
        HttpSession session = ServletActionContext.getRequest().getSession(false);
        Admin admin = (Admin) session.getAttribute("loggedInAdmin");
        if (admin == null) {
            return "login";
        }

        // 查找檢舉紀錄
        CommentReport report = reportService.getReportById(reportId);
        if (report == null) {
            addActionError("找不到該檢舉紀錄！");
            return ERROR;
        }

        // 更新檢舉狀態與回覆
        report.setStatus(status);
        report.setAdminId(admin.getAdminId());
        report.setHandleTime(LocalDateTime.now());
        report.setReply(reply);
        reportService.updateReport(report);

        // 如果是封鎖，將評論狀態設為 -1
        if (status == 2 && report.getComment() != null) {
            int commentId = report.getComment().getCommentId();
            ProductComment comment = commentService.getCommentById(commentId); // 安全重新查一次
            comment.setStatus(-1);
            commentService.updateComment(comment);
        }

        return SUCCESS;
    }

    // Getter / Setter
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
