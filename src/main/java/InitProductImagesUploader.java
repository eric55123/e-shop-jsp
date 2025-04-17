import com.eshop.product.model.Product;
import com.eshop.product.service.ProductImgService;
import com.eshop.product.service.ProductService;
import com.eshop.util.GoogleDriveUploader;

import java.io.File;
import java.util.Arrays;

public class InitProductImagesUploader {

    public static void main(String[] args) {
        String folderPath = "src/main/resources/images"; // 圖片放這裡
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("❌ 資料夾不存在：" + folderPath);
            return;
        }

        File[] imageFiles = folder.listFiles((dir, name) -> name.matches("\\d+_\\d+\\.(jpg|png|jpeg|gif)"));
        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("⚠️ 沒有符合格式的圖片！");
            return;
        }

        ProductImgService imgService = new ProductImgService();
        ProductService productService = new ProductService();

        Arrays.sort(imageFiles); // 確保按名稱順序處理

        for (File file : imageFiles) {
            try {
                String name = file.getName().split("\\.")[0]; // 1_1
                String[] parts = name.split("_");
                int productNo = Integer.parseInt(parts[0]);
                int imgOrder = Integer.parseInt(parts[1]);

                Product product = productService.findById(productNo);
                if (product == null) {
                    System.out.println("⚠️ 找不到商品：" + productNo);
                    continue;
                }

                String imageUrl = GoogleDriveUploader.uploadImage(file);
                imgService.uploadImage(imageUrl, imgOrder, product);
                System.out.printf("✅ 商品 %d 圖片上傳成功（img_order = %d）\n", productNo, imgOrder);
            } catch (Exception e) {
                System.out.println("❌ 圖片上傳失敗：" + file.getName());
                e.printStackTrace();
            }
        }

        System.out.println("✅ 所有圖片處理完畢！");
    }
}