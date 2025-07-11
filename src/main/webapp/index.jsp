<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>电子商城销售管理系统</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="./assets/css/base.css">
    <link rel="stylesheet" href="./assets/css/header.css">
    <link rel="stylesheet" href="./assets/css/main-card.css">
    <link rel="stylesheet" href="./assets/css/sidebar.css">
    <link rel="stylesheet" href="./assets/css/footer.css">
    <link rel="stylesheet" href="./assets/css/slider.css">
    <link rel="stylesheet" href="./assets/css/modal.css">
    <link rel="stylesheet" href="./assets/css/click-appears.css">
    <link rel="stylesheet" href="./assets/css/product-posts.css">
    <link rel="stylesheet" href="./assets/css/orders.css">
    <link rel="stylesheet" href="./assets/css/search.css">

</head>
<body>
    <!--Header-->
    <header class="header">
        <div class="container header-container">
            <!--Logo-->
            <a href="./index.jsp" class="logo">
                <img src="./assets/images/logo.webp" alt="Logo">
                <span class="logo-text">电子商城销售管理系统</span>
            </a>

            <!-- Desktop Navigation -->
            <nav class="nav">
                <ul class="nav-list">
                    <li><a href="javascript:void(0)" class="nav-link active" data-category="all">首页</a></li>
                    <li><a href="javascript:void(0)" class="nav-link" data-category="数码">数码</a></li>
                    <li><a href="javascript:void(0)" class="nav-link" data-category="家电">家电</a></li>
                    <li><a href="javascript:void(0)" class="nav-link" data-category="家具">家具</a></li>
                    <li><a href="javascript:void(0)" class="nav-link" data-category="服装">服装</a></li>
                    <li><a href="javascript:void(0)" class="nav-link" data-category="美妆">美妆</a></li>
                </ul>
            </nav>


            <!-- Header Actions -->
            <div class="header-actions">
                <button class="icon-btn user-btn" aria-label="个人中心" id="login-text">
                        登录
                </button>
                <button class="icon-btn search-btn" aria-label="搜索">
                    <i class="fas fa-search"></i>
                </button>
                <button class="icon-btn theme-toggle" aria-label="切换主题">
                    <i class="fas fa-sun"></i>
                </button>
                <button class="icon-btn menu-btn" aria-label="菜单">
                    <i class="fas fa-bars"></i>
                </button>
            </div>            
        </div>
    </header>

    <!--Mobile Menu-->
    <div class="overlay"></div>
    <div class="mobile-menu">
        <div class="mobile-menu-header">
            <h3>菜单</h3>
            <button class="icon-btn close-menu-btn">
                <i class="fas fa-times"></i>
            </button>
        </div>

        <!-- 用户信息卡片 -->
        <div class="mobile-card">
            <div class="user-card-content">
                <div class="user-header">
                    <img id="mobile-sidebar-avatar" src="./assets/images/avatars/OIP-C.webp" alt="Profile" class="user-avatar">
                    <h3 id="mobile-sidebar-username" class="user-name">访客</h3>
                </div>
                <div class="user-actions" id="mobile-user-actions">
                    <!-- 这里的按钮将根据登录状态动态显示 -->
                </div>
            </div>
        </div>

        <!-- 购物车卡片 -->
        <div class="mobile-card">
            <div class="card-content">
                <h3 class="section-title"><i class="fas fa-shopping-cart"></i> 购物车</h3>
                <ul class="shopping-list mobile-shopping-list">
                    <!-- 购物车内容将通过JavaScript动态生成 -->
                </ul>
                <div class="checkout-container">
                    <div class="total-amount">
                        <span>总金额:</span>
                        <span class="price">¥<span id="mobile-cart-total">0.00</span></span>
                    </div>
                    <button id="mobile-checkout-btn" class="checkout-btn">
                        <i class="fas fa-shopping-cart"></i> 结算
                    </button>
                </div>
            </div>
        </div>
    </div>


    <!-- 登录模态框 -->
    <div id="login-modal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <h2>登录</h2>
            <form method="post" id="login-form">
                <div class="form-group">
                    <div class="label-group">
                        <label for="">邮箱</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    <div class="label-group">
                        <label for="password">密码</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                </div>

                <div id="login-message" class="message"></div>
                <div class="button-group">
                    <button type="button" id="register-btn">注册</button>
                    <button type="button" id="login-submit-btn">登录</button>
                </div>
                
            </form>
        </div>
    </div>

    <!-- 注册模态框 -->
    <div id="register-modal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <h2>注册</h2>
            <form method="post" id="register-form">
                <div class="form-group">
                    <div class="label-group">
                        <label for="new-username">用户名 </label>
                        <input type="text" id="new-username" name="new-username" required>
                    </div>
                    <div class="label-group">
                        <label for="new-email">邮 箱</label>
                        <input type="email" id="new-email" name="new-email" required>
                    </div>
                    <div class="label-group">
                        <label for="verification-code">验证码</label>
                        <input type="text" id="verification-code" name="verification-code" required>
                        <button type="button" id="send-code-btn" class="btn-send-code">发送验证码</button>
                    </div>
            
                    <div class="label-group">
                        <label for="new-password">密 码 </label>
                        <input type="password" id="new-password" name="new-password" required>
                    </div>
                    <div class="label-group">
                        <label for="confirm-password">确认密码</label>
                        <input type="password" id="confirm-password" name="confirm-password" required>
                    </div>

                </div>
                <div style="color: red" id="register-error-msg"></div>
                <div class="button-group">
                    <button type="button" id="back-to-login-btn">返回登录</button>
                    <button type="button" id="register-submit-btn">注册</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 结算模态框 -->
    <div id="checkout-modal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <h2>订单结算</h2>
            <div id="checkout-items-container">
                <h3>商品清单</h3>
                <div class="checkout-items">
                    <!-- 商品列表将通过 JavaScript 动态生成 -->
                </div>
            </div>
            <div class="checkout-summary">
                <div class="checkout-total">
                    <span>总金额:</span>
                    <span class="price">¥<span id="checkout-total">0.00</span></span>
                </div>
                <div class="checkout-address">
                    <h3>收货地址</h3>
                    <div class="form-group">
                        <div class="label-group">
                            <label for="recipient-name">收件人</label>
                            <textarea type="text" id="recipient-name" name="recipient-name" required></textarea>
                        </div>
                        <div class="label-group">
                            <label for="phone">联系电话</label>
                            <textarea type="text" id="phone" name="phone" required></textarea>
                        </div>
                        <div class="label-group">
                            <label for="address">详细地址</label>
                            <textarea id="address" name="address" required></textarea>
                        </div>
                    </div>
                </div>
                <div class="payment-method">
                    <h3>支付方式</h3>
                    <div class="payment-options">
                        <label>
                            <input type="radio" name="payment" value="alipay" checked> 支付宝
                        </label>
                        <label>
                            <input type="radio" name="payment" value="wechat"> 微信支付
                        </label>
                        <label>
                            <input type="radio" name="payment" value="cod"> 货到付款
                        </label>
                    </div>
                </div>
            </div>
            <div id="checkout-message" class="message"></div>
            <div class="button-group">
                <button type="button" id="cancel-checkout-btn">取消</button>
                <button type="button" id="confirm-checkout-btn">确认订单</button>
            </div>
        </div>
    </div>

    <!-- 订单模态框 -->
    <div id="orders-modal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <h2>我的订单</h2>
            <div class="orders-container">
                <!-- 订单列表将通过JavaScript动态生成 -->
                <div id="orders-list"></div>
            </div>
            <div id="orders-message" class="message"></div>
        </div>
    </div>

    <!-- 修改个人信息模态框 -->
    <div id="profile-modal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <h2>修改个人信息</h2>
            <form id="profile-form" enctype="multipart/form-data">
                <div class="form-group">
                    <div class="avatar-upload">
                        <label for="avatar-upload">头像</label>
                        <div class="avatar-preview">
                            <img id="avatar-preview" src="./assets/images/avatars/OIP-C.webp" alt="头像预览">
                        </div>
                        <input type="file" id="avatar-upload" name="avatar" accept="image/*">
                    </div>
                    <div class="label-group">
                        <label for="update-username">用户名</label>
                        <input type="text" id="update-username" name="username" required>
                    </div>
                    <div class="label-group">
                        <label for="current-password">当前密码</label>
                        <input type="password" id="current-password" name="currentPassword">
                    </div>
                    <div class="label-group">
                        <label for="new-profile-password">新密码</label>
                        <input type="password" id="new-profile-password" name="newPassword">
                    </div>
                    <div class="label-group">
                        <label for="confirm-profile-password">确认新密码</label>
                        <input type="password" id="confirm-profile-password" name="confirmPassword">
                    </div>
                </div>
                <div id="profile-message" class="message"></div>
                <div class="button-group">
                    <button type="button" id="cancel-profile-btn">取消</button>
                    <button type="button" id="save-profile-btn">保存修改</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 搜索模态框 -->
    <div id="search-modal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <h2>搜索商品</h2>
            <div class="search-form">
                <input type="text" id="search-input" placeholder="输入商品名称...">
                <button type="button" id="search-submit-btn">搜索</button>
            </div>
            <div id="search-message" class="message"></div>
        </div>
    </div>


    <main class="main">
        <div class="main-card">
            <!-- Slider -->
            <div class="slider">
                <div class="slide active">
                    <img src="./assets/images/slider/数码.png" alt="数码" class="slide-img">
                    <div class="slide-content">
                        <a href="javascript:void(0)" class="nav-link" data-category="数码"><h2 class="slide-title">数码</h2></a>
                        <a href="javascript:void(0)" class="nav-link" data-category="数码"><p class="slide-subtitle">探索科技前沿，享受智能生活</p></a>
                    </div>
                </div>
                
                <div class="slide">
                    <img src="./assets/images/slider/家电.png" alt="家电" class="slide-img">
                    <div class="slide-content">
                        <a href="javascript:void(0)" class="nav-link" data-category="家电"><h2 class="slide-title">家电</h2></a>
                        <a href="javascript:void(0)" class="nav-link" data-category="家电"><p class="slide-subtitle">品质家电，让生活更便捷</p></a>
                    </div>
                </div>
                
                <div class="slide">
                    <img src="./assets/images/slider/家具.png" alt="家具" class="slide-img">
                    <div class="slide-content">
                        <a href="javascript:void(0)" class="nav-link" data-category="家具"><h2 class="slide-title">家具</h2></a>
                        <a href="javascript:void(0)" class="nav-link" data-category="家具"><p class="slide-subtitle">温馨舒适，打造理想居所</p></a>
                    </div>
                </div>
                
                <div class="slide">
                    <img src="./assets/images/slider/服装.png" alt="服装" class="slide-img">
                    <div class="slide-content">
                        <a href="javascript:void(0)" class="nav-link" data-category="服装"><h2 class="slide-title">服装</h2></a>
                        <a href="javascript:void(0)" class="nav-link" data-category="服装"><p class="slide-subtitle">时尚穿搭，彰显个性风采</p></a>
                    </div>
                </div>

                <div class="slide">
                    <img src="./assets/images/slider/美妆.png" alt="美妆" class="slide-img">
                    <div class="slide-content">
                        <a href="javascript:void(0)" class="nav-link" data-category="美妆"><h2 class="slide-title">美妆</h2></a>
                        <a href="javascript:void(0)" class="nav-link" data-category="美妆"><p class="slide-subtitle">精致妆容，绽放自信魅力</p></a>
                    </div>
                </div>
                
                <!-- Slider Controls -->
                <button class="slider-control slider-prev">
                <i class="fas fa-chevron-left"></i>
                </button>
                <button class="slider-control slider-next">
                <i class="fas fa-chevron-right"></i>
                </button>
                
                <!-- Slider Dots -->
                <div class="slider-dots">
                <span class="slider-dot active" data-index="0"></span>
                <span class="slider-dot" data-index="1"></span>
                <span class="slider-dot" data-index="2"></span>
                <span class="slider-dot" data-index="3"></span>
                <span class="slider-dot" data-index="4"></span>
                </div>
            </div>

            
            <!-- product Posts -->
            <div class="product-section">
                <h2 class="product-section-title"><i class="fas fa-pen"></i>所有商品</h2>

                <!-- 商品展示部分 -->
                <div class="product-container">
                    <c:forEach var="product" items="${products}">
                        <div class="product-card" data-category="${product.category}">
                            <img src="${product.image_url}" alt="${product.product_name}" class="product-image">
                            <div class="product-info">
                                <h4 class="product-name">${product.product_name}</h4>
                                <div class="product-card-footer">
                                    <p class="product-price">¥${product.product_price}</p>
                                    <span class="product-category" style="display:none;">${product.category}</span>
                                    <div class="cart-controls">
                                        <button class="cart-btn add-to-cart" data-id="${product.product_id}">
                                            <i class="fas fa-cart-plus"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

            </div>
        </div>

        <!-- Sidebar-->
        <aside class="sidebar">
            <!--user Card-->
            <div class="card">
                <div class="user-card-content">
                    <div class="user-header">
                        <img id="sidebar-avatar" src="./assets/images/avatars/OIP-C.webp" alt="Profile" class="user-avatar">
                        <h3 id="sidebar-username" class="user-name">𝓣𝓼°𝓒 𝓢𝓱𝓪𝓭𝓸𝔀</h3>
                    </div>
                    <div class="user-actions" id="user-actions">
                        <!-- 这里的按钮将根据登录状态动态显示 -->
                    </div>
                    <div class="user-actions" id="user-profile-actions">
                        <!-- 这里的按钮将根据登录状态动态显示 -->
                        <button id="edit-profile-btn" class="user-action-btn">
                            <i class="fas fa-user-edit"></i> 修改个人信息
                        </button>
                    </div>
                </div>
            </div>


            <div class="card">
                <div class="card-content">
                    <h3 class="section-title"><i class="fas fa-shopping-cart"></i> 购物车</h3>
                    <ul class="shopping-list">
                        <c:if test="${empty cartItems}">
                            <li class="shopping-item">购物车为空</li>
                        </c:if>
                        <c:forEach var="item" items="${cartItems}">
                            <li class="shopping-item">
                                <div class="item-info">
                                    <a href="./product?id=${item.product_id}" class="shopping-link">${item.product_name}</a>
                                    <span class="quantity-badge">${item.quantity}</span>
                                    <span class="price-badge"></span>
                                </div>
                                <div class="quantity-controls">
                                    <button class="quantity-btn decrease-btn" data-id="${item.product_id}">
                                        <i class="fas fa-minus fa-xs"></i> 
                                    </button>
                                    <span class="item-quantity">${item.quantity}</span>
                                    <button class="quantity-btn increase-btn" data-id="${item.product_id}">
                                        <i class="fas fa-plus fa-xs"></i>
                                    </button>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                    <div class="checkout-container">
                        <div class="total-amount">
                            <span>总金额:</span>
                            <span class="price">¥<span id="cart-total">0.00</span></span>
                        </div>
                        <button id="checkout-btn" class="checkout-btn">
                            <i class="fas fa-shopping-cart"></i> 结算
                        </button>
                    </div>
                </div>
            </div>
        </aside>
    </main>

    <div style="height: 100px;"></div>
    <!--Footer-->
    <footer class="footer">
        <div class="container footer-container">
            <div class="footer-info">
                <p>2025 © <a href="/" class="footer-link">电子商城销售管理系统</a> - 
                    <a href="https://beian.miit.gov.cn/" 
                    target="_blank" 
                    rel="noopener noreferrer" 
                    class="footer-link">
                        闽ICP备2025xxxxxx号-1
                    </a>
                </p>
                <p>This is a courseworkDesign</p>
            </div>
            <div>
                <div class="footer-links">
                    <a href="./sitemap.xml"
                    target="_blank"
                    rel="noopener noreferrer"
                    class="footer-link">
                        站点地图
                    </a>
                </div>
                <div class="footer-stats">
                    <span>访问量：1</span>
                    <span>访客量：1</span>
                </div>
            </div>
        </div>
    </footer>
    <script>
        window.baseUrl = './';  // 根目录不需要前缀
    </script>
    <script src="vendors/jquery-3.6.0.js"></script>
    <script src="assets/js/scripts.js"></script>
    <script src="assets/js/slider.js"></script>
    <script src="assets/js/click-appears.js"></script>
    <script src="assets/js/login_register.js"></script>
    <script src="assets/js/shopping_cart.js"></script>
    <script src="assets/js/checkout.js"></script>
    <script src="assets/js/category.js"></script>
    <script src="assets/js/orders.js"></script>
    <script src="assets/js/profile.js"></script>
    <script src="assets/js/search.js"></script>
</body>
</html>