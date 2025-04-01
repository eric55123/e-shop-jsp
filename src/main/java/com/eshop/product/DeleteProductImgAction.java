package com.eshop.product;

import com.eshop.product.ProductService.ProductImgService;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteProductImgAction extends ActionSupport {

    private int imgNo;
    private int productNo; // 刪完回到 editProduct 畫面用

    private ProductImgService imgService = new ProductImgService();

    @Override
    public String execute() {
        try {
            imgService.deleteImg(imgNo);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("圖片刪除失敗: " + e.getMessage());
            return ERROR;
        }
    }

    // ===== Getter / Setter =====
    public void setImgNo(int imgNo) {
        this.imgNo = imgNo;
    }

    public int getImgNo() {
        return imgNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public int getProductNo() {
        return productNo;
    }
}
