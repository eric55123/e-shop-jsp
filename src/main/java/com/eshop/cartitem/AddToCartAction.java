package com.eshop.cartitem;

import com.eshop.cartitem.model.CartItem;
import com.eshop.product.model.Product;
import com.eshop.product.service.ProductService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class AddToCartAction extends ActionSupport {
    private int productNo;
    private int quantity = 1;

    private ProductService productService = new ProductService(); // 你的 Service

    public String execute() throws Exception {
        HttpSession session = ServletActionContext.getRequest().getSession();
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
        }

        Product product = productService.findById(productNo);
        if (product == null) {
            addActionError("查無此商品");
            return ERROR;
        }

        if (cart.containsKey(productNo)) {
            CartItem item = cart.get(productNo);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            cart.put(productNo, new CartItem(product, quantity));
        }

        session.setAttribute("cart", cart);

        return SUCCESS;
    }


    // getter/setter
    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
