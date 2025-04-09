<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.eshop.product.model.Product" %>
<%@ page import="com.eshop.product.model.ProductComment" %>
<%@ page import="com.eshop.member.model.Member" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

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
%>

<html>
<head>
    <title>商品詳情</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function reportComment(commentId, form) {
            const reason = $(form).find("select[name='reason']").val();
            if (!reason) {
                alert("請選擇檢舉原因！");
                return false;
            }

            $.post("reportComment.action", {
                commentId: commentId,
                reason: reason
            }).done(function () {
                $(form).replaceWith('<span style="color:gray; margin-left: 10px;">您已檢舉</span>');
            }).fail(function () {
                alert("檢舉失敗，請稍後再試");
            });

            return false;
        }

        function showEditForm(commentId) {
            $("#commentDisplay_" + commentId).hide();
            $("#commentEdit_" + commentId).show();
        }

        function cancelEdit(commentId) {
            $("#commentEdit_" + commentId).hide();
            $("#commentDisplay_" + commentId).show();
        }

        function submitEdit(commentId, productNo) {
            const rating = $("#rating_" + commentId).val();
            const text = $("#text_" + commentId).val();
            const $div = $("#commentDisplay_" + commentId);
            const username = $div.data("username") || "使用者";

            $.post("updateComment.action", {
                commentId: commentId,
                rating: rating,
                commentText: text,
                productNo: productNo
            }).done(function () {
                $div.html(
                    "⭐ " + rating + " 顆星 - <strong>" + username + "</strong>：<br>" +
                    escapeHtml(text) + "<br>" +
                    "<small>🕓 剛剛</small><br>" +
                    '<button onclick="showEditForm(' + commentId + ')">編輯</button>' +
                    '<button onclick="deleteComment(' + commentId + ', ' + productNo + ')">刪除</button>'
                );
                cancelEdit(commentId);
            }).fail(function () {
                alert("更新失敗，請稍後再試");
            });

            return false;
        }

        function deleteComment(commentId, productNo) {
            if (!confirm("確定要刪除這則留言嗎？")) return;

            $.post("deleteComment.action", {
                commentId: commentId,
                productNo: productNo
            }).done(function () {
                $("#commentDisplay_" + commentId).html(
                    '<span style="color:gray; font-style:italic;">此留言已由使用者刪除</span>'
                );
            }).fail(function () {
                alert("刪除失敗，請稍後再試");
            });
        }

        function escapeHtml(str) {
            return str
                .replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;')
                .replace(/"/g, '&quot;');
        }
    </script>
</head>
<body>
<h2>商品詳情</h2>

<% if (product != null) { %>
<p><strong>商品名稱：</strong> <%= product.getProductName() %></p>
<p><strong>價格：</strong> $<%= product.getProductPrice() %></p>
<p><strong>描述：</strong> <%= escapeHtml(product.getProductDesc()) %></p>
<p><strong>剩餘庫存：</strong> <%= product.getRemainingQty() %></p>
<p><strong>狀態：</strong> <%= product.getProductStatus() == 1 ? "上架中" : "已下架" %></p>

<form action="addToCart.action" method="post">
    <input type="hidden" name="productNo" value="<%= product.getProductNo() %>">
    <label for="qty">數量：</label>
    <input type="number" id="qty" name="quantity" value="1" min="1" max="<%= product.getRemainingQty() %>">
    <button type="submit">加入購物車</button>
</form>

<hr>
<h3>評論區</h3>

<% if (comments != null && !comments.isEmpty()) { %>
<ul>
    <% for (ProductComment comment : comments) {
        int cid = comment.getCommentId();
        int status = comment.getStatus();
        boolean isOwner = loginMember != null &&
                comment.getMember() != null &&
                comment.getMember().getMemberId().equals(loginMember.getMemberId());
        String displayName = (comment.getMember() != null && comment.getMember().getUsername() != null)
                ? comment.getMember().getUsername()
                : (comment.getMember() != null ? comment.getMember().getName() : "匿名");
        String safeText = escapeHtml(comment.getCommentText());
    %>
    <li>
        <div id="commentDisplay_<%= cid %>" data-username="<%= displayName %>">
            <% if (status == 0) { %>
            <span style="color:gray; font-style:italic;">此留言已由使用者刪除</span>
            <% } else if (status == -1) { %>
            <span style="color:gray; font-style:italic;">此留言為不當言論</span>
            <% } else { %>
            ⭐ <%= comment.getRating() %> 顆星 - <strong><%= displayName %></strong>：<br>
            <%= safeText %><br>
            <small>🕓 <%= comment.getCommentTime().format(formatter) %></small><br>

            <% if (isOwner) { %>
            <button onclick="showEditForm(<%= cid %>)">編輯</button>
            <button onclick="deleteComment(<%= cid %>, <%= product.getProductNo() %>)">刪除</button>
            <% } else if (loginMember != null) {
                boolean alreadyReported = reportedCommentIds != null && reportedCommentIds.contains(cid);
                if (!alreadyReported) { %>
            <form method="post" onsubmit="return reportComment(<%= cid %>, this)" style="display:inline; margin-left: 10px;">
                <select name="reason" required>
                    <option value="">檢舉原因</option>
                    <option value="辱罵">辱罵</option>
                    <option value="廣告">廣告</option>
                    <option value="歧視">歧視</option>
                    <option value="其他">其他</option>
                </select>
                <button type="submit">檢舉</button>
            </form>
            <% } else { %>
            <span style="color:gray; margin-left: 10px;">您已檢舉</span>
            <% }
            } %>
            <% } %>
        </div>

        <% if (status == 1 && isOwner) { %>
        <div id="commentEdit_<%= cid %>" style="display:none;">
            <form method="post" onsubmit="return submitEdit(<%= cid %>, <%= product.getProductNo() %>)">
                <select id="rating_<%= cid %>" required>
                    <% for (int i = 5; i >= 1; i--) { %>
                    <option value="<%= i %>" <%= (i == comment.getRating()) ? "selected" : "" %>><%= i %> 顆星</option>
                    <% } %>
                </select><br>
                <textarea id="text_<%= cid %>" rows="3" cols="40" required><%= safeText %></textarea><br>
                <button type="submit">儲存</button>
                <button type="button" onclick="cancelEdit(<%= cid %>)">取消</button>
            </form>
        </div>
        <% } %>
    </li>
    <% } %>
</ul>
<% } else { %>
<p>目前尚無評論。</p>
<% } %>

<% if (loginMember != null) { %>
<hr>
<h4>發表評論</h4>
<form action="addProductComment.action" method="post">
    <input type="hidden" name="productNo" value="<%= product.getProductNo() %>">
    <p>
        評分：
        <select name="rating" required>
            <option value="">請選擇</option>
            <option value="5">5 顆星</option>
            <option value="4">4 顆星</option>
            <option value="3">3 顆星</option>
            <option value="2">2 顆星</option>
            <option value="1">1 顆星</option>
        </select>
    </p>
    <p>
        評論內容：<br>
        <textarea name="commentText" rows="4" cols="40" required></textarea>
    </p>
    <button type="submit">送出評論</button>
</form>
<% } else { %>
<p>請先 <a href="login.action">登入</a> 才能留言。</p>
<% } %>

<% } else { %>
<p>查無此商品。</p>
<% } %>

<br>
<a href="productList.action">← 回商品列表</a>
</body>
</html>
