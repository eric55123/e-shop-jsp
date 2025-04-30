package com.eshop.product.service;

import com.eshop.member.model.Member;
import com.eshop.product.dao.ProductCommentDAO;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductComment;

import java.time.LocalDateTime;
import java.util.List;

public class ProductCommentService {

    private ProductCommentDAO commentDAO = new ProductCommentDAO();

    // 新增評論
    public void addComment(Product product, Member member, int rating, String text) {
        ProductComment comment = new ProductComment();
        comment.setProduct(product);
        comment.setMember(member);
        comment.setRating((byte)rating);
        comment.setCommentText(text);
        comment.setCommentTime(LocalDateTime.now().withNano(0));
        comment.setStatus((byte)1); // 預設為顯示狀態
        commentDAO.insert(comment);
    }

    // 查詢某商品公開評論（status = 1）
    public List<ProductComment> getPublicCommentsByProduct(Product product, Member loginMember) {
        return commentDAO.findByProduct(product);
    }

    // 使用者自刪評論（狀態設為 0）
    public boolean deleteComment(int commentId) {
        try {
            commentDAO.updateStatus(commentId, (byte) 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 管理員封鎖不當評論（狀態設為 -1）
    public void markAsInappropriate(int commentId) {
        commentDAO.updateStatus(commentId, (byte) -1); // ✅ 明確轉型為 byte
    }

    // 查詢單一評論（編輯或驗證用）
    public ProductComment getCommentById(int commentId) {
        return commentDAO.findById(commentId);
    }

    // 編輯更新留言內容與評分
    public void updateComment(ProductComment comment) {
        comment.setCommentTime(LocalDateTime.now().withNano(0));
        commentDAO.update(comment);
    }
}
