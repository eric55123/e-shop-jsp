package com.eshop.product.Service;

import com.eshop.product.DAO.ProductCommentDAO;
import com.eshop.member.Model.Member;
import com.eshop.product.Model.Product;
import com.eshop.product.Model.ProductComment;

import java.time.LocalDateTime;
import java.util.List;

public class ProductCommentService {

    private ProductCommentDAO commentDAO = new ProductCommentDAO();

    // 新增評論
    public void addComment(Product product, Member member, int rating, String text) {
        ProductComment comment = new ProductComment();
        comment.setProduct(product);
        comment.setMember(member);
        comment.setRating(rating);
        comment.setCommentText(text);
        comment.setCommentTime(LocalDateTime.now());
        comment.setStatus(1); // 預設為顯示狀態
        commentDAO.insert(comment);
    }

    // 查詢某商品公開評論
    public List<ProductComment> getPublicCommentsByProduct(Product product) {
        return commentDAO.findByProduct(product);
    }

    // 使用者自刪評論（狀態設為 0）
    public void softDeleteByUser(int commentId) {
        commentDAO.updateStatus(commentId, 0);
    }

    // 管理員封鎖不當言論（狀態設為 -1）
    public void markAsInappropriate(int commentId) {
        commentDAO.updateStatus(commentId, -1);
    }

    // 查詢單一評論（用於編輯或確認）
    public ProductComment getCommentById(int commentId) {
        return commentDAO.findById(commentId);
    }
}
