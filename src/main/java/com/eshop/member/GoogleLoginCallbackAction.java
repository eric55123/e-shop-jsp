package com.eshop.member;

import com.eshop.member.model.Member;
import com.eshop.member.service.MemberService;
import com.eshop.util.PropertiesUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

public class GoogleLoginCallbackAction extends ActionSupport {
    private String code; // Google 回傳的授權碼

    // ✅ 從設定檔讀取金鑰與 URI
    private final String clientId = PropertiesUtil.get("google.clientId");
    private final String clientSecret = PropertiesUtil.get("google.clientSecret");
    private final String redirectUri = PropertiesUtil.get("google.redirectUri");

    @Override
    public String execute() throws Exception {
        if (code == null || code.isEmpty()) return ERROR;

        // 1. 換 access_token
        URL tokenUrl = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) tokenUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = "code=" + URLEncoder.encode(code, "UTF-8") +
                "&client_id=" + URLEncoder.encode(clientId, "UTF-8") +
                "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                "&grant_type=authorization_code";

        conn.getOutputStream().write(params.getBytes(StandardCharsets.UTF_8));

        if (conn.getResponseCode() != 200) {
            System.out.println("❌ 換 token 失敗，回傳：" + conn.getResponseCode());
            return ERROR;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.lines().reduce("", String::concat);
        JSONObject tokenJson = new JSONObject(response);
        String accessToken = tokenJson.getString("access_token");

        // 2. 拿 user 資料
        URL userinfoUrl = new URL("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken);
        HttpURLConnection userConn = (HttpURLConnection) userinfoUrl.openConnection();
        BufferedReader userReader = new BufferedReader(new InputStreamReader(userConn.getInputStream()));
        String userInfoJson = userReader.lines().reduce("", String::concat);
        JSONObject userJson = new JSONObject(userInfoJson);

        String email = userJson.getString("email");
        String name = userJson.getString("name");
        String sub = userJson.getString("sub");

        // 3. 建立或查詢會員
        MemberService memberService = new MemberService();
        Member member = memberService.findByEmailAndGoogleSub(email, sub);

        if (member == null) {
            member = new Member();
            member.setEmail(email);
            member.setName(name);
            member.setGoogleSub(sub);
            member.setLoginType("google");
            member.setCreatedAt(LocalDateTime.now());
            member.setStatus(1);
            memberService.save(member);
        }

        Map<String, Object> session = ActionContext.getContext().getSession();
        session.put("loginMember", member);

        return SUCCESS;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
