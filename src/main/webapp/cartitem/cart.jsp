<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.math.BigDecimal" %>
<%@ page import="com.eshop.product.model.Product" %>
<%@ page import="com.eshop.cartitem.model.CartItem" %>
<%@ page import="com.eshop.member.model.Member" %>
<%@ page import="com.eshop.member.model.MemberAddress" %>
<%
    Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
    BigDecimal discount = (BigDecimal) session.getAttribute("discount");
    String appliedCouponCode = (String) session.getAttribute("appliedCouponCode");
    Member loggedInMember = (Member) session.getAttribute("loginMember");
    MemberAddress addr = (MemberAddress) session.getAttribute("memberAddress");
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>購物車 | Shade Template</title>
    <link href="<%=request.getContextPath()%>/template/css/bootstrap.css" rel="stylesheet" />
    <script src="<%=request.getContextPath()%>/template/js/jquery-1.11.0.min.js"></script>
    <link href="<%=request.getContextPath()%>/template/css/style.css" rel="stylesheet" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/template/css/sticky-navigation.css" />
    <link href="<%=request.getContextPath()%>/template/css/demo.css" rel="stylesheet" />
    <script>
        function doubleConfirmClear() {
            return confirm('確定要清空購物車嗎？') && confirm('此操作將移除所有商品，確定繼續？');
        }
    </script>
</head>
<body>

<!-- ✅ 導覽列 -->
<div class="header">
    <div class="container">
        <div id="demo_top_wrapper">
            <div id="sticky_navigation_wrapper">
                <div id="sticky_navigation">
                    <div class="demo_container navigation-bar">
                        <div class="navigation">
                            <div class="logo"><a href="productList.action">eShop</a></div>
                            <span class="menu"></span>
                            <script>
                                $("span.menu").click(function() {
                                    $(".navig").slideToggle("slow");
                                });
                            </script>
                            <div class="navig">
                                <ul>
                                    <li><a href="women.action">Woman</a></li>
                                    <li><a href="men.action">Men</a></li>
                                    <li><a href="#">Kids</a></li>
                                    <li><a href="#">即將到來</a></li>
                                    <li><a href="faqList.action">常見問題</a></li>
                                </ul>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <div class="navigation-right">
                            <ul class="user">
                                <li><span></span><a href="login.action">Log In</a></li>
                                <li><span class="cart"></span><a href="cart.action">購物車</a></li>
                                <li><button class="search"></button></li>
                            </ul>
                        </div>
                        <div class="clearfix"></div>
                        <div class="serch">
                            <span>
                                <input type="text" placeholder="Search" required="">
                                <input type="submit" value="" />
                            </span>
                        </div>
                        <script>
                            $("button.search").click(function() {
                                $(".serch").slideToggle("slow");
                            });
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ✅ 主畫面 -->
<div class="container">
    <section id="main">
        <div class="content">
            <h3 class="c-head text-center">🛒 您的購物車</h3>

            <% if (cart == null || cart.isEmpty()) { %>
            <div class="alert alert-info">目前購物車是空的。</div>
            <% } else { %>
            <table class="table table-bordered text-center align-middle">
                <thead class="table-light">
                <tr class="cart-row">
                    <th>圖片</th>
                    <th>商品名稱</th>
                    <th>單價</th>
                    <th>數量</th>
                    <th>小計</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%
                    BigDecimal total = BigDecimal.ZERO;
                    for (CartItem item : cart.values()) {
                        Product p = item.getProduct();
                        int qty = item.getQuantity();
                        BigDecimal subtotal = p.getProductPrice().multiply(new BigDecimal(qty));
                        total = total.add(subtotal);
                %>
                <tr class="cart-row">
                    <td>
                        <% if (p.getCoverImageUrl() != null && p.getCoverImageUrl().contains("id=")) { %>
                        <img src="https://drive.google.com/thumbnail?id=<%= p.getCoverImageUrl().split("id=")[1] %>"
                             width="80" class="img-thumbnail" />
                        <% } else { %>
                        <span class="text-muted">無圖片</span>
                        <% } %>
                    </td>
                    <td><%= p.getProductName() %></td>
                    <td>$<%= p.getProductPrice() %></td>
                    <td>

                        <div class="d-flex justify-content-center">
                            <input type="number" name="quantity" class="form-control qty-input w-50" data-product="<%= p.getProductNo() %>"
                                   data-price="<%= p.getProductPrice() %>" value="<%= qty %>" min="0" max="<%= p.getRemainingQty() %>">
                        </div>

                    </td>
                    <td class="subtotal">$0.00</td>
                    <td>
                        <form action="removeFromCart.action" method="post">
                            <input type="hidden" name="productNo" value="<%= p.getProductNo() %>" />
                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('確定要移除這個商品嗎？')">移除</button>
                        </form>
                    </td>
                </tr>
                <% } %>
                <tr class="cart-row">
                    <td colspan="4" class="text-end"><strong>總計：</strong></td>
                    <td colspan="2" id="cart-total">$0.00</td>
                </tr>
                </tbody>
            </table>

            <!-- ✅ 優惠券選單 -->
            <form action="applyCoupon.action" method="post" class="mt-4">
                <label for="couponCode">選擇優惠券：</label>
                <select name="couponCode" id="couponCode" class="form-select w-50 d-inline-block" required>
                    <option value="">請選擇優惠券</option>
                    <%
                        List<com.eshop.coupon.model.CouponHolder> availableCoupons =
                                (List<com.eshop.coupon.model.CouponHolder>) session.getAttribute("availableCoupons");

                        if (availableCoupons != null) {
                            for (com.eshop.coupon.model.CouponHolder ch : availableCoupons) {
                                String code = ch.getCouponCode();
                                String name = ch.getCoupon().getName();
                    %>
                    <option value="<%= code %>" <%= code.equals(appliedCouponCode) ? "selected" : "" %>><%= name %></option>
                    <%      }
                    }
                    %>
                </select>
                <button type="submit" class="btn btn-success">套用優惠券</button>
            </form>

            <% if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) { %>
            <div class="alert alert-success mt-3">
                ✅ 已套用優惠券：<strong><%= appliedCouponCode %></strong><br>
                ✅ 折扣金額：<strong>$<%= discount %></strong><br>
                <strong>優惠後總金額：</strong> $<%= total.subtract(discount) %>
            </div>
            <form action="removeCoupon.action" method="post">
                <button type="submit" class="btn btn-outline-danger btn-sm">取消優惠券</button>
            </form>
            <% } %>

            <!-- ✅ 收件資訊 -->
            <hr />
            <% if (loggedInMember != null) { %>
            <h4>填寫收件資訊</h4>
            <form action="checkout.action" method="post">
                <div class="mb-2">
                    <label>收件人姓名：</label>
                    <input type="text" name="receiverName" class="form-control" required
                           value="<%= addr != null ? addr.getRecipientName() :
                                       (loggedInMember.getName() != null && !loggedInMember.getName().trim().isEmpty()
                                       ? loggedInMember.getName()
                                       : loggedInMember.getUsername()) %>" />
                </div>
                <div class="mb-2">
                    <label>收件人電話：</label>
                    <input type="text" name="receiverPhone" class="form-control" required
                           value="<%= addr != null ? addr.getRecipientPhone() : loggedInMember.getPhone() %>" />
                </div>
                <div class="mb-2">
                    <label>收件人地址：</label>
                    <input type="text" name="receiverAddress" class="form-control" required
                           value="<%= addr != null ? addr.getAddress() : "" %>" />
                </div>
                <div class="mb-2">
                    <label>備註：</label>
                    <textarea name="note" class="form-control" rows="3"></textarea>
                </div>
                <div class="mb-3 form-check">
                    <input type="checkbox" name="saveAddress" class="form-check-input" value="true" <%= addr != null ? "checked" : "" %>>
                    <label class="form-check-label">記住此地址</label>
                </div>
                <button type="submit" class="btn btn-warning">✅ 確認送出訂單</button>
            </form>
            <% } else { %>
            <div class="alert alert-warning">
                🔒 尚未登入，請先登入才能結帳。
            </div>
            <% } %>

            <!-- ✅ 清空購物車 -->
            <form action="clearCart.action" method="post" onsubmit="return doubleConfirmClear();" class="mt-4">
                <button type="submit" class="btn btn-danger">🗑 清空購物車</button>
            </form>
            <% } %>

            <div class="mt-4">
                <a href="productList.action">← 回商品列表</a>
            </div>
        </div>
    </section>
</div>

<script>
    $(document).ready(function () {
        function updateCartSummary() {
            let total = 0;
            $(".cart-row").each(function () {
                const qty = parseInt($(this).find(".qty-input").val()) || 0;
                const price = parseFloat($(this).find(".qty-input").data("price")) || 0;
                const subtotal = qty * price;
                $(this).find(".subtotal").text("$" + subtotal.toFixed(2));
                total += subtotal;
            });
            $("#cart-total").text("$" + total.toFixed(2));
        }

        $(".qty-input").on("input", function () {

            updateCartSummary();

            // 🚀 同步更新後端購物車資料
            const productNo = $(this).data("product");
            const quantity = $(this).val();
            $.post("updateCart.action", {
                productNo: productNo,
                quantity: quantity
            });
        });


        updateCartSummary();
    });
</script>
</body>
</html>
