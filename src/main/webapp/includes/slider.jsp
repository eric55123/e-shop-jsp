<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Start Slider Area -->
<div class="slider__container slider--one">
    <div class="slider__activation__wrap owl-carousel owl-theme">

        <!-- 輪播圖：整張圖片可點，連到 /cart.action -->
        <c:forEach var="i" begin="1" end="5">
            <a href="#main">
                <div class="slide slider__full--screen"
                     style="background-image: url('<c:url value="/images/slider_${i}.png" />');">
                </div>
            </a>
        </c:forEach>
    </div>
</div>

<!-- End Slider Area -->

<!-- ✅ Owl Carousel 初始化 JS（建議放在頁面底部） -->
<link rel="stylesheet" href="<c:url value='/template/css/owl.carousel.min.css' />" />
<link rel="stylesheet" href="<c:url value='/template/css/owl.theme.default.min.css' />" />
<script src="<c:url value='/template/js/owl.carousel.min.js' />"></script>

<script>
    $(document).ready(function () {
        $(".slider__activation__wrap").owlCarousel({
            items: 1,
            loop: true,
            autoplay: true,
            autoplayTimeout: 4000,
            smartSpeed: 800,
            animateOut: 'fadeOut',
            dots: false
        });
    });
</script>

<!-- ✅ 建議額外放到 style.css 中的基本樣式（可複製） -->
<style>
    /* 🔽 輪播圖片區塊樣式 - 顯示完整圖片用 contain 模式 */
    .slider__full--screen {
        height: 800px; /* 依圖片高度需求調整，例如 800px */
        width: 100%;
        background-size: cover !important;       /* ✅ 填滿整個容器 */
        background-position: center center !important;
        background-repeat: no-repeat !important;
        background-color: #000;
        position: relative;
    }

    /* 🔽 輪播中文字區塊置中 */
    .slider__inner {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        color: white;
        text-align: center;
        z-index: 10;
    }

    /* 🔽 標題樣式 */
    .slider__inner h1 {
        font-size: 48px;
        font-weight: bold;
        line-height: 1.3;
    }

    /* 🔽 按鈕樣式 */
    .slider__btn .htc__btn {
        background: #e74c3c;
        color: white;
        padding: 12px 30px;
        font-weight: bold;
        display: inline-block;
        margin-top: 20px;
        text-transform: uppercase;
        border: none;
        border-radius: 4px;
        transition: background 0.3s ease;
    }
    .slider__btn .htc__btn:hover {
        background: #c0392b;
    }

    /* 🔽 左右箭頭樣式 */
    .owl-nav {
        position: absolute;
        top: 50%;
        width: 100%;
        transform: translateY(-50%);
        display: flex;
        justify-content: space-between;
        pointer-events: none;
    }
    .owl-nav .owl-prev,
    .owl-nav .owl-next {
        background: rgba(0, 0, 0, 0.5);
        color: white;
        font-size: 32px;
        padding: 10px 18px;
        border-radius: 50%;
        cursor: pointer;
        pointer-events: all;
        transition: background 0.3s;
    }
    .owl-nav .owl-prev:hover,
    .owl-nav .owl-next:hover {
        background: rgba(0, 0, 0, 0.8);
    }

    /* 🔽 響應式：手機畫面自動調整高度 */
    @media (max-width: 768px) {
        .slider__full--screen {
            height: 50vw; /* 可根據寬度自動算高度 */
        }
        .slider__inner h1 {
            font-size: 28px;
        }
        .slider__btn .htc__btn {
            padding: 8px 20px;
        }
    }

    html {
        scroll-behavior: smooth;
    }

</style>
