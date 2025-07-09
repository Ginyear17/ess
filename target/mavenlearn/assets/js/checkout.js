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

// 检查用户是否登录
function isUserLoggedIn() {
    const userInfoStr = sessionStorage.getItem("userInfo");
    return userInfoStr !== null;
}
