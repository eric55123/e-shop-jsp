package com.eshop.product;

import com.eshop.member.Model.Member;
import com.eshop.product.Model.CommentReport;
import com.eshop.product.Model.ProductComment;
import com.eshop.product.Service.CommentReportService;
import com.eshop.product.Service.ProductCommentService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public class CommentReportAction extends ActionSupport {

    private int commentId;
    private String reason;
    private int productNo;
    private CommentReportService reportService = new CommentReportService();
    private ProductCommentService commentService = new ProductCommentService();

    @Override
    public String execute() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loginMember");

        if (member == null) {
            addActionError("請先登入才能檢舉評論");
            return LOGIN;
        }

        ProductComment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            addActionError("找不到該評論");
            return ERROR;
        }

        CommentReport report = new CommentReport();
        report.setComment(comment);
        report.setReporter(member);
        report.setReason(reason);
        report.setReportTime(LocalDateTime.now());
        report.setCommentTime(comment.getCommentTime());
        report.setStatus(0); // 預設未處理

        reportService.insert(report);
        addActionMessage("檢舉成功，我們將儘快處理！");

        return SUCCESS;
    }

    // === Getter / Setter ===
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }
}
