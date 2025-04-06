package com.eshop.product.Service;

import com.eshop.product.DAO.CommentReportDAO;
import com.eshop.product.model.CommentReport;

import java.util.List;

public class CommentReportService {

    private CommentReportDAO reportDAO = new CommentReportDAO();

    // 新增檢舉
    public void addReport(CommentReport report) {
        reportDAO.insert(report);
    }

    // 查詢所有檢舉
    public List<CommentReport> getAllReports() {
        return reportDAO.findAll();
    }

    // 查詢未處理檢舉
    public List<CommentReport> getUnprocessedReports() {
        return reportDAO.findUnprocessed();
    }

    // 根據 ID 查詢
    public CommentReport getReportById(int reportId) {
        return reportDAO.findById(reportId);
    }

    // 更新檢舉
    public void updateReport(CommentReport report) {
        reportDAO.update(report);
    }

    public void save(CommentReport report) {
        reportDAO.insert(report);
    }


    public void insert(CommentReport report) {
        reportDAO.insert(report);
    }

    public List<Integer> getReportedCommentIdsByMember(int memberId) {
        return reportDAO.findReportedCommentIdsByMember(memberId);
    }
}
