package com.eshop.product.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_no")
    private Integer productNo;

    @Column(name = "product_name", length = 32, nullable = false, unique = true)
    private String productName;

    @Column(name = "product_desc", columnDefinition = "TEXT")
    private String productDesc;

    @Column(name = "product_add_qty")
    private Integer productAddQty;

    @Column(name = "remaining_qty")
    private Integer remainingQty;

    @Column(name = "product_add_time")
    private LocalDateTime productAddTime;

    @Column(name = "product_remove_time")
    private LocalDateTime productRemoveTime;

    @Column(name = "product_status")
    private Byte productStatus;

    @Column(name = "product_price", precision = 10, scale = 2)
    private BigDecimal productPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImg> productImgs;

    // ===== Getter / Setter =====

    public List<ProductImg> getProductImgs() {
        return productImgs;
    }

    public void setProductImgs(List<ProductImg> productImgs) {
        this.productImgs = productImgs;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Integer getProductAddQty() {
        return productAddQty;
    }

    public void setProductAddQty(Integer productAddQty) {
        this.productAddQty = productAddQty;
    }

    public Integer getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(Integer remainingQty) {
        this.remainingQty = remainingQty;
    }

    public LocalDateTime getProductAddTime() {
        return productAddTime;
    }

    public void setProductAddTime(LocalDateTime productAddTime) {
        this.productAddTime = productAddTime;
    }

    public LocalDateTime getProductRemoveTime() {
        return productRemoveTime;
    }

    public void setProductRemoveTime(LocalDateTime productRemoveTime) {
        this.productRemoveTime = productRemoveTime;
    }

    public Byte getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Byte productStatus) {
        this.productStatus = productStatus;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
}
