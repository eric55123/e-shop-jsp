package com.eshop.product;

import com.eshop.member.model.Member;
import com.eshop.product.model.ProductCategory;
import com.eshop.product.service.ProductImgService;
import com.eshop.product.service.ProductService;
import com.eshop.product.service.ProductCommentService;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductComment;
import com.eshop.product.dao.ProductCategoryDAO;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
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
        return SUCCESS;
    }

    // 商品詳情
    public String detail() {
        product = productService.getProductById(productNo);

        HttpSession session = ServletActionContext.getRequest().getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (product != null) {
            comments = commentService.getPublicCommentsByProduct(product, loginMember);

            // 可選的 Lazy Load 預載（但 JOIN FETCH 已預載就不必）
            for (ProductComment c : comments) {
                if (c.getMember() != null) {
                    c.getMember().getName();
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
            productService.addProduct(product);
            return SUCCESS;
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
        if (product != null && product.getProductNo() != null) {
            boolean success = productService.updateProduct(product);
            return success ? SUCCESS : ERROR;
        }
        return ERROR;
    }

    // 刪除商品（包含圖片與實體圖片檔案）
    public String deleteProduct() {
        try {
            Product product = productService.getProductById(productNo);
            if (product != null) {
                imgService.deleteAllImagesWithFilesByProduct(product);
                productService.deleteProduct(productNo);
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