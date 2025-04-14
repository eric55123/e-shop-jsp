package com.eshop.product.action.admin;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminLogService;
import com.eshop.product.model.CommentReport;
import com.eshop.product.model.ProductComment;
import com.eshop.product.service.CommentReportService;
import com.eshop.product.service.ProductCommentService;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;

import java.time.LocalDateTime;

public class UpdateCommentReportStatusAction extends ActionSupport {

    private int reportId;
    private int status; // 1 = 駁回, 2 = 封鎖
    private String reply; // 管理員處理意見

    private CommentReportService reportService = new CommentReportService();
    private ProductCommentService commentService = new ProductCommentService();
    private AdminLogService adminLogService = new AdminLogService(); // ✅ 新增 log 服務

    @Override
    public String execute() {
        Admin admin = RequestUtil.getLoggedInAdmin();

        if (admin == null) {
            return "login";
        }

        CommentReport report = reportService.getReportById(reportId);
        if (report == null) {
            addActionError("找不到該檢舉紀錄！");
            return ERROR;
        }

        // ✅ 更新檢舉狀態
        report.setStatus(status);
        report.setAdminId(admin.getAdminId());
        report.setHandleTime(LocalDateTime.now());
        report.setReply(reply);
        reportService.updateReport(report);

        // ✅ 若封鎖則更新留言狀態
        if (status == 2 && report.getComment() != null) {
            int commentId = report.getComment().getCommentId();
            ProductComment comment = commentService.getCommentById(commentId);
            comment.setStatus(-1);
            commentService.updateComment(comment);
        }

        // ✅ 寫入 admin log
        String actionType = "review_comment";
        String targetTable = "product_comment";
        Integer targetId = (report.getComment() != null) ? report.getComment().getCommentId() : null;
        String desc = (status == 1)
                ? "駁回檢舉（ID: " + reportId + "）"
                : "封鎖評論為不當言論（ID: " + reportId + "）";
        String ip = RequestUtil.getClientIp();


        adminLogService.log(
                admin.getAdminId(),
                actionType,
                targetTable,
                targetId != null ? String.valueOf(targetId) : null,
                desc,
                ip
        );

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
