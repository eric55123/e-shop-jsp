package com.eshop.product.action.product;

import com.eshop.admin.model.Admin;
import com.eshop.admin.service.AdminLogService;
import com.eshop.member.model.Member;
import com.eshop.product.model.ProductCategory;
import com.eshop.product.service.ProductImgService;
import com.eshop.product.service.ProductService;
import com.eshop.product.service.ProductCommentService;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductComment;
import com.eshop.product.dao.ProductCategoryDAO;
import com.eshop.util.RequestUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductAction extends ActionSupport {

    private List<Product> productList;
    private Product product;
    private int productNo;
    private List<ProductCategory> categoryList;
    private Map<String, String> statusOptions = new LinkedHashMap<>();
    private List<ProductComment> comments;

    private ProductService productService = new ProductService();
    private ProductImgService imgService = new ProductImgService();
    private ProductCommentService commentService = new ProductCommentService();

    // 商品列表
    public String execute() {
        productList = productService.getAllProducts();
        for (Product p : productList) {
            if (p.getProductImgs() != null && !p.getProductImgs().isEmpty()) {
                p.setCoverImageUrl(p.getProductImgs().get(0).getProductImgUrl());
            }
        }
        return SUCCESS;
    }

    // 商品詳情
    public String detail() {
        product = productService.getProductById(productNo);

        // ✅ 確保有讀出所有圖片（用於 detail.jsp 顯示）
        if (product != null) {
            product.setProductImgs(imgService.getImagesByProduct(product)); // ⬅️ 加這行

            // ✅ 主圖（可用圖片第一張）
            if (product.getProductImgs() != null && !product.getProductImgs().isEmpty()) {
                product.setCoverImageUrl(product.getProductImgs().get(0).getProductImgUrl());
            }
        }

        // ✅ 登入會員
        HttpSession session = ServletActionContext.getRequest().getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (product != null) {
            comments = commentService.getPublicCommentsByProduct(product, loginMember);

            for (ProductComment c : comments) {
                if (c.getMember() != null) {
                    c.getMember().getName(); // 強制讀 name（防 LazyInit）
                }
            }
            return "success";
        }

        return ERROR;
    }

    // 顯示新增商品頁面
    public String showAddForm() {
        categoryList = new ProductCategoryDAO().findAll();
        statusOptions.put("1", "上架");
        statusOptions.put("0", "下架");
        return SUCCESS;
    }

    // 處理新增商品
    public String addProduct() {
        if (product != null) {

            int productNo = productService.addProduct(product);

            if (productNo > 0) {

                return SUCCESS;
            } else {

            }

        } else {
        }

        return ERROR;
    }

    // 顯示修改頁面
    public String editProduct() {
        product = productService.getProductById(productNo);
        if (product != null) {
            categoryList = new ProductCategoryDAO().findAll();
            statusOptions.put("1", "上架");
            statusOptions.put("0", "下架");
            return "edit";
        } else {
            addActionError("找不到此商品");
            return ERROR;
        }
    }

    // 處理商品更新
    public String updateProduct() {
        Admin loggedInAdmin = RequestUtil.getLoggedInAdmin();

        try {
            if (product != null && product.getProductNo() != null) {
                Product original = productService.getProductById(product.getProductNo());
                boolean success = productService.updateProduct(product);

                if (success) {
                    if (loggedInAdmin != null) {
                        new AdminLogService().log(
                                loggedInAdmin.getAdminId(),
                                "edit",
                                "product",
                                String.valueOf(product.getProductNo()),
                                "修改商品: " + original.getProductName(),
                                RequestUtil.getClientIp()
                        );
                    }
                    addActionMessage("商品修改成功！");
                    return SUCCESS;
                } else {
                    addActionError("商品修改失敗！");
                    return ERROR;
                }
            } else {
                addActionError("找不到要修改的商品！");
                return ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("更新失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // 刪除商品（包含圖片與實體圖片檔案）
    public String deleteProduct() {
        Admin loggedInAdmin = RequestUtil.getLoggedInAdmin();
        try {
            Product product = productService.getProductById(productNo);
            if (product != null) {
                imgService.deleteAllImagesWithFilesByProduct(product);
                productService.deleteProduct(productNo);
                if (loggedInAdmin != null) {
                    new AdminLogService().log(
                            loggedInAdmin.getAdminId(),
                            "delete",
                            "product",
                            String.valueOf(productNo),
                            "刪除商品: " + product.getProductName(),
                            RequestUtil.getClientIp()
                    );
                }
                addActionMessage("商品與圖片成功刪除！");
                return SUCCESS;
            } else {
                addActionError("找不到商品");
                return ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("刪除失敗：" + e.getMessage());
            return ERROR;
        }
    }

    // ===== Getter / Setter =====
    public List<Product> getProductList() {
        return productList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public List<ProductCategory> getCategoryList() {
        return categoryList;
    }

    public Map<String, String> getStatusOptions() {
        return statusOptions;
    }

    public void setStatusOptions(Map<String, String> statusOptions) {
        this.statusOptions = statusOptions;
    }

    public List<ProductComment> getComments() {
        return comments;
    }
}