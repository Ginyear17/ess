$(document).ready(function() {
    // 添加到购物车
    $('.add-to-cart').click(function() {
        const productId = $(this).data('id');
        
        // 检查用户是否登录
        if(!isUserLoggedIn()) {
            // 如果未登录，显示登录模态框
            $('#login-modal').show();
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
                    alert('商品已添加到购物车');
                    
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
    
    // 检查用户是否登录的函数
    function isUserLoggedIn() {
        // 检查sessionStorage中是否存在userInfo数据
        return sessionStorage.getItem("userInfo") !== null;
    }

});

// 更新购物车显示
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
                <a href="./product?id=${item.product_id}" class="shopping-link">${item.product_name}</a>
                <span class="quantity-badge">${item.quantity}</span>
                <i class="fas fa-shopping-bag shopping-icon"></i>
            </li>
        `;
        shoppingList.append(itemHtml);
    });
}
