
$(document).ready(function() {
    // 保存所有商品的数据
    let allProducts = [];
    
    // 初始化时加载所有商品
    function initProducts() {
        // 将现有商品数据存储到allProducts变量
        $('.product-card').each(function() {
            allProducts.push($(this).clone());
        });
    }
    
    // 根据分类过滤商品
    function filterProductsByCategory(category) {
        const productContainer = $('.product-container');
        productContainer.empty();
        
        // 更新分类标题
        $('.product-section-title').html('<i class="fas fa-pen"></i>' + 
            (category === 'all' ? '所有商品' : category + '商品'));
        
        if (category === 'all') {
            // 显示所有商品
            allProducts.forEach(product => {
                productContainer.append(product.clone());
            });
        } else {
            // 过滤并显示指定分类的商品
            allProducts.forEach(product => {
                // 从商品元素中获取分类信息
                const productCategory = $(product).data('category');
                if (productCategory === category) {
                    productContainer.append(product.clone());
                }
            });
        }
        
        // 重新绑定添加到购物车按钮事件
        bindAddToCartEvents();
    }
    
    // 重新绑定添加到购物车按钮事件
    function bindAddToCartEvents() {
        $('.add-to-cart').click(function() {
            const productId = $(this).data('id');
            
            // 检查用户是否登录
            if(!isUserLoggedIn()) {
                // 如果未登录，显示请登录
                $('#login-modal').css('display', 'flex');
                return;
            }
            
            // 发送AJAX请求添加商品到购物车
            $.ajax({
                url: 'addToCart',
                type: 'POST',
                data: { productId: productId },
                success: function(response) {
                    if(response.success) {
                        // 显示添加成功提示
                        console.log('商品已添加到购物车');
                        
                        // 更新购物车显示
                        updateShoppingCartDisplay(response.cartItems);
                    } else {
                        alert(response.message);
                    }
                },
                error: function() {
                    alert('添加失败，请重试');
                }
            });
        });
    }
    
    // 导航菜单点击事件
    $('.nav-link').click(function(e) {
        e.preventDefault();
        
        // 更新活动状态
        $('.nav-link').removeClass('active');
        $(this).addClass('active');
        
        // 获取选中的分类
        const category = $(this).data('category');
        
        // 过滤显示对应分类的商品
        filterProductsByCategory(category);
    });
    
    // 初始化
    initProducts();
    
    // 为每个商品卡片添加分类数据属性
    $('.product-card').each(function() {
        const category = $(this).find('.product-category').text();
        $(this).attr('data-category', category);
    });
});
