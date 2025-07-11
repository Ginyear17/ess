$(document).ready(function() {
    // 在页面加载时获取购物车数据
    if(isUserLoggedIn()) {
        // 发送请求获取购物车数据
        $.ajax({
            url: 'getCartItems',
            type: 'GET',
            success: function(cartItems) {
                // 更新购物车显示
                updateShoppingCartDisplay(cartItems);
            },
            error: function() {
                console.log('获取购物车数据失败');
            }
        });
    }

    // 添加到购物车
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

    // 购物车数量增加按钮点击事件
    $(document).on('click', '.increase-btn', function() {
        const productId = $(this).data('id');
        updateCartItemQuantity(productId, 1);
    });
    
    // 购物车数量减少按钮点击事件
    $(document).on('click', '.decrease-btn', function() {
        const productId = $(this).data('id');
        // 无论数量是多少，都允许减少
        updateCartItemQuantity(productId, -1);
    });

    
    // 更新购物车项目数量的函数
    function updateCartItemQuantity(productId, change) {
        $.ajax({
            url: 'updateCart',
            type: 'POST',
            data: { 
                productId: productId,
                quantityChange: change 
            },
            success: function(response) {
                if(response.success) {
                    // 更新购物车显示
                    updateShoppingCartDisplay(response.cartItems);
                } else {
                    alert(response.message);
                }
            },
            error: function() {
                alert('更新失败，请重试');
            }
        });
    }


});

// 更新购物车显示函数修改
function updateShoppingCartDisplay(cartItems) {
    const shoppingList = $('.shopping-list');
    shoppingList.empty();
    
    if (!cartItems || cartItems.length === 0) {
        shoppingList.append('<li class="shopping-item">购物车为空</li>');
        return;
    }
    
    // 遍历购物车项目并添加到列表
    cartItems.forEach(function(item) {
        const itemHtml = `
            <li class="shopping-item">
                <div class="item-info">
                    <a href="./product?id=${item.product_id}" class="shopping-link">${item.product_name}</a>
                    <span class="quantity-badge">${item.quantity}</span>
                </div>
                <div class="quantity-controls">
                    <button class="quantity-btn decrease-btn" data-id="${item.product_id}">
                        <i class="fas fa-minus"></i>
                    </button>
                    <span class="item-quantity">${item.quantity}</span>
                    <button class="quantity-btn increase-btn" data-id="${item.product_id}">
                        <i class="fas fa-plus"></i>
                    </button>
                </div>
            </li>
        `;
        shoppingList.append(itemHtml);
    });


}


// 计算并更新购物车总金额
function updateCartTotal(cartItems) {
    let total = 0;
    
    if (cartItems && cartItems.length > 0) {
        cartItems.forEach(function(item) {
            // 使用product_price而不是price
            if (item.product_price) {
                total += parseFloat(item.product_price) * item.quantity;
            }
        });
    }
    
    // 更新总金额显示
    $('#cart-total').text(total.toFixed(2));
}


// 在更新购物车显示函数中调用总金额更新
function updateShoppingCartDisplay(cartItems) {
    const shoppingList = $('.shopping-list');
    shoppingList.empty();
    
    if (!cartItems || cartItems.length === 0) {
        shoppingList.append('<li class="shopping-item">购物车为空</li>');
        // 更新总金额为0
        updateCartTotal([]);
        return;
    }
    
    // 遍历购物车项目并添加到列表
    cartItems.forEach(function(item) {
        const itemHtml = `
            <li class="shopping-item">
                <div class="item-info">
                    <a href="./product?id=${item.product_id}" class="shopping-link">${item.product_name}</a>
                    <span class="quantity-badge">${item.quantity}</span>
                    <span class="price-badge">¥${parseFloat(item.product_price).toFixed(2)}</span>
                </div>
                <div class="quantity-controls">
                    <button class="quantity-btn decrease-btn" data-id="${item.product_id}">
                        <i class="fas fa-minus"></i>
                    </button>
                    <span class="item-quantity">${item.quantity}</span>
                    <button class="quantity-btn increase-btn" data-id="${item.product_id}">
                        <i class="fas fa-plus"></i>
                    </button>
                </div>
            </li>
        `;
        shoppingList.append(itemHtml);
    });
    
    // 更新总金额
    updateCartTotal(cartItems);
}

// 检查用户是否登录的函数
function isUserLoggedIn() {
    // 检查sessionStorage中是否存在userInfo数据
    return sessionStorage.getItem("userInfo") !== null;
}