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
                // 显示添加成功提示
                alert('商品已添加到购物车');
                // 可以在这里更新购物车图标或数量
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
