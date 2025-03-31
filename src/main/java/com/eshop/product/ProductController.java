package com.eshop.product;

import com.eshop.product.DAO.ProductCategoryDAO;
import com.eshop.product.DAO.ProductDAO;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductCategory;
import com.opensymphony.xwork2.ActionSupport;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductController extends ActionSupport {

    private List<ProductCategory> categoryList;
    private Map<String, String> statusOptions = new LinkedHashMap<>(); // 給 JSP 狀態下拉選單用

    private ProductService productService = new ProductService();
    private List<Product> productList;
    private Product product;
    private int productNo;

    // 商品列表
    public String execute() {
        productList = productService.getAllProducts();
        return SUCCESS;
    }

    // 商品詳情
    public String detail() {
        product = productService.getProductById(productNo);
        return (product != null) ? "detail" : ERROR;
    }

    // 顯示新增商品頁面
    public String showAddForm() {
        // 取得分類列表 (供 JSP 下拉選單使用)
        ProductCategoryDAO categoryDAO = new ProductCategoryDAO();
        categoryList = categoryDAO.findAll();

        // 狀態選項初始化（上架/下架）
        statusOptions = new LinkedHashMap<>();
        statusOptions.put("1", "上架");
        statusOptions.put("0", "下架");

        return SUCCESS;
    }


    // 處理新增商品
    public String addProduct() {
        categoryList = new ProductCategoryDAO().findAll(); // 確保驗證失敗能重新顯示下拉選單

        if (product != null) {
            product.setProductAddTime(LocalDateTime.now());
            product.setRemainingQty(product.getProductAddQty());
            productService.addProduct(product);
            return SUCCESS;
        }
        return ERROR;
    }

    // 顯示修改頁面 (對應 editProduct.action)
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

    // 處理修改提交 (對應 updateProduct.action)
    public String updateProduct() {
        if (product != null && product.getProductNo() != null) {
            productService.updateProduct(product);
            addActionMessage("商品修改成功！");
            return SUCCESS;
        } else {
            addActionError("商品修改失敗！");
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
}
