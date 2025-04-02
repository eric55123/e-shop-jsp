package com.eshop.product.Model;

import com.eshop.member.Model.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment_report")
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private ProductComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member reporter;

    @Column(name = "reason", length = 100)
    private String reason;

    @Column(name = "report_time")
    private LocalDateTime reportTime = LocalDateTime.now();

    @Column(name = "comment_time")
    private LocalDateTime commentTime;

    @Column(name = "status")
    private Integer status = 0; // 0=未處理, 1=刪除留言, 2=駁回

    @Column(name = "admin_id")
    private Integer adminId; // 可為 null

    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    @Column(name = "reply", length = 255)
    private String reply;
    // ===== Getter / Setter =====

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public ProductComment getComment() {
        return comment;
    }

    public void setComment(ProductComment comment) {
        this.comment = comment;
    }

    public Member getReporter() {
        return reporter;
    }

    public void setReporter(Member reporter) {
        this.reporter = reporter;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public LocalDateTime getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(LocalDateTime commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }


}
