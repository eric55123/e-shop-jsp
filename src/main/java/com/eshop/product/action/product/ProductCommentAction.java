package com.eshop.product.action.product;



import com.eshop.member.model.Member;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductComment;
import com.eshop.product.service.ProductCommentService;
import com.eshop.product.service.ProductService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ProductCommentAction extends ActionSupport {

    private int productNo;
    private int rating;
    private String commentText;
    private List<ProductComment> comments;
    private int commentId;
    private ProductComment comment;
    private ProductService productService = new ProductService();
    private ProductCommentService commentService = new ProductCommentService();

    // 顯示商品評論
    public String showComments() {
        Product product = productService.getProductById(productNo);
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (product != null) {
            comments = commentService.getPublicCommentsByProduct(product, loginMember);
            return SUCCESS;
        }
        return ERROR;
    }


    // 新增商品評論
    public String addComment() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loginMember");

        if (member != null && productNo > 0 && rating >= 1 && rating <= 5 && commentText != null && !commentText.trim().isEmpty()) {
            Product product = productService.getProductById(productNo);
            if (product != null) {
                commentService.addComment(product, member, rating, commentText);
                addActionMessage("評論新增成功！");
                return SUCCESS;
            }
        }

        addActionError("評論新增失敗，請確認內容正確。");
        return ERROR;
    }

    public String editForm() {
        comment = commentService.getCommentById(commentId);
        if (comment != null) {
            return "product_detail"; // 對應到 editComment.jsp
        }
        addActionError("找不到該評論");
        return ERROR;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public ProductComment getComment() {
        return comment;
    }

    public void setComment(ProductComment comment) {
        this.comment = comment;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ProductCommentService getCommentService() {
        return commentService;
    }

    public void setCommentService(ProductCommentService commentService) {
        this.commentService = commentService;
    }

    public String updateComment() {
        ProductComment comment = commentService.getCommentById(commentId);
        if (comment != null) {
            comment.setRating(rating);
            comment.setCommentText(commentText);
            commentService.updateComment(comment);
            return writeJson("{\"message\":\"success\"}");
        }
        return writeJson("{\"message\":\"not_found\"}");
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
        return null; // ✅ 不讓 Struts 找 JSP 頁面
    }


    public String deleteComment() {
        System.out.println("🛠 進入 ProductCommentAction.deleteComment(), commentId = " + commentId);

        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loginMember");

        ProductComment comment = commentService.getCommentById(commentId);
        if (comment != null && member != null &&
                comment.getMember().getMemberId().equals(member.getMemberId())) {

            commentService.deleteComment(commentId); // 將狀態改為 0

            // 回傳 JSON（讓前端處理畫面）
            try {
                HttpServletResponse response = ServletActionContext.getResponse();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"deleted\"}");
                return null; // 不跳頁
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    // ===== Getter / Setter =====
    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public List<ProductComment> getComments() {
        return comments;
    }

    public void setComments(List<ProductComment> comments) {
        this.comments = comments;
    }
}
