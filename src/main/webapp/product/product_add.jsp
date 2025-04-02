<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>新增商品</title>
</head>
<body>
<h2>新增商品</h2>

<!-- ✅ 改成新的 Action 並加上 enctype 和驗證事件 -->
<s:form action="addProductWithImage" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
    <table>
        <tr>
            <td><label for="productName">商品名稱</label></td>
            <td><s:textfield name="product.productName" id="productName" required="true" size="30" /></td>
        </tr>

        <tr>
            <td><label for="productDesc">商品描述</label></td>
            <td><s:textarea name="product.productDesc" id="productDesc" rows="5" cols="30" /></td>
        </tr>

        <tr>
            <td><label for="productPrice">商品價格</label></td>
            <td><s:textfield name="product.productPrice" id="productPrice" required="true" size="10" /></td>
        </tr>

        <tr>
            <td><label for="productAddQty">上架數量</label></td>
            <td><s:textfield name="product.productAddQty" id="productAddQty" required="true" size="10" /></td>
        </tr>

        <tr>
            <td><label for="productStatus">商品狀態</label></td>
            <td>
                <s:select name="product.productStatus"
                          id="productStatus"
                          list="statusOptions"
                          listKey="key"
                          listValue="value"
                          headerKey=""
                          headerValue="請選擇狀態" />
            </td>
        </tr>

        <tr>
            <td><label for="productCategory">商品類別</label></td>
            <td>
                <s:select name="product.productCategory.productCategoryId"
                          id="productCategory"
                          list="categoryList"
                          listKey="productCategoryId"
                          listValue="productCategoryName"
                          headerKey=""
                          headerValue="請選擇分類" />
            </td>
        </tr>

        <!-- ✅ 圖片上傳欄位 -->
        <tr>
            <td><label for="uploadFile">上傳圖片</label></td>
            <td>
                <input type="file" name="uploadFile" id="uploadFile" multiple accept="image/*" />
                <div id="preview" style="margin-top: 10px;"></div>
            </td>
        </tr>

        <tr>
            <td></td>
            <td><s:submit value="新增商品並上傳圖片" /></td>
        </tr>
    </table>
</s:form>

<!-- ✅ 表單驗證腳本 -->
<script>
    function validateForm() {
        const price = document.getElementById("productPrice").value.trim();
        const qty = document.getElementById("productAddQty").value.trim();
        const status = document.getElementById("productStatus").value;
        const category = document.getElementById("productCategory").value;

        if (!price || isNaN(price) || Number(price) <= 0) {
            alert("請輸入正確的商品價格（必須為正數）");
            return false;
        }

        if (!qty || isNaN(qty) || !Number.isInteger(Number(qty)) || Number(qty) <= 0) {
            alert("請輸入正確的上架數量（必須為正整數）");
            return false;
        }

        if (!status) {
            alert("請選擇商品狀態");
            return false;
        }

        if (!category) {
            alert("請選擇商品類別");
            return false;
        }

        return true;
    }

    // ✅ 圖片預覽
    document.getElementById("uploadFile").addEventListener("change", function (event) {
        const preview = document.getElementById("preview");
        preview.innerHTML = "";

        const files = event.target.files;
        for (let i = 0; i < files.length; i++) {
            const file = files[i];

            if (file.type.startsWith("image/")) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    const img = document.createElement("img");
                    img.src = e.target.result;
                    img.style.maxWidth = "100px";
                    img.style.maxHeight = "100px";
                    img.style.marginRight = "10px";
                    img.style.marginBottom = "10px";
                    img.style.border = "1px solid #ccc";
                    img.style.padding = "3px";
                    img.style.boxShadow = "1px 1px 4px rgba(0,0,0,0.2)";
                    preview.appendChild(img);
                };
                reader.readAsDataURL(file);
            }
        }
    });
</script>

</body>
</html>
