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

            // ✅ 安全解析 orderId：從 ESHOP20Txxx 拆出 20
            Integer orderId = null;
            if (merchantTradeNo != null && merchantTradeNo.startsWith("ES")) {
                try {
                    String idPart = merchantTradeNo.replace("ES", "").split("T")[0]; // 拿出 23
                    orderId = Integer.parseInt(idPart);
                } catch (Exception e) {
                    System.err.println("❌ 無法解析訂單 ID，MerchantTradeNo: " + merchantTradeNo);
                }
            }


            Orders order = (orderId != null) ? em.find(Orders.class, orderId) : null;
            System.out.println("🔍 解析後 orderId: " + orderId);
            System.out.println("🔍 找到的訂單: " + order);

            if (order != null && "1".equals(rtnCode)) {
                // ✅ 更新訂單狀態
                order.setPaymentStatus((byte) 1);
                em.merge(order);

                // ✅ 建立付款紀錄
                Payment payment = new Payment();
                payment.setOrder(order);
                payment.setMerchantTradeNo(merchantTradeNo);
                payment.setTradeNo(tradeNo);
                payment.setPaymentType(paymentType);
                payment.setPaymentStatus((byte) 1);

                // 付款時間處理
                if (paymentDateStr != null) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime paidAt = LocalDateTime.parse(paymentDateStr, formatter);
                        payment.setPaidAt(Timestamp.valueOf(paidAt));
                    } catch (Exception e) {
                        System.err.println("❌ 付款時間格式錯誤: " + paymentDateStr);
                    }
                }

                // 金額處理
                if (amountStr != null) {
                    try {
                        payment.setAmount(new BigDecimal(amountStr));
                    } catch (NumberFormatException e) {
                        System.err.println("❌ 金額格式錯誤: " + amountStr);
                    }
                }

                payment.setReturnCode(rtnCode);
                payment.setReturnMsg(rtnMsg);

                em.persist(payment);
            } else {
                System.err.println("❗ 未找到訂單或 rtnCode ≠ 1，訂單未更新！");
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
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.print("1|OK");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return NONE;
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
