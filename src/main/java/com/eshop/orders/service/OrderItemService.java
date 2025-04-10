package com.eshop.orders.service;

import com.eshop.cartitem.model.CartItem;
import com.eshop.orders.model.OrdersItem;
import com.eshop.orders.model.Orders;
import com.eshop.product.model.Product;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Map;

public class OrderItemService {

    public void createOrderItems(EntityManager em, Orders order, Map<Integer, CartItem> cart) {
        for (CartItem item : cart.values()) {
            Product product = item.getProduct();
            Integer qty = item.getQuantity();
            BigDecimal price = product.getProductPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(qty));

            OrdersItem orderItem = new OrdersItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(qty);
            orderItem.setUnitPrice(price);
            orderItem.setSubtotal(subtotal);

            order.getItems().add(orderItem);

            // ✅ 減少庫存
            product.setRemainingQty(product.getRemainingQty() - qty);
            em.merge(product);
        }
    }
}
