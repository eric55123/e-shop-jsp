package com.eshop.util;

import com.eshop.admin.model.Admin;
import com.eshop.member.model.Member;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestUtil {

    // ✅ 取得目前 HttpServletRequest
    public static HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    // ✅ 取得目前 Session（不自動建立）
    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        return (request != null) ? request.getSession(false) : null;
    }

    // ✅ 取得目前登入的管理員
    public static Admin getLoggedInAdmin() {
        HttpSession session = getSession();
        return (session != null) ? (Admin) session.getAttribute("loggedInAdmin") : null;
    }

    // ✅ 取得目前登入的會員
    public static Member getLoggedInMember() {
        HttpSession session = getSession();
        return (session != null) ? (Member) session.getAttribute("loginMember") : null;
    }

    // ✅ 取得來源 IP（支援 Proxy & IPv6 本機）
    public static String getClientIp() {
        HttpServletRequest request = getRequest();
        if (request == null) return "unknown";

        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }

        ip = request.getRemoteAddr();
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    // ✅ 回傳目前時間字串（格式 yyyy-MM-dd HH:mm:ss）
    public static String getFormattedNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
