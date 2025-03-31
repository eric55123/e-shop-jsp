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
    private Map<String, String> statusOptions = new LinkedHashMap<>();

    private ProductDAO productDAO = new ProductDAO();
    private ProductService productService = new ProductService();

    // 表單綁定欄位
    private List<Product> productList;
    private Product product;
    private int productNo;

    // 商品列表 (對應 productList.action)
    public String execute() {
        productList = productService.getAllProducts();
        return SUCCESS;
    }

    // 商品詳情 (對應 productDetail.action?productNo=xxx)
    public String detail() {
        product = productService.getProductById(productNo);
        return (product != null) ? "detail" : ERROR;
    }

    // 顯示新增商品頁面 (對應 showAddProduct.action)
    public String showAddForm() {
        ProductCategoryDAO categoryDAO = new ProductCategoryDAO();
        categoryList = categoryDAO.findAll(); // ✅ 把分類資料撈出來
        return SUCCESS; // product_add.jsp
    }


    // 處理新增商品 (對應 addProduct.action)
    public String addProduct() {
        if (product != null) {
            // 自動補上時間與預設值
            product.setProductAddTime(LocalDateTime.now());
            product.setProductRemoveTime(null);

            // 自動補齊庫存數量
            if (product.getProductAddQty() != null && product.getRemainingQty() == null) {
                product.setRemainingQty(product.getProductAddQty());
            }

            // 印出確認用
            System.out.println("✅ 收到新增商品資料：");
            System.out.println("名稱：" + product.getProductName());
            System.out.println("價格：" + product.getProductPrice());
            System.out.println("上架數量：" + product.getProductAddQty());
            System.out.println("庫存：" + product.getRemainingQty());
            System.out.println("分類ID：" + product.getProductCategory());
            System.out.println("狀態：" + product.getProductStatus());

            productService.addProduct(product);
            addActionMessage("商品新增成功！");
            return SUCCESS;
        } else {
            addActionError("新增商品失敗，請檢查輸入資料！");
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
}
