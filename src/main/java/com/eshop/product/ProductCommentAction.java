package com.eshop.product;



import com.eshop.member.model.Member;
import com.eshop.product.Model.Product;
import com.eshop.product.Model.ProductComment;
import com.eshop.product.ProductService.ProductCommentService;
import com.eshop.product.ProductService.ProductService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class ProductCommentAction extends ActionSupport {

    private int productNo;
    private int rating;
    private String commentText;
    private List<ProductComment> comments;

    private ProductService productService = new ProductService();
    private ProductCommentService commentService = new ProductCommentService();

    // 顯示商品評論
    public String showComments() {
        Product product = productService.getProductById(productNo);
        if (product != null) {
            comments = commentService.getPublicCommentsByProduct(product);
            return SUCCESS;
        }
        return ERROR;
    }

    // 新增商品評論
    public String addComment() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member member = (Member) session.getAttribute("loggedInMember");

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
