<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.eshop.admin.model.Admin" %>
<%
    Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
    if (loggedInAdmin == null) {
        response.sendRedirect(request.getContextPath() + "/adminLogin.action");
        return;
    }
%>

<div style="text-align:right;">
    👤 歡迎，<strong><%= loggedInAdmin.getName() %></strong>（帳號：<%= loggedInAdmin.getUsername() %>）
    | <a href="adminLogout.action">登出</a>
</div>
<html>
<head>
    <title>新增商品</title>
</head>
<body>
<h2>新增商品</h2>

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

        <!-- ✅ 圖片上傳欄位（支援多張） -->
        <tr>
            <td><label for="uploadFiles">上傳圖片</label></td>
            <td>
                <input type="file" name="uploadFiles" id="uploadFiles" multiple accept="image/*" />
                <s:hidden name="mainImageIndex" id="mainImageIndex" value="0" />
                <div id="preview" style="margin-top: 10px;"></div>
            </td>
        </tr>

        <tr>
            <td></td>
            <td><s:submit value="新增商品並上傳圖片" /></td>
        </tr>
    </table>
</s:form>

<!-- ✅ 驗證與預覽 -->
<script>
    let selectedFiles = [];
    let mainImageIndex = 0;

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

    document.getElementById("uploadFiles").addEventListener("change", function (event) {
        selectedFiles = Array.from(event.target.files);
        mainImageIndex = 0;
        rebuildPreview();
    });

    function rebuildPreview() {
        const preview = document.getElementById("preview");
        const mainIndexInput = document.getElementById("mainImageIndex");
        preview.innerHTML = "";

        selectedFiles.forEach((file, index) => {
            if (file.type.startsWith("image/")) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    const wrapper = document.createElement("div");
                    wrapper.style.display = "inline-block";
                    wrapper.style.margin = "5px";
                    wrapper.style.position = "relative";

                    const img = document.createElement("img");
                    img.src = e.target.result;
                    img.style.maxWidth = "100px";
                    img.style.maxHeight = "100px";
                    img.style.border = (index === mainImageIndex) ? "3px solid red" : "1px solid #ccc";
                    img.style.padding = "3px";
                    img.style.cursor = "pointer";

                    img.onclick = function () {
                        mainImageIndex = index;
                        rebuildPreview();
                    };

                    const removeBtn = document.createElement("button");
                    removeBtn.textContent = "×";
                    removeBtn.style.position = "absolute";
                    removeBtn.style.top = "0";
                    removeBtn.style.right = "0";
                    removeBtn.style.background = "red";
                    removeBtn.style.color = "white";
                    removeBtn.style.border = "none";
                    removeBtn.style.cursor = "pointer";

                    removeBtn.onclick = function () {
                        selectedFiles.splice(index, 1);
                        if (mainImageIndex >= selectedFiles.length) {
                            mainImageIndex = selectedFiles.length - 1;
                        }
                        rebuildFileInput();
                        rebuildPreview();
                    };

                    wrapper.appendChild(img);
                    wrapper.appendChild(removeBtn);
                    preview.appendChild(wrapper);
                };
                reader.readAsDataURL(file);
            }
        });

        mainIndexInput.value = mainImageIndex;
    }

    function rebuildFileInput() {
        const oldInput = document.getElementById("uploadFiles");
        const newInput = oldInput.cloneNode();
        oldInput.parentNode.replaceChild(newInput, oldInput);

        const dataTransfer = new DataTransfer();
        selectedFiles.forEach(file => dataTransfer.items.add(file));
        newInput.files = dataTransfer.files;
        newInput.id = "uploadFiles";

        // 綁定 change 事件（防止遞迴）
        newInput.addEventListener("change", function (e) {
            selectedFiles = Array.from(e.target.files);
            mainImageIndex = 0;
            rebuildPreview();
        });
    }
</script>


<a href="/back">🔙 回後台首頁</a>
</body>
</html>
