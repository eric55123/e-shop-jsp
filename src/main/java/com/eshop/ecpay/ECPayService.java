package com.eshop.ecpay;

import com.eshop.orders.model.Orders;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

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
        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo("ESHOP" + order.getOrderId());
        obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        obj.setTotalAmount(String.valueOf(order.getTotalAmount().intValue()));
        obj.setTradeDesc("E-shop 訂單");
        obj.setItemName("商品共 " + order.getItems().size() + " 項");

        // ✅ 記得改為 ngrok 或正式部署網址
        obj.setReturnURL("https://5987-36-227-210-125.ngrok-free.app/ecpayReturn.action");
        obj.setOrderResultURL("https://5987-36-227-210-125.ngrok-free.app/ecpay/result.jsp");


        obj.setNeedExtraPaidInfo("N");

        return allInOne.aioCheckOut(obj, null);
    }
}
