package com.eshop.payment;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

public class EcpayResultAction extends ActionSupport {
    @Override
    public String execute() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String rtnCode = request.getParameter("RtnCode");
        String rtnMsg = request.getParameter("RtnMsg");

        System.out.println("🔁 使用者回到站內，付款結果：RtnCode=" + rtnCode + ", RtnMsg=" + rtnMsg);

        // 你可以根據 rtnCode 顯示成功或失敗畫面（或傳值到 JSP）
        return SUCCESS;
    }
}
