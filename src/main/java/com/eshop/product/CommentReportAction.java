package com.eshop.product;

import com.eshop.member.model.Member;
import com.eshop.product.model.CommentReport;
import com.eshop.product.model.ProductComment;
import com.eshop.product.service.CommentReportService;
import com.eshop.product.service.ProductCommentService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class CommentReportAction extends ActionSupport {

    private int commentId;
    private String reason;
    private int productNo;

    private CommentReportService reportService = new CommentReportService();
    private ProductCommentService commentService = new ProductCommentService();

    @Override
    public String execute() {
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            Member member = (Member) session.getAttribute("loginMember");

            if (member == null) {
                return writeJson("{\"message\":\"請先登入\"}");
            }

            ProductComment comment = commentService.getCommentById(commentId);
            if (comment == null) {
                return writeJson("{\"message\":\"找不到該評論\"}");
            }

            CommentReport report = new CommentReport();
            report.setComment(comment);
            report.setReporter(member);
            report.setReason(reason);
            report.setReportTime(LocalDateTime.now());
            report.setCommentTime(comment.getCommentTime());
            report.setStatus(0); // 預設未處理

            reportService.insert(report);

            return writeJson("{\"message\":\"檢舉成功\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return writeJson("{\"message\":\"伺服器錯誤，請稍後再試\"}");
        }
    }

    private String writeJson(String json) {
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // 不跳轉
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
