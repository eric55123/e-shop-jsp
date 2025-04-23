package com.eshop.ecpay;

import com.eshop.orders.model.Orders;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

import java.io.IOException;
import java.nio.file.Files; // ✅ 這行是關鍵
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ECPayService {

    private final AllInOne allInOne;

    public ECPayService() {
        String configPath = this.getClass().getClassLoader().getResource("payment_conf.xml").getPath();
        this.allInOne = new AllInOne(configPath);
    }

    /**
     * 產生綠界付款 HTML 表單
     */
    public String generateCheckoutForm(Orders order) {
        try {
            AioCheckOutALL obj = new AioCheckOutALL();

            // ✅ 產生符合規範的唯一交易編號（最多20字）
            String tradeNo = "ES" + order.getOrderId() + "T" + System.currentTimeMillis();
            obj.setMerchantTradeNo(tradeNo);
            System.out.println("✅ 傳送給綠界的 MerchantTradeNo: " + tradeNo);

            obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
            obj.setTotalAmount(String.valueOf(order.getTotalAmount().intValue()));
            obj.setTradeDesc("E-shop 訂單");
            obj.setItemName("商品共 " + order.getItems().size() + " 項");
            obj.setNeedExtraPaidInfo("N");

            // ✅ 自動讀 ngrok 網址
            String baseUrl = getNgrokUrl(); // 自動讀 ngrok_url.txt
            obj.setReturnURL(baseUrl + "/ecpayReturn.action");
            obj.setOrderResultURL(baseUrl + "/ecpay/result.jsp");
            obj.setClientBackURL(baseUrl + "/eshop/orderSuccess.action");

            return allInOne.aioCheckOut(obj, null);
        } catch (Exception e) {
            System.err.println("❌ 綠界結帳發生錯誤：" + e.getMessage());
            e.printStackTrace();
            return "<h3 style='color:red;'>發生錯誤：綠界付款失敗，請聯絡客服</h3>";
        }
    }

    private String getNgrokUrl() {
        try {
            Path path = Paths.get("/home/ubuntu/ngrok_url.txt");
            return new String(Files.readAllBytes(path)).trim();
        } catch (IOException e) {
            System.err.println("⚠️ 無法讀取 ngrok_url.txt，改用預設 localhost");
            return "http://localhost:8080/eshop";
        }
    }
}
