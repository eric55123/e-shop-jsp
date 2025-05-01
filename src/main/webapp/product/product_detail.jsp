<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.eshop.product.model.Product" %>
<%@ page import="com.eshop.product.model.ProductComment" %>
<%@ page import="com.eshop.member.model.Member" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<jsp:include page="/includes/header.jsp" />

<!-- ✅ zmdi 圖示 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/material-design-iconic-font@2.2.0/dist/css/material-design-iconic-font.min.css">

<style>
    .zmdi {
        font-size: 18px;
        vertical-align: middle;
    }

</style>

<%!
    public static String escapeHtml(String input) {
        if (input == null) return "";
        return input.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;");
    }
%>

<%
    Product product = (Product) request.getAttribute("product");
    List<ProductComment> comments = (List<ProductComment>) request.getAttribute("comments");
    Member loginMember = (Member) session.getAttribute("loginMember");
    Set<Integer> reportedCommentIds = (Set<Integer>) request.getAttribute("reportedCommentIds");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    float avgRating = 0f;
    int starRating = 0;
    int commentCount = 0;
    if (comments != null && !comments.isEmpty()) {
        int sum = 0;
        for (ProductComment c : comments) {
            if (c.getStatus() == 1) {
                sum += c.getRating();
                commentCount++;
            }
        }
        if (commentCount > 0) {
            avgRating = (float) sum / commentCount;
            starRating = Math.round(avgRating);
        }
    }
%>

<div class="wrapper fixed__footer container mt-5">
    <% if (product != null) { %>
    <div class="row">
        <div class="col-md-6">
            <% if (product.getCoverImageUrl() != null) { %>
            <!-- 主圖 -->
            <div class="text-center mb-4">
                <img src="https://drive.google.com/thumbnail?id=<%= product.getCoverImageThumbnailUrl().split("id=")[1] %>"
                     class="img-fluid rounded border"
                     style="max-width: 400px; height: auto;" />
            </div>
            <% } %>

            <% if (product.getProductImgs() != null && !product.getProductImgs().isEmpty()) { %>
            <!-- 縮圖列 -->
            <div class="d-flex flex-wrap gap-2 justify-content-center">
                <% for (com.eshop.product.model.ProductImg img : product.getProductImgs()) {
                    if (img.getProductImgUrl() != null && img.getProductImgUrl().contains("id=")) { %>
                <img src="https://drive.google.com/thumbnail?id=<%= img.getProductImgUrl().split("id=")[1] %>"
                     class="rounded border"
                     style="width: 80px; height: 80px; object-fit: cover;" />
                <% }} %>
            </div>
            <% } %>
        </div>

        <div class="col-md-6 col-lg-6 col-sm-12 smt-30 xmt-30">
            <div class="htc__product__details__inner">
                <!-- 商品名稱 + 評分 -->
                <div class="pro__detl__title d-flex align-items-center gap-2">
                    <h2 class="mb-0 d-flex align-items-center">
                        <%= product.getProductName() %>
                        <span class="ms-3 d-flex align-items-center">
                            <% if (commentCount > 0) { %>
                                <% for (int i = 1; i <= 5; i++) { %>
                               <i class="zmdi zmdi-star<%= i <= starRating ? "" : "-outline" %>" style="color: #f5c518;"></i>
                                <% } %>
                                <span class="ms-1">(<%= String.format("%.1f", avgRating) %>)</span>
                            <% } else { %>
                                <span class="text-muted ms-2">(尚無評分)</span>
                            <% } %>
                        </span>
                    </h2>
                </div>

                <!-- 商品描述 -->
                <div class="pro__details">
                    <p><%= escapeHtml(product.getProductDesc()) %></p>
                </div>

                <!-- 商品資訊與購買區塊 -->
                <div class="card p-4 mt-4 shadow-sm" style="max-width: 400px;">
                    <!-- 價格 -->
                    <div class="mb-3 d-flex justify-content-between">
                        <strong>價格：</strong>
                        <span>$<%= product.getProductPrice() %></span>
                    </div>

                    <!-- 庫存 -->
                    <div class="mb-3 d-flex justify-content-between">
                        <strong>庫存：</strong>
                        <% if (product.getRemainingQty() > 0) { %>
                        <span><%= product.getRemainingQty() %> 件</span>
                        <% } else { %>
                        <span class="text-danger">目前已售完</span>
                        <% } %>
                    </div>

                    <!-- 購買數量 -->
                    <% if (product.getRemainingQty() > 0) { %>
                    <form action="addToCart.action" method="post">
                        <input type="hidden" name="productNo" value="<%= product.getProductNo() %>" />

                        <div class="mb-3">
                            <label for="quantity" class="form-label"><strong>購買數量：</strong></label>
                            <input type="number" name="quantity" id="quantity" class="form-control" value="1" min="1" max="<%= product.getRemainingQty() %>" style="width: 100px;" />
                        </div>

                        <button type="submit" class="btn btn-success w-100">加入購物車</button>
                    </form>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <!-- 評論區塊 -->
    <section class="htc__product__details__tab bg__white pt--50 pb--120">
        <div class="row">
            <div class="col-md-12">
                <h3 class="mb--30">評論區</h3>
                <span class="rat__qun">(已有 <%= comments != null ? comments.size() : 0 %> 筆評論)</span>

                <% if (comments != null && !comments.isEmpty()) { %>
                <% for (ProductComment comment : comments) {
                    int cid = comment.getCommentId();
                    int status = comment.getStatus();
                    boolean isOwner = loginMember != null && comment.getMember() != null &&
                            comment.getMember().getMemberId().equals(loginMember.getMemberId());
                    String displayName = (comment.getMember() != null && comment.getMember().getUsername() != null)
                            ? comment.getMember().getUsername()
                            : (comment.getMember() != null ? comment.getMember().getName() : "匿名");
                    String safeText = escapeHtml(comment.getCommentText());
                %>
                <div class="pro__review mb-4 p-3 border rounded bg-light">
                    <div class="review__details">

                        <% if (status == 1) { %>
                        <!-- ✅ 正常留言才顯示會員名字、星星 -->
                        <div class="review__info mb-2 d-flex align-items-center gap-2">
                            <h5 class="mb-0 me-2"><%= displayName %></h5>
                            <div>
                                <% for (int i = 1; i <= 5; i++) { %>
                                <i class="zmdi zmdi-star<%= i <= comment.getRating() ? "" : "-outline" %>" style="color: #f5c518;"></i>
                                <% } %>
                            </div>
                        </div>
                        <% } %>

                        <div class="review__date mb-1 d-flex justify-content-between">
                            <small class="text-muted"><%= comment.getCommentTime().format(formatter) %></small>

                            <% if (status == 1 && isOwner) { %>
                            <div class="d-flex gap-2">
                                <button class="btn btn-sm btn-outline-primary" onclick="toggleEditForm('<%= cid %>')">編輯</button>
                                <button class="btn btn-sm btn-outline-danger" onclick="deleteComment('<%= cid %>')">刪除</button>
                            </div>
                            <% } %>
                        </div>

                        <!-- 留言內容 -->
                        <p id="comment-text-<%= cid %>" class="mb-0">
                            <% if (status == 0) { %>
                            <span style="color:gray; font-style:italic;">此留言已由使用者刪除</span>
                            <% } else if (status == -1) { %>
                            <span style="color:gray; font-style:italic;">此留言為不當言論</span>
                            <% } else { %>
                            <%= safeText %>
                            <% } %>
                        </p>

                        <% if (status == 1 && !isOwner && loginMember != null && (reportedCommentIds == null || !reportedCommentIds.contains(cid))) { %>
                        <div class="text-end mt-2">
                            <button class="btn btn-sm btn-outline-warning" onclick="reportComment('<%= cid %>')">檢舉</button>
                        </div>
                        <% } else if (status == 1 && !isOwner && loginMember != null && reportedCommentIds.contains(cid)) { %>
                        <div class="text-end mt-2 text-muted" style="font-size: 0.85rem;">
                            您已檢舉過此留言
                        </div>
                        <% } %>


                        <% if (status == 1 && isOwner) { %>
                        <form id="edit-form-<%= cid %>" onsubmit="submitEditForm(event, '<%= cid %>')" class="mt-3" style="display: none;">
                            <input type="hidden" name="commentId" value="<%= cid %>">
                            <div class="form-group mb-2">
                                <label>修改評分</label>
                                <select name="rating" class="form-control" required>
                                    <% for (int i = 5; i >= 1; i--) { %>
                                    <option value="<%= i %>" <%= i == comment.getRating() ? "selected" : "" %>><%= i %> 顆星</option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="form-group mb-3">
                                <label>修改評論內容</label>
                                <textarea name="commentText" class="form-control" rows="3" required><%= comment.getCommentText() %></textarea>
                            </div>
                            <button type="submit" class="btn btn-sm btn-success">保存修改</button>
                            <button type="button" class="btn btn-sm btn-secondary" onclick="toggleEditForm('<%= cid %>')">取消</button>
                        </form>
                        <% } %>

                    </div>
                </div>
                <% } %>

                <% } else { %>
                <p>目前尚無評論。</p>
                <% } %>

                <!-- 發表新評論 -->
                <% if (loginMember != null) { %>
                <hr>
                <h4>發表評論</h4>
                <form action="addProductComment.action" method="post">
                    <input type="hidden" name="productNo" value="<%= product.getProductNo() %>">
                    <div class="form-group mb-2">
                        <label>評分</label>
                        <select name="rating" class="form-control" required>
                            <option value="">請選擇</option>
                            <% for (int i = 5; i >= 1; i--) { %>
                            <option value="<%= i %>"><%= i %> 顆星</option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group mb-3">
                        <label>評論內容</label>
                        <textarea name="commentText" class="form-control" rows="4" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">送出評論</button>
                </form>
                <% } else { %>
                <p>請先 <a href="login.action">登入</a> 才能留言。</p>
                <% } %>
            </div>
        </div>
    </section>
    <% } else { %>
    <p>查無此商品。</p>
    <% } %>
</div>

<script>
    function toggleEditForm(commentId) {
        const form = document.getElementById('edit-form-' + commentId);
        const text = document.getElementById('comment-text-' + commentId);
        if (form.style.display === 'none') {
            form.style.display = 'block';
            text.style.display = 'none';
        } else {
            form.style.display = 'none';
            text.style.display = 'block';
        }
    }

    function submitEditForm(event, commentId) {
        event.preventDefault();
        const form = document.getElementById('edit-form-' + commentId);
        const formData = new FormData(form);

        fetch('updateProductComment.action', {
            method: 'POST',
            body: formData
        })
            .then(resp => resp.json())
            .then(result => {
                if (result.message === "success") {
                    alert('✅ 評論已更新');
                    location.reload();
                } else {
                    alert('❌ 更新失敗');
                }
            })
            .catch(err => {
                console.error(err);
                alert('❌ 系統錯誤');
            });
    }

    function deleteComment(commentId) {
        if (!confirm('確定要刪除這則評論嗎？')) return;

        fetch('deleteProductComment.action?commentId=' + commentId)
            .then(resp => resp.json())
            .then(result => {
                if (result.message === "deleted") {
                    alert('🗑 已刪除評論');
                    location.reload();
                } else {
                    alert('❌ 刪除失敗');
                }
            })
            .catch(err => {
                console.error(err);
                alert('❌ 系統錯誤');
            });
    }

    function reportComment(commentId) {
        const reason = prompt("請輸入檢舉理由：");
        if (!reason || reason.trim() === "") {
            alert("❗ 檢舉理由不得為空");
            return;
        }

        const formData = new FormData();
        formData.append("commentId", commentId);
        formData.append("reason", reason);
        formData.append("productNo", "<%= product.getProductNo() %>");

        fetch("commentReport.action", {
            method: "POST",
            body: formData
        })
            .then(resp => resp.json())
            .then(result => {
                if (result.message === "檢舉成功") {
                    alert("✅ 檢舉已送出，感謝您的協助！");
                    location.reload();
                } else {
                    alert("⚠️ " + result.message);
                }
            })
            .catch(err => {
                console.error(err);
                alert("❌ 檢舉失敗，請稍後再試！");
            });
    }

</script>


<jsp:include page="/includes/footer.jsp" />
