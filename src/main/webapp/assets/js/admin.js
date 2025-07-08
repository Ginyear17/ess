$(document).ready(function() {
    // 加载商品列表
    loadProducts();
    
    // 加载用户列表
    loadUsers();
    
    // 加载订单列表
    loadOrders();
    
    // 主题切换
    $('#theme-toggle').click(function() {
        $('body').toggleClass('dark-theme');
        var icon = $(this).find('i');
        if (icon.hasClass('fa-sun')) {
            icon.removeClass('fa-sun').addClass('fa-moon');
        } else {
            icon.removeClass('fa-moon').addClass('fa-sun');
        }
    });
    
    // 添加商品按钮点击事件
    $('#add-product-btn').click(function() {
        $('#add-product-form')[0].reset();
        $('#add-product-modal').modal('show');
    });
    
    // 保存新商品按钮点击事件
    $('#save-product-btn').click(function() {
        saveProduct();
    });
    
    // 更新商品按钮点击事件
    $('#update-product-btn').click(function() {
        updateProduct();
    });
    
    // 过滤商品列表
    $('#category-filter').change(function() {
        filterProducts();
    });
    
    $('#product-search').keyup(function() {
        filterProducts();
    });
    
    // 搜索用户
    $('#search-user-btn').click(function() {
        searchUsers();
    });
    
    // 搜索订单
    $('#search-order-btn').click(function() {
        searchOrders();
    });
});

// 加载商品列表函数
function loadProducts() {
    $.ajax({
        url: 'admin/getProducts',
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            if (response.success) {
                renderProductList(response.products);
            } else {
                alert('加载商品列表失败：' + response.message);
            }
        },
        error: function() {
            alert('服务器错误，请稍后再试');
        }
    });
}

// 渲染商品列表
function renderProductList(products) {
    var productList = $('#product-list');
    productList.empty();
    
    $.each(products, function(index, product) {
        var statusBadge = product.is_active ? 
            '<span class="label label-success">已上架</span>' : 
            '<span class="label label-default">已下架</span>';
        
        var row = $('<tr></tr>');
        row.append('<td>' + product.product_id + '</td>');
        row.append('<td><img src="' + product.image_url + '" width="50" height="50" class="img-thumbnail"></td>');
        row.append('<td>' + product.product_name + '</td>');
        row.append('<td>¥ ' + product.product_price + '</td>');
        row.append('<td>' + product.product_stock_quantity + '</td>');
        row.append('<td>' + product.category + '</td>');
        row.append('<td>' + statusBadge + '</td>');
        
        var actions = $('<td></td>');
        actions.append('<button class="btn btn-sm btn-info btn-action edit-product" data-id="' + product.product_id + '"><i class="fas fa-edit"></i></button> ');
        actions.append('<button class="btn btn-sm btn-danger btn-action delete-product" data-id="' + product.product_id + '"><i class="fas fa-trash"></i></button> ');
        
        if (product.is_active) {
            actions.append('<button class="btn btn-sm btn-warning btn-action toggle-product" data-id="' + product.product_id + '" data-active="true"><i class="fas fa-eye-slash"></i></button>');
        } else {
            actions.append('<button class="btn btn-sm btn-success btn-action toggle-product" data-id="' + product.product_id + '" data-active="false"><i class="fas fa-eye"></i></button>');
        }
        
        row.append(actions);
        productList.append(row);
    });
    
    // 绑定编辑商品事件
    $('.edit-product').click(function() {
        var productId = $(this).data('id');
        editProduct(productId);
    });
    
    // 绑定删除商品事件
    $('.delete-product').click(function() {
        var productId = $(this).data('id');
        if (confirm('确定要删除这个商品吗？')) {
            deleteProduct(productId);
        }
    });
    
    // 绑定切换商品状态事件
    $('.toggle-product').click(function() {
        var productId = $(this).data('id');
        var currentActive = $(this).data('active') === true;
        toggleProductStatus(productId, !currentActive);
    });
}

// 其他函数实现（省略，可根据需要补充）

// 用户列表和订单列表的加载和渲染函数（省略）
