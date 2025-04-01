package com.eshop.product;

import com.eshop.product.DAO.ProductCategoryDAO;
import com.eshop.product.ProductService.ProductService;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductCategory;
import com.opensymphony.xwork2.ActionSupport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductController extends ActionSupport {

    private List<ProductCategory> categoryList;
    private Map<String, String> statusOptions = new LinkedHashMap<>();

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
        categoryList = new ProductCategoryDAO().findAll();
        statusOptions.put("1", "上架");
        statusOptions.put("0", "下架");
        return SUCCESS;
    }

    // 處理新增商品
    public String addProduct() {
        categoryList = new ProductCategoryDAO().findAll();

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
            if (success) {
                addActionMessage("商品修改成功！");
                return SUCCESS;
            }
        }
        addActionError("商品修改失敗！");
        return ERROR;
    }

    // 刪除商品
    public String deleteProduct() {
        boolean success = productService.deleteProduct(productNo);
        if (success) {
            addActionMessage("商品刪除成功！");
            return SUCCESS;
        } else {
            addActionError("商品刪除失敗！");
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
