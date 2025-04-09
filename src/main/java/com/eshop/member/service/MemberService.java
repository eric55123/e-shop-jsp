package com.eshop.member.service;

import com.eshop.member.dao.MemberDAO;
import com.eshop.member.model.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MemberService {

    private MemberDAO memberDAO = new MemberDAO();

    // 註冊會員
    public void register(Member member) {
        memberDAO.insert(member);
    }

    // 登入驗證（使用 email 與 password）
    public Member login(String email, String password) {
        Member found = memberDAO.findByEmail(email);
        if (found != null && password.equals(found.getPassword())) {
            return found;
        }
        return null;
    }

    // 查詢會員是否已存在（依據 email）
    public Member findByEmail(String email) {
        return memberDAO.findByEmail(email);
    }

    // 更新會員資料
    public void update(Member member) {
        memberDAO.update(member);
    }

    // 查詢所有會員
    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    // 依 ID 查詢單一會員
    public Member getMemberById(Integer memberId) {
        return memberDAO.findById(memberId);
    }

    // ✅ 取得本月壽星
    public List<Member> getMembersWithBirthdayInCurrentMonth() {
        List<Member> all = getAllMembers();
        List<Member> result = new ArrayList<>();
        int currentMonth = LocalDate.now().getMonthValue(); // 1 ~ 12

        for (Member m : all) {
            LocalDate birthday = m.getBirthday();
            if (birthday != null && birthday.getMonthValue() == currentMonth) {
                result.add(m);
            }
        }
        return result;
    }


    // ✅ 查詢註冊時間區間的會員（假設 Member 有 createdAt 欄位）
    public List<Member> getMembersByRegisterDateRange(LocalDateTime start, LocalDateTime end) {
        List<Member> all = getAllMembers();
        List<Member> result = new ArrayList<>();

        for (Member m : all) {
            LocalDateTime createdAt = m.getCreatedAt();
            if (createdAt != null &&
                    (createdAt.isAfter(start) || createdAt.isEqual(start)) &&
                    (createdAt.isBefore(end) || createdAt.isEqual(end))) {
                result.add(m);
            }
        }
        return result;
    }



    // ✅ 關鍵字搜尋（姓名 或 帳號，模糊比對）
    public List<Member> filterMembersByKeyword(List<Member> source, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return source;
        keyword = keyword.toLowerCase();

        List<Member> result = new ArrayList<>();
        for (Member m : source) {
            if ((m.getName() != null && m.getName().toLowerCase().contains(keyword)) ||
                    (m.getUsername() != null && m.getUsername().toLowerCase().contains(keyword))) {
                result.add(m);
            }
        }
        return result;
    }

}
