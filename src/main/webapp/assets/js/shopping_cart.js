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


// 结算按钮点击事件
// 获取结算模态框元素
var checkoutModal = document.getElementById("checkout-modal");
var cancelCheckoutBtn = document.getElementById("cancel-checkout-btn");
var confirmCheckoutBtn = document.getElementById("confirm-checkout-btn");
var closeCheckoutBtn = checkoutModal.querySelector(".close-button");

// 结算按钮点击事件处理
$(document).ready(function() {
    $('#checkout-btn').on('click', function() {
        // 检查用户是否已登录
        if (!isUserLoggedIn()) {
            // 如果未登录，显示登录模态框
            $('#login-modal').css('display', 'flex');
            return;
        }
        
        // 检查购物车是否为空
        if ($('.shopping-list .shopping-item').length === 0 || 
            $('.shopping-list .shopping-item:first').text().trim() === '购物车为空' ||
            parseFloat($('#cart-total').text()) <= 0) {
            alert('购物车为空，无法结算');
            return;
        }
        
        // 加载购物车商品到结算模态框
        loadCheckoutItems();
        
        // 显示结算模态框
        checkoutModal.style.display = "flex";
    });
    
    // 取消结算按钮点击事件
    cancelCheckoutBtn.addEventListener("click", function() {
        checkoutModal.style.display = "none";
    });
    
    // 关闭按钮点击事件
    closeCheckoutBtn.addEventListener("click", function() {
        checkoutModal.style.display = "none";
    });
    
    // 确认订单按钮点击事件
    confirmCheckoutBtn.addEventListener("click", function() {
        processOrder();
    });
});

// 加载购物车商品到结算模态框
function loadCheckoutItems() {
    // 获取购物车商品
    $.ajax({
        url: 'getCartItems',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            // 清空结算商品列表
            var checkoutItems = document.querySelector('.checkout-items');
            checkoutItems.innerHTML = '';
            
            let total = 0;
            
            // 如果购物车为空
            if (data.length === 0) {
                checkoutItems.innerHTML = '<p>购物车为空</p>';
                document.getElementById('checkout-total').textContent = '0.00';
                return;
            }
            
            // 添加商品到结算列表
            data.forEach(function(item) {
                const subtotal = item.quantity * item.product_price;
                total += subtotal;
                
                const itemHTML = `
                    <div class="checkout-item" data-id="${item.product_id}">
                        <div class="checkout-item-name">${item.product_name}</div>
                        <div class="checkout-item-price">¥${item.product_price.toFixed(2)}</div>
                        <div class="checkout-item-quantity">x${item.quantity}</div>
                        <div class="checkout-item-subtotal">¥${subtotal.toFixed(2)}</div>
                    </div>
                `;
                
                checkoutItems.innerHTML += itemHTML;
            });
            
            // 更新总金额
            document.getElementById('checkout-total').textContent = total.toFixed(2);
        },
        error: function() {
            console.error('获取购物车商品失败');
        }
    });
}

// 处理订单
function processOrder() {
    // 获取表单数据
    const recipientName = document.getElementById('recipient-name').value;
    const phone = document.getElementById('phone').value;
    const address = document.getElementById('address').value;
    const paymentMethod = document.querySelector('input[name="payment"]:checked').value;
    
    // 表单验证
    if (!recipientName || !phone || !address) {
        document.getElementById('checkout-message').textContent = '请填写完整的收货信息';
        return;
    }
    
    // 提交订单数据
    $.ajax({
        url: 'placeOrder',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            recipient: recipientName,
            phone: phone,
            address: address,
            paymentMethod: paymentMethod
        }),
        success: function(response) {
            if (response.success) {
                // 订单成功
                checkoutModal.style.display = "none";
                alert('订单提交成功！');
                
                // 清空购物车
                $('.shopping-list').html('<li class="shopping-item">购物车为空</li>');
                $('#cart-total').text('0.00');
                
                // 重定向到订单确认页面或者回到首页
                window.location.href = 'orderConfirmation?orderId=' + response.orderId;
            } else {
                // 显示错误信息
                document.getElementById('checkout-message').textContent = response.message || '订单提交失败，请重试';
            }
        },
        error: function() {
            document.getElementById('checkout-message').textContent = '系统错误，请稍后重试';
        }
    });
}

// 检查用户是否登录的函数
function isUserLoggedIn() {
    // 检查sessionStorage中是否存在userInfo数据
    return sessionStorage.getItem("userInfo") !== null;
}