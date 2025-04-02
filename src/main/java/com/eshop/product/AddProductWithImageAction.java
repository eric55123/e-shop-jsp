package com.eshop.product;

import com.eshop.product.ProductService.ProductImgService;
import com.eshop.product.ProductService.ProductService;
import com.eshop.product.model.Product;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.UUID;

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
            // ✅ 這裡加驗證（前面就擋掉錯資料）
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
            System.out.println("🚀 執行新增商品");

            // 新增商品
            productService.addProduct(product);
            System.out.println("✅ 商品已新增，商品編號：" + product.getProductNo());

            // 處理圖片上傳
            if (uploadFile != null && uploadFile.length > 0) {
                String uploadPath = ServletActionContext.getServletContext().getRealPath("/uploads");
                System.out.println("📁 圖片儲存目錄：" + uploadPath);

                File saveDir = new File(uploadPath);
                if (!saveDir.exists()) saveDir.mkdirs();

                int nextOrder = imgService.getNextImageOrder(product);

                for (int i = 0; i < uploadFile.length && i < 5; i++) {
                    File file = uploadFile[i];
                    String originalName = uploadFileFileName[i];
                    String ext = originalName.substring(originalName.lastIndexOf("."));
                    String uuidName = UUID.randomUUID().toString() + ext;

                    String savePath = uploadPath + File.separator + uuidName;
                    System.out.println("📝 儲存圖片：" + savePath);

                    File destFile = new File(savePath);
                    FileUtils.copyFile(file, destFile);

                    String imageUrl = "uploads/" + uuidName;
                    imgService.uploadImage(imageUrl, nextOrder++, product);

                    System.out.println("🌐 圖片資料寫入成功：" + imageUrl + " | 排序：" + (nextOrder - 1));
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
