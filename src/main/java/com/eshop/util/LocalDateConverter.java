package com.eshop.util;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LocalDateConverter extends DefaultTypeConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Object convertValue(Map<String, Object> context, Object value, Class toType) {
        try {
            if (toType == LocalDate.class) {
                if (value instanceof String[]) {
                    String[] values = (String[]) value;
                    if (values.length > 0 && !values[0].isEmpty()) {
                        return LocalDate.parse(values[0], FORMATTER);
                    }
                }
            } else if (toType == String.class && value instanceof LocalDate) {
                return ((LocalDate) value).format(FORMATTER);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 方便除錯
        }
        return null;
    }
}
