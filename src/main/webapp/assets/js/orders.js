$(document).ready(function() {
    // 获取订单模态框元素
    var ordersModal = document.getElementById("orders-modal");
    var closeOrdersBtn = ordersModal.querySelector(".close-button");
    
    // 关闭按钮点击事件
    closeOrdersBtn.addEventListener("click", function() {
        ordersModal.style.display = "none";
    });
    
    // 点击我的订单按钮事件
    $(document).on('click', '#my-orders-btn', function() {
        // 检查用户是否已登录
        if (!isUserLoggedIn()) {
            // 如果未登录，显示登录模态框
            $('#login-modal').css('display', 'flex');
            return;
        }
        
        // 加载用户订单
        loadUserOrders();
        
        // 显示订单模态框
        ordersModal.style.display = "flex";
    });
    
    // 加载用户订单
    function loadUserOrders() {
        $.ajax({
            url: 'getUserOrders',
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    displayUserOrders(response.orders);
                } else {
                    $('#orders-message').text(response.message);
                }
            },
            error: function() {
                $('#orders-message').text('获取订单失败，请稍后重试');
            }
        });
    }
    
    // 显示用户订单
    function displayUserOrders(orders) {
        var ordersList = $('#orders-list');
        ordersList.empty();
        
        if (orders.length === 0) {
            ordersList.html('<p class="empty-message">您还没有订单记录</p>');
            return;
        }
        
        // 按订单ID分组
        const groupedOrders = {};
        orders.forEach(order => {
            if (!groupedOrders[order.orderId]) {
                groupedOrders[order.orderId] = [];
            }
            groupedOrders[order.orderId].push(order);
        });
        
        // 生成订单HTML
        for (const orderId in groupedOrders) {
            const orderItems = groupedOrders[orderId];
            const firstItem = orderItems[0];
            
            // 计算该订单总金额
            let orderTotal = 0;
            orderItems.forEach(item => {
                orderTotal += parseFloat(item.total);
            });
            
            // 创建订单卡片
            const orderCard = $('<div class="order-card"></div>');
            
            // 订单头部
            const orderHeader = $(`
                <div class="order-header">
                    <div class="order-info">
                        <span class="order-id">订单号: ${orderId}</span>
                        <span class="order-date">下单时间: ${firstItem.orderDate}</span>
                    </div>
                    <div class="order-status">已完成</div>
                </div>
            `);
            
            // 订单商品列表
            const orderItemsList = $('<div class="order-items"></div>');
            orderItems.forEach(item => {
                const orderItem = $(`
                    <div class="order-item">
                        <div class="item-info">
                            <span class="item-name">${item.productName}</span>
                            <span class="item-price">¥${item.price}</span>
                            <span class="item-quantity">x${item.quantity}</span>
                        </div>
                        <div class="item-subtotal">¥${item.total}</div>
                    </div>
                `);
                orderItemsList.append(orderItem);
            });
            
            // 订单底部
            const orderFooter = $(`
                <div class="order-footer">
                    <div class="order-recipient">
                        <p>收件人: ${firstItem.realName}</p>
                        <p>电话: ${firstItem.phone}</p>
                        <p>地址: ${firstItem.address}</p>
                    </div>
                    <div class="order-payment">
                        <p>支付方式: ${firstItem.paymentMethod}</p>
                        <p class="order-total">订单总额: ¥${orderTotal.toFixed(2)}</p>
                    </div>
                </div>
            `);
            
            // 组合订单卡片
            orderCard.append(orderHeader);
            orderCard.append(orderItemsList);
            orderCard.append(orderFooter);
            ordersList.append(orderCard);
        }
    }
});

// 检查用户是否登录
function isUserLoggedIn() {
    return sessionStorage.getItem("userInfo") !== null;
}
