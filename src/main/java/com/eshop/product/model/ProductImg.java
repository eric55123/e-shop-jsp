package com.eshop.product.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_img")
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_no")
    private Integer productImgNo;

    @Column(name = "product_no")
    private Integer productNo;  // 外來鍵，可改為 @ManyToOne 關聯

    @Column(name = "product_img_url", length = 255)
    private String productImgUrl;

    @Column(name = "img_order")
    private Integer imgOrder;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // ===== Getter / Setter =====

    public Integer getProductImgNo() {
        return productImgNo;
    }

    public void setProductImgNo(Integer productImgNo) {
        this.productImgNo = productImgNo;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public Integer getImgOrder() {
        return imgOrder;
    }

    public void setImgOrder(Integer imgOrder) {
        this.imgOrder = imgOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
