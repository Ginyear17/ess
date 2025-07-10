$(document).ready(function() {
    // 初始化加载数据
    loadDashboardData();

    // 加载控制台概览数据
    function loadDashboardData() {
        $.ajax({
            url: 'admin/dashboard',
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                $('#user-count').text(response.userCount || 0);
                $('#product-count').text(response.productCount || 0);
            },
            error: function () {
                $('#user-count').text('加载失败');
                $('#product-count').text('加载失败');
            }
        });
    }

    // 默认加载控制台页面 (这行代码确保页面加载时显示控制台数据)
    $('.nav-sidebar li:first-child a').click();


    //---------------------用户
    // 加载用户数据
    function loadUsersData() {
        $.ajax({
            url: 'admin/users',
            type: 'GET',
            dataType: 'json',
            success: function (users) {
                var userTable = $('#users-table');
                userTable.empty();
                $.each(users, function (index, user) {
                    var row = $('<tr>');
                    row.append($('<td>').text(user.userId));
                    row.append($('<td>').text(user.userName));
                    row.append($('<td>').text(user.email)); // 显示 email
                    row.append($('<td>').text(user.userType));
                    row.append($('<td>').text(new Date(user.createdAt).toLocaleString())); // 格式化日期
                    //  如果需要显示头像，可以在这里添加：
                    // row.append($('<td>').html('<img src="' + user.avatarUrl + '" alt="Avatar" width="50">'));

                    var actions = $('<td>');
                    var editButton = $('<button>').addClass('btn btn-primary btn-xs edit-user-btn').text('编辑').data('user', user);
                    var deleteButton = $('<button>').addClass('btn btn-danger btn-xs delete-user-btn').text('删除').data('user-id', user.userId);
                    actions.append(editButton).append(deleteButton); // 添加删除按钮
                    row.append(actions);
                    userTable.append(row);
                });
            },
            error: function () {
                alert('加载用户数据失败');
            }
        });
    }

    // 点击用户管理标签时加载用户数据
    $('a[data-target="#users"]').on('click', function () {
        loadUsersData();
    });

    // 编辑用户
    $('#users-table').on('click', '.edit-user-btn', function () {
        var user = $(this).data('user');
        $('#user-id').val(user.userId);
        $('#user-name').val(user.userName);
        $('#user-email').val(user.email); // 设置 email
        $('#user-type').val(user.userType);
        $('#userModal').modal('show');
    });

    // 删除用户
    $('#users-table').on('click', '.delete-user-btn', function () {
        var userId = $(this).data('user-id');
        if (confirm("确定要删除该用户吗？")) {  // 添加确认提示
            $.ajax({
                url: 'admin/users',
                type: 'POST',
                data: {
                    action: 'delete', // 添加 action 参数
                    userId: userId
                },
                success: function (response) {
                    loadUsersData(); // 重新加载用户数据
                    alert("用户删除成功");
                },
                error: function (xhr, status, error) {
                    alert("用户删除失败: " + xhr.responseText);
                }
            });
        }
        loadDashboardData();
    });

    // 保存用户修改
    $('#save-user-btn').on('click', function () {
        var userId = $('#user-id').val();
        var userName = $('#user-name').val();
        var userEmail = $('#user-email').val();
        var userType = $('#user-type').val();

        $.ajax({
            url: 'admin/users',
            type: 'POST',
            data: {
                action: 'update', // 添加 action 参数
                userId: userId,
                userName: userName,
                userEmail: userEmail,
                userType: userType
            },
            success: function (response) { // 更新成功后的回调函数
                $('#userModal').modal('hide'); // 关闭模态框
                loadUsersData();          // 重新加载用户数据，更新表格
                alert("用户更新成功");     // 显示成功消息
            },
            error: function (xhr, status, error) { // 更新失败后的回调函数
                alert("用户更新失败: " + xhr.responseText); // 显示错误消息
            }
        });
    });

    // 商品管理功能
    $(document).ready(function() {
        // 加载商品数据
        loadProducts();

        // 添加商品按钮点击事件
        $('#add-product-btn').click(function() {
            $('#productModalTitle').text('添加商品');
            $('#product-id').val('');
            $('#productForm')[0].reset();
            $('#productModal').modal('show');
        });

        // 保存商品按钮点击事件
        $('#save-product-btn').click(function() {
            saveProduct();
        });
    });

    // 加载商品数据
    function loadProducts() {
        $.ajax({
            url: 'admin/products',
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                renderProductsTable(data);
                // 更新商品总数
                $('#product-count').text(data.length);
            },
            error: function() {
                alert('加载商品数据失败');
            }
        });
    }

    // 渲染商品表格
    function renderProductsTable(products) {
        const $tbody = $('#products-table');
        $tbody.empty();

        products.forEach(function(product) {
            const $tr = $('<tr>');
            $tr.append($('<td>').text(product.productId));
            $tr.append($('<td>').text(product.productName));
            $tr.append($('<td>').text('¥' + product.productPrice.toFixed(2)));
            $tr.append($('<td>').text(product.productStock));
            $tr.append($('<td>').text(product.category));
            $tr.append($('<td>').text(product.isActive ? '上架' : '下架'));

            // 操作按钮
            const $btnGroup = $('<div>').addClass('btn-group');
            const $editBtn = $('<button>')
                .addClass('btn btn-xs btn-primary')
                .text('编辑')
                .click(function() {
                    editProduct(product);
                });
            const $deleteBtn = $('<button>')
                .addClass('btn btn-xs btn-danger')
                .text('删除')
                .click(function() {
                    deleteProduct(product.productId);
                });

            $btnGroup.append($editBtn, $deleteBtn);
            $tr.append($('<td>').append($btnGroup));
            $tbody.append($tr);
        });
    }

    // 编辑商品
    function editProduct(product) {
        $('#productModalTitle').text('编辑商品');
        $('#product-id').val(product.productId);
        $('#product-name').val(product.productName);
        $('#product-description').val(product.productDescription);
        $('#product-price').val(product.productPrice);
        $('#product-stock').val(product.productStock);
        $('#product-category').val(product.category);
        $('#product-image').val(product.imageUrl);
        $('#product-active').prop('checked', product.isActive);

        $('#productModal').modal('show');
    }

    // 保存商品
    function saveProduct() {
        const productId = $('#product-id').val();
        const isAdd = productId === '';

        const productData = {
            productName: $('#product-name').val(),
            productDescription: $('#product-description').val(),
            productPrice: $('#product-price').val(),
            productStock: $('#product-stock').val(),
            category: $('#product-category').val(),
            imageUrl: $('#product-image').val(),
            isActive: $('#product-active').is(':checked'),
            action: isAdd ? 'add' : 'update'
        };

        if (!isAdd) {
            productData.productId = productId;
        }

        $.ajax({
            url: 'admin/products',
            type: 'POST',
            data: productData,
            success: function(response) {
                $('#productModal').modal('hide');
                loadProducts();
                alert(isAdd ? '商品添加成功' : '商品更新成功');
            },
            error: function() {
                alert(isAdd ? '商品添加失败' : '商品更新失败');
            }
        });
    }

    // 删除商品
    function deleteProduct(productId) {
        if (!confirm('确定要删除这个商品吗？')) {
            return;
        }

        $.ajax({
            url: 'admin/products',
            type: 'POST',
            data: {
                action: 'delete',
                productId: productId
            },
            success: function(response) {
                alert('商品删除成功');
                loadProducts();
            },
            error: function() {
                alert('商品删除失败');
            }
        });
    }

// 加载订单数据
    function loadOrdersData() {
        $.ajax({
            url: 'admin/orders',
            type: 'GET',
            dataType: 'json',
            success: function(orders) {
                var orderTable = $('#orders-table');
                orderTable.empty();
                $.each(orders, function(index, order) {
                    var row = $('<tr>');
                    row.append($('<td>').text(order.order_id));
                    row.append($('<td>').text(order.user_id));
                    row.append($('<td>').text(order.product_id));
                    row.append($('<td>').text(new Date(order.order_date).toLocaleString()));
                    row.append($('<td>').text('¥' + order.price.toFixed(2)));
                    row.append($('<td>').text(order.quantity));
                    row.append($('<td>').text('¥' + order.total.toFixed(2)));
                    row.append($('<td>').text(order.real_name));
                    row.append($('<td>').text(order.phone));
                    row.append($('<td>').text(order.address));
                    row.append($('<td>').text(getPaymentMethodText(order.payment_method)));

                    // 操作按钮
                    var actions = $('<td>');
                    var editButton = $('<button>').addClass('btn btn-primary btn-xs edit-order-btn').text('编辑').data('order', order);
                    var deleteButton = $('<button>').addClass('btn btn-danger btn-xs delete-order-btn').text('删除').data('order-id', order.order_id);
                    actions.append(editButton).append(' ').append(deleteButton);
                    row.append(actions);

                    orderTable.append(row);
                });
            },
            error: function() {
                alert('加载订单数据失败');
            }
        });
    }

// 支付方式文本转换
    function getPaymentMethodText(method) {
        switch(method) {
            case 'alipay': return '支付宝';
            case 'wechat': return '微信支付';
            case 'cod': return '货到付款';
            default: return method;
        }
    }

// 点击订单管理标签时加载订单数据
    $('a[data-target="#orders"]').on('click', function() {
        loadOrdersData();
    });

// 编辑订单按钮点击事件
    $('#orders-table').on('click', '.edit-order-btn', function() {
        var order = $(this).data('order');
        $('#order-id').val(order.order_id);
        $('#order-user-id').val(order.user_id);
        $('#order-product-id').val(order.product_id);
        $('#order-price').val(order.price);
        $('#order-quantity').val(order.quantity);
        $('#order-real-name').val(order.real_name);
        $('#order-phone').val(order.phone);
        $('#order-address').val(order.address);
        $('#order-payment-method').val(order.payment_method);

        $('#orderModalTitle').text('编辑订单');
        $('#orderModal').modal('show');
    });

// 保存订单修改
    $('#save-order-btn').on('click', function() {
        var orderData = {
            order_id: $('#order-id').val(),
            user_id: $('#order-user-id').val(),
            product_id: $('#order-product-id').val(),
            price: $('#order-price').val(),
            quantity: $('#order-quantity').val(),
            real_name: $('#order-real-name').val(),
            phone: $('#order-phone').val(),
            address: $('#order-address').val(),
            payment_method: $('#order-payment-method').val(),
            action: 'update'
        };

        $.ajax({
            url: 'admin/orders',
            type: 'POST',
            data: orderData,
            success: function(response) {
                $('#orderModal').modal('hide');
                loadOrdersData();
                alert("订单更新成功");
            },
            error: function(xhr) {
                alert("订单更新失败: " + xhr.responseText);
            }
        });
    });

    // 删除订单
    $('#orders-table').on('click', '.delete-order-btn', function() {
        var orderId = $(this).data('order-id');

        if (confirm("确定要删除该订单吗？此操作不可撤销！")) {
            $.ajax({
                url: 'admin/orders',
                type: 'POST',
                data: {
                    action: 'delete',
                    order_id: orderId
                },
                success: function(response) {
                    loadOrdersData();
                    alert("订单删除成功");
                },
                error: function(xhr) {
                    alert("订单删除失败: " + xhr.responseText);
                }
            });
        }
    });


});


