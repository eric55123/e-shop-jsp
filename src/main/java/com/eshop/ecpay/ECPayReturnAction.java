package com.eshop.ecpay;

import com.eshop.orders.model.Orders;
import com.eshop.payment.Payment;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ECPayReturnAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @Override
    public String execute() {
        System.out.println("✅ 綠界回傳成功進入 Action");

        // 🧾 印出全部回傳參數（debug 用）
        request.getParameterMap().forEach((k, v) ->
                System.out.println("🟡 " + k + " => " + java.util.Arrays.toString(v))
        );

        String rtnCode = request.getParameter("RtnCode");
        String rtnMsg = request.getParameter("RtnMsg");
        String merchantTradeNo = request.getParameter("MerchantTradeNo");
        String tradeNo = request.getParameter("TradeNo");
        String paymentType = request.getParameter("PaymentType");
        String paymentDateStr = request.getParameter("PaymentDate");
        String amountStr = request.getParameter("TradeAmt");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            int orderId = Integer.parseInt(merchantTradeNo.replace("ESHOP", ""));
            Orders order = em.find(Orders.class, orderId);
            System.out.println("🔍 解析後 orderId: " + orderId);
            System.out.println("🔍 找到的訂單: " + order);

            if (order != null && "1".equals(rtnCode)) {
                order.setPaymentStatus(1);
                em.merge(order);

                Payment payment = new Payment();
                payment.setOrder(order);
                payment.setMerchantTradeNo(merchantTradeNo);
                payment.setTradeNo(tradeNo);
                payment.setPaymentType(paymentType);
                payment.setPaymentStatus(1);

                if (paymentDateStr != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime paidAt = LocalDateTime.parse(paymentDateStr, formatter);
                    payment.setPaidAt(Timestamp.valueOf(paidAt));
                }

                if (amountStr != null) {
                    payment.setAmount(new BigDecimal(amountStr));
                }

                payment.setReturnCode(rtnCode);
                payment.setReturnMsg(rtnMsg);

                em.persist(payment);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

        // ✅ 回傳給綠界固定格式
        try {
            PrintWriter out = response.getWriter();
            out.print("1|OK");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return NONE; // 不跳轉頁面
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
}
