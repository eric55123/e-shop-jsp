<!-- 🎯 You May Also Like 區塊 -->
<div class="look">
  <h3>You May Also Like</h3>
</div>

<div class="partner">
  <ul id="flexiselDemo3">
    <li><img src="<%=request.getContextPath()%>/template/images/ss1.jpg" class="img-responsive" alt=""/></li>
    <li><img src="<%=request.getContextPath()%>/template/images/ss2.jpg" class="img-responsive" alt=""/></li>
    <li><img src="<%=request.getContextPath()%>/template/images/ss3.jpg" class="img-responsive" alt=""/></li>
    <li><img src="<%=request.getContextPath()%>/template/images/ss4.jpg" class="img-responsive" alt=""/></li>
    <li><img src="<%=request.getContextPath()%>/template/images/ss5.png" class="img-responsive" alt=""/></li>
  </ul>
</div>

<!-- ✅ 輪播功能 JS 初始化 -->
<script src="<%=request.getContextPath()%>/template/js/jquery.flexisel.js"></script>
<script>
  $(window).on("load", function () {
    $("#flexiselDemo3").flexisel({
      visibleItems: 5,
      animationSpeed: 1000,
      autoPlay: true,
      autoPlaySpeed: 3000,
      pauseOnHover: true,
      enableResponsiveBreakpoints: true,
      responsiveBreakpoints: {
        portrait: { changePoint: 480, visibleItems: 1 },
        landscape: { changePoint: 640, visibleItems: 2 },
        tablet: { changePoint: 768, visibleItems: 3 }
      }
    });
  });
</script>

<!-- 🎯 Footer 區塊 -->
<div class="footer" style="background-color: #2e2e2e; color: #f0f0f0; padding: 40px 0; margin-top: 50px;">
  <div class="row footer-row" style="max-width: 1200px; margin: auto;">
    <div class="col-md-3 footer-col">
      <h4 class="ft-title" style="color: #fff;">Collection</h4>
      <ul class="ft-list" style="color: #ddd;">
        <li><a href="#" style="color: #ccc;">Woman (1725)</a></li>
        <li><a href="#" style="color: #ccc;">Men (635)</a></li>
        <li><a href="#" style="color: #ccc;">Kids (2514)</a></li>
        <li><a href="#" style="color: #ccc;">Coming Soon (76)</a></li>
      </ul>
    </div>
    <div class="col-md-3 footer-col">
      <h4 class="ft-title" style="color: #fff;">Site</h4>
      <ul class="ft-list list-h" style="color: #ddd;">
        <li><a href="#" style="color: #ccc;">Terms of Service</a></li>
        <li><a href="#" style="color: #ccc;">Privacy Policy</a></li>
        <li><a href="#" style="color: #ccc;">Copyright Policy</a></li>
        <li><a href="#" style="color: #ccc;">Press Kit</a></li>
        <li><a href="#" style="color: #ccc;">Support</a></li>
      </ul>
    </div>
    <div class="col-md-3 footer-col">
      <h4 class="ft-title" style="color: #fff;">Shop</h4>
      <ul class="ft-list list-h" style="color: #ddd;">
        <li><a href="#" style="color: #ccc;">About Us</a></li>
        <li><a href="#" style="color: #ccc;">Shipping Methods</a></li>
        <li><a href="#" style="color: #ccc;">Career</a></li>
        <li><a href="contact.jsp" style="color: #ccc;">Contact</a></li>
      </ul>
    </div>
    <div class="col-md-3 foot-cl">
      <h4 class="ft-title" style="color: #fff;">Social</h4>
      <p style="color: #ccc;">eShop is made with ❤️ in Taiwan.<br>2025 &copy; All rights reserved.</p>
      <ul class="social" style="list-style: none; display: flex; gap: 10px; padding-left: 0;">
        <li><i class="fa" style="color: #ccc;"></i></li>
        <li><i class="tw" style="color: #ccc;"></i></li>
        <li><i class="is" style="color: #ccc;"></i></li>
      </ul>
    </div>
    <div class="clearfix"></div>
  </div>
</div>
