package com.eshop.product.action.product;

import com.eshop.product.model.Product;
import com.eshop.product.service.ProductImgService;
import com.eshop.product.service.ProductService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.io.File;

public class AddProductWithImageAction extends ActionSupport {

    private Product product;
    private File[] uploadFile;
    private String[] uploadFileFileName;
    private String[] uploadFileContentType;

    private ProductService productService = new ProductService();
    private ProductImgService imgService = new ProductImgService();

    @Override
    public String execute() {
        try {
            // ✅ 商品欄位驗證
            if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
                addActionError("商品名稱不能為空");
                return ERROR;
            }
            if (product.getProductPrice() == null || product.getProductPrice().doubleValue() <= 0) {
                addActionError("商品價格必須大於 0");
                return ERROR;
            }
            if (product.getProductAddQty() == null || product.getProductAddQty() <= 0) {
                addActionError("上架數量必須為正整數");
                return ERROR;
            }
            if (product.getProductStatus() == null) {
                addActionError("請選擇商品狀態");
                return ERROR;
            }
            if (product.getProductCategory() == null || product.getProductCategory().getProductCategoryId() == null) {
                addActionError("請選擇商品分類");
                return ERROR;
            }

            System.out.println("\uD83D\uDE80 執行新增商品");

            // ✅ 商品新增
            productService.addProduct(product);
            System.out.println("✅ 商品已新增，商品編號：" + product.getProductNo());

            // ✅ 管理員操作紀錄
            com.eshop.admin.model.Admin loggedInAdmin =
                    (com.eshop.admin.model.Admin) ServletActionContext.getRequest()
                            .getSession().getAttribute("loggedInAdmin");

            if (loggedInAdmin != null && product.getProductNo() != null) {
                System.out.println("\uD83D\uDCDD 寫入商品新增 log 中...");
                new com.eshop.admin.service.AdminLogService().log(
                        loggedInAdmin.getAdminId(),
                        "add",
                        "product",
                        String.valueOf(product.getProductNo()),
                        "新增商品: " + product.getProductName(),
                        com.eshop.util.RequestUtil.getClientIp()
                );
            }

            // ✅ 圖片上傳至 Google Drive
            if (uploadFile != null && uploadFile.length > 0) {
                int nextOrder = imgService.getNextImageOrder(product);

                for (int i = 0; i < uploadFile.length && i < 5; i++) {
                    File file = uploadFile[i];

                    // ✅ 呼叫 GoogleDriveUploader 上傳並取得網址
                    String imageUrl = com.eshop.util.GoogleDriveUploader.uploadImage(file);

                    // ✅ 存進資料庫
                    imgService.uploadImage(imageUrl, nextOrder++, product);

                    System.out.println("\u2601\uFE0F 已上傳圖片至 Google Drive：" + imageUrl + " | 排序：" + (nextOrder - 1));
                }
            } else {
                System.out.println("⚠️ 沒有選擇任何圖片");
            }

            return SUCCESS;

        } catch (Exception e) {
            System.out.println("❌ 上傳過程中發生例外錯誤");
            e.printStackTrace();
            addActionError("上傳失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // ===== Getter / Setter =====

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public File[] getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File[] uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String[] getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String[] uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String[] getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String[] uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }
}
