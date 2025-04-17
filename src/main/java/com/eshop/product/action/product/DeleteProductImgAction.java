package com.eshop.product.action.product;

import com.eshop.product.service.ProductImgService;
import com.eshop.product.model.ProductImg;
import com.eshop.util.GoogleDriveUploader;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteProductImgAction extends ActionSupport {
    private int imgNo;
    private int productNo;

    private ProductImgService imgService = new ProductImgService();

    @Override
    public String execute() {
        try {
            ProductImg img = imgService.getImgById(imgNo);
            if (img != null) {
                String imgUrl = img.getProductImgUrl();

                // ✅ 從網址中取得 Google Drive file ID
                if (imgUrl != null && imgUrl.contains("id=")) {
                    String fileId = imgUrl.substring(imgUrl.indexOf("id=") + 3);

                    try {
                        GoogleDriveUploader.deleteFileById(fileId);
                        System.out.println("✅ 已刪除 Google Drive 圖片：" + fileId);
                    } catch (Exception e) {
                        System.err.println("❌ 刪除 Google Drive 圖片失敗：" + fileId);
                        e.printStackTrace();
                    }
                }

                imgService.deleteImg(imgNo);
                System.out.println("✅ 已刪除圖片資料 imgNo = " + imgNo);
            } else {
                System.out.println("⚠️ 找不到圖片 imgNo = " + imgNo);
            }

            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("刪除圖片失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // Getter / Setter
    public int getImgNo() {
        return imgNo;
    }

    public void setImgNo(int imgNo) {
        this.imgNo = imgNo;
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }
}
