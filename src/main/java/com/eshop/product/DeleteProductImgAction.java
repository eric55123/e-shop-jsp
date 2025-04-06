package com.eshop.product;

import com.eshop.product.Service.ProductImgService;
import com.eshop.product.model.ProductImg;
import com.opensymphony.xwork2.ActionSupport;

import java.io.File;

public class DeleteProductImgAction extends ActionSupport {
    private int imgNo;
    private int productNo; // 刪除後可以導回編輯該商品

    private ProductImgService imgService = new ProductImgService();

    @Override
    public String execute() {
        try {
            ProductImg img = imgService.getImgById(imgNo);
            if (img != null) {
                String imgUrl = img.getProductImgUrl();
                String uploadBasePath = "/opt/tomcat/webapps/ROOT/uploads/";
                String fileNameOnly = new File(imgUrl).getName();
                String fullPath = uploadBasePath + fileNameOnly;

                File file = new File(fullPath);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("✅ 圖片檔案已刪除：" + fullPath);
                    } else {
                        System.out.println("❌ 無法刪除圖片檔案：" + fullPath);
                    }
                } else {
                    System.out.println("⚠️ 找不到圖片檔案：" + fullPath);
                }

                imgService.deleteImg(imgNo);
                System.out.println("✅ 單張圖片刪除完成 imgNo = " + imgNo);
            } else {
                System.out.println("⚠️ 找不到圖片資料 imgNo = " + imgNo);
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("刪除圖片失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // Getter/Setter
    public int getImgNo() { return imgNo; }
    public void setImgNo(int imgNo) { this.imgNo = imgNo; }

    public int getProductNo() { return productNo; }
    public void setProductNo(int productNo) { this.productNo = productNo; }
}
