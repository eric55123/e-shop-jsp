package com.eshop.member.action;

import com.eshop.member.model.LoginLog;
import com.eshop.member.model.Member;
import com.eshop.member.service.LoginLogService;
import com.eshop.member.service.MemberService;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public class MemberAction extends ActionSupport {

    private Member member;
    private String email;
    private String password;

    private MemberService memberService = new MemberService();

    // 顯示註冊頁面
    public String showRegisterForm() {
        return SUCCESS;
    }

    // 註冊
    public String register() {
        if (member != null && member.getEmail() != null) {
            boolean exists = memberService.findByEmail(member.getEmail()) != null;
            if (exists) {
                addActionError("此 Email 已被註冊！");
                return INPUT;
            }

            // ✅ Local 註冊初始化欄位
            member.setStatus((byte)1); // 啟用中
            member.setCreatedAt(LocalDateTime.now());
            member.setLoginType("local");

            // Google 註冊才需要 name，local 可設為 null（避免亂塞）
            member.setName(null);

            memberService.register(member);
            addActionMessage("註冊成功！即將跳轉到登入頁...");
            return SUCCESS;
        }
        return INPUT;
    }


    // 登入
    public String login() {
        Member found = memberService.login(email, password);
        if (found != null) {
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("loginMember", found);
            addActionMessage("登入成功！");

            // 正確紀錄登入
            LoginLog log = new LoginLog();
            log.setMemberId(found.getMemberId());
            log.setLoginTime(LocalDateTime.now());
            log.setLoginType("local"); // 或 google 等，這裡用 member.getLoginType() 也可以
            log.setStatus((byte)1); // 成功
            log.setIpAddress(RequestUtil.getClientIp());
            log.setUserAgent(RequestUtil.getRequest().getHeader("User-Agent"));
            new LoginLogService().save(log);

            return SUCCESS;
        } else {
            addActionError("帳號或密碼錯誤！");
            return INPUT;
        }
    }

    public String update() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member sessionMember = (Member) session.getAttribute("loginMember");
        if (sessionMember == null) return LOGIN;

        // 更新欄位
        sessionMember.setName(member.getName());
        sessionMember.setUsername(member.getUsername());
        sessionMember.setPhone(member.getPhone());
        sessionMember.setBirthday(member.getBirthday());

        memberService.update(sessionMember);
        addActionMessage("會員資料已更新！");
        return SUCCESS;
    }


    // 登出
    public String logout() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.invalidate();
        addActionMessage("您已登出");
        return SUCCESS;
    }

    // 顯示會員資訊
    public String profile() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        member = (Member) session.getAttribute("loginMember");
        return (member != null) ? SUCCESS : LOGIN;
    }

    // ===== Getter / Setter =====
    @TypeConversion(key = "member.birthday", converter = "com.eshop.util.LocalDateConverter")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
