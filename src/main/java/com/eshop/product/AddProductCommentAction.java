package com.eshop.product;

import com.eshop.product.model.Product;
import com.eshop.product.model.ProductComment;
import com.eshop.product.Service.ProductCommentService;
import com.eshop.product.Service.ProductService;
import com.eshop.member.Model.Member;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;

public class AddProductCommentAction extends ActionSupport {

    private int productNo;
    private int rating;
    private String commentText;

    private ProductService productService = new ProductService();
    private ProductCommentService commentService = new ProductCommentService();

    private Product product;
    private List<ProductComment> comments;

    @Override
    public String execute() {
        try {
            Member loginMember = (Member) com.opensymphony.xwork2.ActionContext.getContext().getSession().get("loginMember");
            if (loginMember == null) {
                addActionError("請先登入再評論");
                return "loginRequired";
            }

            product = productService.getProductById(productNo);
            if (product == null) {
                addActionError("查無此商品");
                return ERROR;
            }

            commentService.addComment(product, loginMember, rating, commentText);
            comments = commentService.getPublicCommentsByProduct(product);
            addActionMessage("評論成功");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("新增評論失敗: " + e.getMessage());
            return ERROR;
        }
    }

    // Getter / Setter
    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getCommentText() { return commentText; }
    public void setCommentText(String commentText) { this.commentText = commentText; }

    public Product getProduct() { return product; }
    public List<ProductComment> getComments() { return comments; }
}
