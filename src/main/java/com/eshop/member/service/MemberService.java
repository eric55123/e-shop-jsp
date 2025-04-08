package com.eshop.member.service;

import com.eshop.member.dao.MemberDAO;
import com.eshop.member.model.Member;

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


}
