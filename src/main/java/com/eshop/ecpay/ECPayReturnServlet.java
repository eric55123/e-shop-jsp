package com.eshop.ecpay;

import com.eshop.orders.model.Orders;
import com.eshop.payment.Payment;


import javax.persistence.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/ecpay/return")
public class ECPayReturnServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String rtnCode = req.getParameter("RtnCode");
        String rtnMsg = req.getParameter("RtnMsg");
        String merchantTradeNo = req.getParameter("MerchantTradeNo");
        String tradeNo = req.getParameter("TradeNo");
        String paymentType = req.getParameter("PaymentType");
        String paymentDateStr = req.getParameter("PaymentDate");
        String amountStr = req.getParameter("TradeAmt");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("eShopPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            int orderId = Integer.parseInt(merchantTradeNo.replace("ESHOP", ""));
            Orders order = em.find(Orders.class, orderId);

            if (order != null && "1".equals(rtnCode)) {
                // ✅ 更新訂單付款狀態
                order.setPaymentStatus(1);
                em.merge(order);

                // ✅ 建立付款紀錄
                Payment payment = new Payment();
                payment.setOrder(order);
                payment.setMerchantTradeNo(merchantTradeNo);
                payment.setTradeNo(tradeNo);
                payment.setPaymentType(paymentType);
                payment.setPaymentStatus(1); // 1 = 已付款

                // ✅ 將付款時間轉為 Timestamp
                if (paymentDateStr != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime paidAt = LocalDateTime.parse(paymentDateStr, formatter);
                    payment.setPaidAt(Timestamp.valueOf(paidAt));
                }

                // ✅ 實際付款金額
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

        // ✅ 綠界要求回傳格式
        resp.getWriter().write("1|OK");
    }
}
