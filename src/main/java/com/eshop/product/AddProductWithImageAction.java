package com.eshop.product;

import com.eshop.product.ProductService.ProductImgService;
import com.eshop.product.ProductService.ProductService;
import com.eshop.product.model.Product;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;

public class AddProductWithImageAction extends ActionSupport {

    private Product product;
    private File[] uploadFile;
    private String[] uploadFileFileName;

    private ProductService productService = new ProductService();
    private ProductImgService imgService = new ProductImgService();

    @Override
    public String execute() {
        try {
            System.out.println("🚀 執行新增商品");
            productService.addProduct(product);
            System.out.println("✅ 商品已新增，商品編號：" + product.getProductNo());

            if (uploadFile != null && uploadFile.length > 0) {
                String uploadPath = ServletActionContext.getServletContext().getRealPath("/uploads");
                System.out.println("📁 圖片儲存目錄：" + uploadPath);
                File saveDir = new File(uploadPath);
                if (!saveDir.exists()) saveDir.mkdirs();

                for (int i = 0; i < uploadFile.length && i < 5; i++) {
                    File file = uploadFile[i];
                    String fileName = uploadFileFileName[i];

                    String savePath = uploadPath + File.separator + fileName;
                    System.out.println("📝 準備儲存圖片：" + savePath);
                    File destFile = new File(savePath);
                    FileUtils.copyFile(file, destFile);

                    String imageUrl = "uploads/" + fileName;
                    int nextOrder = imgService.getNextImageOrder(product);
                    imgService.uploadImage(imageUrl, nextOrder, product);
                    System.out.println("🌐 圖片資料寫入成功：" + imageUrl + " | 排序：" + nextOrder);
                }
            } else {
                System.out.println("⚠️ 沒有選擇任何圖片");
            }

            return SUCCESS;

        } catch (Exception e) {
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
}
