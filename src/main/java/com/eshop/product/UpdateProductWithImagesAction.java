package com.eshop.product;

import com.eshop.product.Service.ProductImgService;
import com.eshop.product.Service.ProductService;
import com.eshop.product.Model.Product;
import com.eshop.product.Model.ProductImg;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class UpdateProductWithImagesAction extends ActionSupport {

    private Product product;
    private List<File> uploadFiles;
    private List<String> uploadFilesFileName;
    private List<Integer> deleteImgNos;
    private List<String> uploadFilesContentType;
    private ProductService productService = new ProductService();
    private ProductImgService imgService = new ProductImgService();

    @Override
    public String execute() {
        try {
            System.out.println("\uD83D\uDEA7 執行 UpdateProductWithImagesAction");
            System.out.println("\uD83D\uDD27 商品編號：" + product.getProductNo());

            // 查出原本的 product（含圖片）
            Product dbProduct = productService.getProductById(product.getProductNo());
            dbProduct.setProductName(product.getProductName());
            dbProduct.setProductDesc(product.getProductDesc());
            dbProduct.setProductPrice(product.getProductPrice());
            dbProduct.setRemainingQty(product.getRemainingQty());
            dbProduct.setProductStatus(product.getProductStatus());
            dbProduct.setProductCategory(product.getProductCategory());

            // 刪除圖片
            if (deleteImgNos != null && !deleteImgNos.isEmpty()) {
                Iterator<ProductImg> iterator = dbProduct.getProductImgs().iterator();
                while (iterator.hasNext()) {
                    ProductImg img = iterator.next();
                    if (deleteImgNos.contains(img.getProductImgNo())) {
                        iterator.remove();
                        imgService.deleteImg(img.getProductImgNo());
                    }
                }
            }

            // 上傳新圖片
            if (uploadFiles != null && !uploadFiles.isEmpty()) {
                String uploadPath = ServletActionContext.getServletContext().getRealPath("/uploads");
                File saveDir = new File(uploadPath);
                if (!saveDir.exists()) saveDir.mkdirs();

                int nextOrder = imgService.getNextImgOrder(dbProduct);

                for (int i = 0; i < uploadFiles.size(); i++) {
                    File file = uploadFiles.get(i);
                    String fileName = uploadFilesFileName.get(i);
                    File destFile = new File(uploadPath + File.separator + fileName);
                    FileUtils.copyFile(file, destFile);

                    String imageUrl = "uploads/" + fileName;
                    imgService.uploadImage(imageUrl, nextOrder++, dbProduct);
                }
            }

            // 更新商品
            productService.updateProduct(dbProduct);
            return SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            addActionError("更新失敗：" + e.getMessage());
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

    public List<File> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(List<File> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public List<String> getUploadFilesFileName() {
        return uploadFilesFileName;
    }

    public void setUploadFilesFileName(List<String> uploadFilesFileName) {
        this.uploadFilesFileName = uploadFilesFileName;
    }

    public List<String> getUploadFilesContentType() {
        return uploadFilesContentType;
    }

    public void setUploadFilesContentType(List<String> uploadFilesContentType) {
        this.uploadFilesContentType = uploadFilesContentType;
    }

    public List<Integer> getDeleteImgNos() {
        return deleteImgNos;
    }

    public void setDeleteImgNos(List<Integer> deleteImgNos) {
        this.deleteImgNos = deleteImgNos;
    }
}