<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
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
    <link rel="stylesheet" href="./assets/css/product.css">
    <link rel="stylesheet" href="./assets/css/product-posts.css">

</head>
<body>
    <!--Header-->
    <header class="header">
        <div class="container header-container">
            <!--Logo-->
            <a href="/" class="logo">
                <img src="./assets/images/logo.webp" alt="Logo">
                <span class="logo-text">电子商城销售管理系统</span>
            </a>

            <!-- Desktop Navigation -->
            <nav class="nav">
                <ul class="nav-list">
                    <li><a href="./index.html" class="nav-link active">首页</a></li>
                    <li><a href="./pages/board/index.html" class="nav-link">数码</a></li>
                    <li><a href="./pages/moments/index.html" class="nav-link">家电</a></li>
                    <li><a href="./pages/moments/index.html" class="nav-link">家具</a></li>
                    <li><a href="./pages/moments/index.html" class="nav-link">服装</a></li>
                    <li><a href="./pages/moments/index.html" class="nav-link">美妆</a></li>
                </ul>
            </nav>

            <!-- Header Actions -->
            <div class="header-actions">
                <button class="icon-btn user-btn" aria-label="个人中心">
                    <img id="user-avatar" src="" alt="用户头像" style="display: none;">
                    <div class="user-login-container">
                        <span id="login-text">登录</span>
                    </div>
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
    <div class=" overlay"></div>
    <div class="mobile-menu">
        <div class="mobile-menu-header">
            <h3>菜单</h3>
            <button class="icon-btn close-menu-btn">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <ul class="mobile-menu-list">
            <li class="mobile-menu-item"><a href="./index.html" class="mobile-menu-link">首页</a></li>
            <li class="mobile-menu-item"><a href="./pages/board/index.html" class="mobile-menu-link">收藏夹</a></li>
            <li class="mobile-menu-item"><a href="./pages/moments/index.html" class="mobile-menu-link">购物车</a></li>
        </ul>
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
                <!-- 替换这两行 -->
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


    <main class="main">
        <div class="main-card">
            <!-- Slider -->
            <div class="slider">
                <div class="slide active">
                    <img src="./assets/images/album/时光小憩.JPG" alt="时光小憩" class="slide-img">
                    <div class="slide-content">
                        <h2 class="slide-title"></h2>
                        <p class="slide-subtitle"></p>
                        <a href="/" class="slide-link"><i class="fas fa-arrow-right"></i></a>
                    </div>
                </div>
                
                <div class="slide">
                    <img src="./assets/images/album/IMG_20240210_174111.jpg" alt="螺洲古镇日落" class="slide-img">
                    <div class="slide-content">
                        <h2 class="slide-title"></h2>
                        <p class="slide-subtitle"></p>
                        <a href="/" class="slide-link"><i class="fas fa-arrow-right"></i></a>
                    </div>
                </div>
                
                <div class="slide">
                    <img src="./assets/images/album/IMG_20230815_121415.jpg" alt="白云" class="slide-img">
                    <div class="slide-content">
                        <h2 class="slide-title"></h2>
                        <p class="slide-subtitle"></p>
                        <a href="/" class="slide-link"><i class="fas fa-arrow-right"></i></a>
                    </div>
                </div>
                
                <div class="slide">
                    <img src="./assets/images/album/IMG_20230820_183148.jpg" alt="烟台山小店" class="slide-img">
                    <div class="slide-content">
                        <h2 class="slide-title"></h2>
                        <p class="slide-subtitle"></p>
                        <a href="/" class="slide-link"><i class="fas fa-arrow-right"></i></a>
                    </div>
                </div>

                <div class="slide">
                    <img src="./assets/images/album/IMG_20241202_173347.jpg" alt="建发楼日落" class="slide-img">
                    <div class="slide-content">
                        <h2 class="slide-title"></h2>
                        <p class="slide-subtitle"></p>
                        <a href="/" class="slide-link"><i class="fas fa-arrow-right"></i></a>
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

                <div class="product-container">
                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=160&h=160&fit=crop" alt="手泥香酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">手泥香酥</h3>
                            <p class="product-price">¥68.8</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=160&h=160&fit=crop" alt="香芋酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">香芋酥</h3>
                            <p class="product-price">¥72.8</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1587668178277-295251f900ce?w=160&h=160&fit=crop" alt="蛋黄酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">蛋黄酥</h3>
                            <p class="product-price">¥75.0</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=160&h=160&fit=crop" alt="凤梨酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">凤梨酥</h3>
                            <p class="product-price">¥69.9</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=160&h=160&fit=crop" alt="手泥香酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">手泥香酥</h3>
                            <p class="product-price">¥68.8</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=160&h=160&fit=crop" alt="香芋酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">香芋酥</h3>
                            <p class="product-price">¥72.8</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1587668178277-295251f900ce?w=160&h=160&fit=crop" alt="蛋黄酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">蛋黄酥</h3>
                            <p class="product-price">¥75.0</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=160&h=160&fit=crop" alt="凤梨酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">凤梨酥</h3>
                            <p class="product-price">¥69.9</p>
                        </div>
                    </div>                    
                   <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=160&h=160&fit=crop" alt="手泥香酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">手泥香酥</h3>
                            <p class="product-price">¥68.8</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=160&h=160&fit=crop" alt="香芋酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">香芋酥</h3>
                            <p class="product-price">¥72.8</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1587668178277-295251f900ce?w=160&h=160&fit=crop" alt="蛋黄酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">蛋黄酥</h3>
                            <p class="product-price">¥75.0</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=160&h=160&fit=crop" alt="凤梨酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">凤梨酥</h3>
                            <p class="product-price">¥69.9</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=160&h=160&fit=crop" alt="手泥香酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">手泥香酥</h3>
                            <p class="product-price">¥68.8</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=160&h=160&fit=crop" alt="香芋酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">香芋酥</h3>
                            <p class="product-price">¥72.8</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1587668178277-295251f900ce?w=160&h=160&fit=crop" alt="蛋黄酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">蛋黄酥</h3>
                            <p class="product-price">¥75.0</p>
                        </div>
                    </div>

                    <div class="product-card">
                        <img src="https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=160&h=160&fit=crop" alt="凤梨酥" class="product-image">
                        <div class="product-info">
                            <h3 class="product-name">凤梨酥</h3>
                            <p class="product-price">¥69.9</p>
                        </div>
                    </div>   
                </div>                
                
            </div>
        </div>

        <!-- Sidebar-->
        <aside class="sidebar">
            <!--user Card-->
            <div class="card">
                <div class="user-card-content">
                    <div class="user-header">
                        <img id="sidebar-avatar" src="./assets/images/avatars/avatar-main.webp" alt="Profile" class="user-avatar">
                        <h3 id="sidebar-username" class="user-name">𝓣𝓼°𝓒 𝓢𝓱𝓪𝓭𝓸𝔀</h3>
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-content">
                    <h3 class="section-title"><i class="fas fa-shopping-cart"></i> 购物车</h3>
                    <ul class="shopping-list">
                        <li class="shopping-item">
                            <a href="https://zaiwen.xueban.org.cn/chat/working-edition" class="shopping-link">在问AI</a>
                            <i class="fas fa-link shopping-icon"></i>
                        </li>
                        <li class="shopping-item">
                            <a href="https://www.bilibili.com/" class="shopping-link">哔哩哔哩bilibili</a>
                            <i class="fas fa-link shopping-icon"></i>
                        </li>
                    </ul>
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
    <script src="assets/js/modal.js"></script>
</body>
</html>