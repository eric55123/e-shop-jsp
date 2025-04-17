package com.eshop.product.action.product;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminLogService;
import com.eshop.product.service.ProductImgService;
import com.eshop.product.service.ProductService;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductImg;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.List;

public class AddProductWithImageAction extends ActionSupport {

    private Product product;
    private File[] uploadFiles;
    private String[] uploadFilesFileName;
    private String[] uploadFilesContentType;
    private Integer mainImageIndex; // ✅ 前端指定的主圖索引

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

            // ✅ 新增商品
            productService.addProduct(product);
            System.out.println("✅ 商品已新增，商品編號：" + product.getProductNo());

            // ✅ 管理員操作紀錄
            Admin loggedInAdmin = RequestUtil.getLoggedInAdmin();
            if (loggedInAdmin != null && product.getProductNo() != null) {
                new AdminLogService().log(
                        loggedInAdmin.getAdminId(),
                        "add",
                        "product",
                        String.valueOf(product.getProductNo()),
                        "新增商品: " + product.getProductName(),
                        RequestUtil.getClientIp()
                );
            }

            // ✅ 上傳圖片（Google Drive）
            if (uploadFiles != null && uploadFiles.length > 0) {
                int order = 1;
                for (int i = 0; i < uploadFiles.length && i < 5; i++) {
                    File file = uploadFiles[i];
                    int imgOrder = (mainImageIndex != null && mainImageIndex == i) ? 0 : order++;
                    imgService.uploadImage(file, imgOrder, product);
                }
            } else {
                System.out.println("⚠️ 沒有選擇任何圖片");
            }

            return SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            addActionError("新增失敗：" + e.getMessage());
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

    public File[] getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(File[] uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public String[] getUploadFilesFileName() {
        return uploadFilesFileName;
    }

    public void setUploadFilesFileName(String[] uploadFilesFileName) {
        this.uploadFilesFileName = uploadFilesFileName;
    }

    public String[] getUploadFilesContentType() {
        return uploadFilesContentType;
    }

    public void setUploadFilesContentType(String[] uploadFilesContentType) {
        this.uploadFilesContentType = uploadFilesContentType;
    }

    public Integer getMainImageIndex() {
        return mainImageIndex;
    }

    public void setMainImageIndex(Integer mainImageIndex) {
        this.mainImageIndex = mainImageIndex;
    }
}
