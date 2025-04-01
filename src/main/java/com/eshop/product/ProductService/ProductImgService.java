package com.eshop.product.ProductService;
import com.eshop.product.DAO.ProductImgDAO;
import com.eshop.product.model.Product;
import com.eshop.product.model.ProductImg;

import java.time.LocalDateTime;
import java.util.List;

public class ProductImgService {
    private ProductImgDAO dao = new ProductImgDAO();

    public List<ProductImg> getImagesByProduct(Product product) {
        return dao.findByProduct(product);
    }

    public void uploadImage(String imageUrl, int imgOrder, Product product) {
        ProductImg img = new ProductImg();
        img.setProduct(product);
        img.setProductImgUrl(imageUrl);
        img.setImgOrder(imgOrder);
        img.setCreatedAt(LocalDateTime.now());
        dao.insert(img);
    }

    public void deleteImg(int imgNo) {
        dao.deleteById(imgNo);
    }

    public int getNextImageOrder(Product product) {
        List<ProductImg> existingImgs = dao.findByProduct(product);
        return existingImgs.size() + 1;
    }

    public int getNextImgOrder(Product product) {
        List<ProductImg> imgList = new ProductImgDAO().findByProduct(product);
        int maxOrder = 0;
        for (ProductImg img : imgList) {
            if (img.getImgOrder() > maxOrder) {
                maxOrder = img.getImgOrder();
            }
        }
        return maxOrder + 1; // 下一張圖片的順序編號
    }


}
