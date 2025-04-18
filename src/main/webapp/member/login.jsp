<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<jsp:include page="/includes/header.jsp" />

<!-- ✅ 登入區塊 -->
<div class="container">
    <section id="main">
        <div class="content">
            <div class="pag-nav">
                <ul class="p-list">
                    <li><a href="productList.action">Home</a></li> &nbsp;&nbsp;/&nbsp;
                    <li class="act">&nbsp;Login</li>
                </ul>
            </div>

            <div class="contact-box login-box">
                <h3 class="c-head text-center">會員登入</h3>

                <s:form action="login" method="post" cssClass="needs-validation" onsubmit="return validateLogin()">
                    <div class="form-group mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="text" name="email" id="email" class="form-control" value="${email}" />
                    </div>

                    <div class="form-group mb-3">
                        <label for="password" class="form-label">密碼</label>
                        <input type="password" name="password" id="password" class="form-control" value="${password}" />
                    </div>

                    <div class="form-group d-flex justify-content-center">
                        <s:submit value="登入" cssClass="btn btn-dark px-4" />
                    </div>
                </s:form>

                <!-- ✅ Google 登入與註冊連結 -->
                <div class="text-center mt-4">
                    <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=29437975110-6rosalgpp5a2ncocs3cfgoqshiq182dt.apps.googleusercontent.com&redirect_uri=http://localhost:8080/googleLoginCallback.action&response_type=code&scope=openid%20email%20profile">
                        <button class="btn btn-danger">使用 Google 登入</button>
                    </a>
                </div>
                <div class="text-center mt-2">
                    <a href="memberRegister.action">還沒註冊？馬上註冊</a>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/includes/footer.jsp" />
