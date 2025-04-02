<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>修改商品</title>
    <style>
        .img-preview {
            display: inline-block;
            margin: 10px;
            text-align: center;
        }
        .img-preview img {
            width: 100px;
            height: 100px;
            border: 1px solid #ccc;
            cursor: pointer;
        }
        .img-preview input[type="checkbox"] {
            margin-top: 5px;
        }
    </style>
</head>
<body>
<h2>修改商品</h2>

<s:form action="updateProductWithImages" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
    <s:hidden name="product.productNo" />

    <table>
        <tr>
            <td><label for="productName">商品名稱</label></td>
            <td><s:textfield name="product.productName" id="productName" size="30" required="true" /></td>
        </tr>

        <tr>
            <td><label for="productDesc">商品描述</label></td>
            <td><s:textarea name="product.productDesc" id="productDesc" rows="5" cols="30" /></td>
        </tr>

        <tr>
            <td><label for="productPrice">商品價格</label></td>
            <td><s:textfield name="product.productPrice" id="productPrice" size="10" required="true" /></td>
        </tr>

        <tr>
            <td><label for="remainingQty">剩餘庫存</label></td>
            <td><s:textfield name="product.remainingQty" id="remainingQty" size="10" readonly="true" /></td>
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
                          headerValue="請選擇類別" />
            </td>
        </tr>

        <tr>
            <td><label for="uploadFiles">上傳圖片</label></td>
            <td><input type="file" name="uploadFiles" id="uploadFiles" multiple accept="image/*" /></td>
        </tr>
        <tr>
            <td></td>
            <td><div class="preview" style="margin-top:10px;"></div></td>
        </tr>

        <tr>
            <td valign="top">已上傳圖片</td>
            <td>
                <s:iterator value="productImgs" var="img">
                    <div class="img-preview">
                        <label>
                            <img src="${pageContext.request.contextPath}/<s:property value='#img.productImgUrl' />" />
                            <br/>
                            <input type="checkbox" name="deleteImgNos" value="<s:property value='#img.productImgNo' />" /> 刪除
                        </label>
                    </div>
                </s:iterator>
            </td>
        </tr>

        <tr>
            <td></td>
            <td><s:submit value="更新商品" /></td>
        </tr>
    </table>
</s:form>

<p><a href="productList.action">← 回商品列表</a></p>

<script>
    function validateForm() {
        const price = document.getElementById("productPrice").value.trim();
        const status = document.getElementById("productStatus").value;
        const category = document.getElementById("productCategory").value;

        if (!price || isNaN(price) || Number(price) <= 0) {
            alert("請輸入正確的商品價格（必須為正數）");
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

    document.getElementById("uploadFiles").addEventListener("change", function (event) {
        const previewContainer = document.querySelector(".preview");
        if (previewContainer) previewContainer.innerHTML = "";

        const files = event.target.files;
        for (let i = 0; i < files.length; i++) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const img = document.createElement("img");
                img.src = e.target.result;
                img.style.maxWidth = "100px";
                img.style.margin = "5px";
                if (previewContainer) previewContainer.appendChild(img);
            }
            reader.readAsDataURL(files[i]);
        }
    });
</script>
</body>
</html>