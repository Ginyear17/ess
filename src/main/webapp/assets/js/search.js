$(document).ready(function() {
    // 搜索按钮点击事件
    $('.search-btn').click(function() {
        $('#search-modal').css('display', 'flex');
        $('#search-input').focus();
    });
    
    // 关闭搜索模态框
    $('#search-modal .close-button').click(function() {
        $('#search-modal').css('display', 'none');
    });
    
    // 提交搜索请求
    $('#search-submit-btn').click(function() {
        submitSearch();
    });
    
    // 按回车键提交搜索
    $('#search-input').keypress(function(e) {
        if (e.which === 13) {
            submitSearch();
        }
    });
    
    // 执行搜索
    function submitSearch() {
        const keyword = $('#search-input').val().trim();
        
        if (!keyword) {
            $('#search-message').text('请输入搜索关键词').css('color', 'red');
            return;
        }
        
        $('#search-message').text('正在搜索...').css('color', 'blue');
        
        $.ajax({
            url: 'searchProducts',
            type: 'GET',
            data: { keyword: keyword },
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    // 搜索成功
                    $('#search-message').text('找到 ' + response.products.length + ' 个相关商品').css('color', 'green');
                    displaySearchResults(response.products);
                    $('#search-modal').css('display', 'none');
                } else {
                    // 搜索失败
                    $('#search-message').text(response.message).css('color', 'red');
                }
            },
            error: function() {
                $('#search-message').text('搜索请求失败，请稍后再试').css('color', 'red');
            }
        });
    }
    
    // 显示搜索结果
    function displaySearchResults(products) {
        const $productContainer = $('.product-container');
        
        // 清空现有商品
        $productContainer.empty();
        
        if (products.length === 0) {
            $productContainer.html('<div class="no-results">没有找到相关商品</div>');
            return;
        }
        
        // 添加搜索结果商品
        products.forEach(function(product) {
            const productCard = `
                <div class="product-card" data-category="${product.category}">
                    <img src="${product.imageUrl}" alt="${product.productName}" class="product-image">
                    <div class="product-info">
                        <h4 class="product-name">${product.productName}</h4>
                        <div class="product-card-footer">
                            <p class="product-price">¥${product.productPrice}</p>
                            <span class="product-category" style="display:none;">${product.category}</span>
                            <div class="cart-controls">
                                <button class="cart-btn add-to-cart" data-id="${product.productId}">
                                    <i class="fas fa-cart-plus"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            $productContainer.append(productCard);
        });
        
        // 更新标题显示搜索结果
        $('.product-section-title').html('<i class="fas fa-search"></i> 搜索结果');
        
        // 为新添加的添加购物车按钮绑定事件
        bindAddToCartEvents();
    }
    
    // 重新绑定添加购物车事件
    function bindAddToCartEvents() {
        $('.add-to-cart').off('click').on('click', function() {
            const productId = $(this).data('id');
            addToCart(productId, 1);
        });
    }
});
