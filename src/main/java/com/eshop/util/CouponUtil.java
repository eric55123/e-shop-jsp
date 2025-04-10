package com.eshop.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CouponUtil {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 將日期字串轉換為 Timestamp
     * @param dateStr 格式必須為 yyyy-MM-dd
     * @return Timestamp 物件，若格式錯誤回傳 null
     */
    public static Timestamp parseDateString(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            return new Timestamp(sdf.parse(dateStr).getTime());
        } catch (ParseException e) {
            System.err.println("⚠ 日期轉換失敗：" + e.getMessage());
            return null;
        }
    }

    /**
     * 將 Timestamp 轉成 yyyy-MM-dd 格式字串
     */
    public static String formatTimestamp(Timestamp ts) {
        if (ts == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(ts);
    }
}
