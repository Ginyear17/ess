$(document).ready(function() {
    // 初始化加载数据
    loadDashboardData();
    
    // 侧边栏点击事件
    $('.nav-sidebar a').click(function(e) {
        e.preventDefault();
        $('.nav-sidebar li').removeClass('active');
        $(this).parent().addClass('active');
        
        // 根据点击的链接加载相应的数据
        const target = $(this).data('target');
        if(target === '#users') {
            loadUsers();
        } else if(target === '#products') {
            loadProducts();
        }
    });

    // 加载控制台概览数据
    function loadDashboardData() {
        $.ajax({
            url: 'admin/dashboard',
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                $('#user-count').text(response.userCount || 0);
                $('#product-count').text(response.productCount || 0);
            },
            error: function() {
                $('#user-count').text('加载失败');
                $('#product-count').text('加载失败');
            }
        });
    }

    // 加载用户数据
    function loadUsers() {
        $.ajax({
            url: 'admin/users',
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                renderUsers(response.users);
            },
            error: function() {
                $('#users-table').html('<tr><td colspan="6" class="text-center">加载用户数据失败</td></tr>');
            }
        });
    }

    // 渲染用户列表
    function renderUsers(users) {
        if (!users || users.length === 0) {
            $('#users-table').html('<tr><td colspan="6" class="text-center">没有用户数据</td></tr>');
            return;
        }

        let html = '';
        users.forEach(function(user) {
            html += '<tr>';
            html += '<td>' + user.id + '</td>';
            html += '<td>' + user.userName + '</td>';
            html += '<td>' + user.email + '</td>';
            html += '<td>' + (user.isAdmin ? '管理员' : '普通用户') + '</td>';
            html += '<td>' + new Date(user.registerTime).toLocaleString() + '</td>';
            html += '<td>';
            html += '<button class="btn btn-sm btn-primary edit-user-btn" data-id="' + user.id + '">编辑</button> ';
            html += '<button class="btn btn-sm btn-danger delete-user-btn" data-id="' + user.id + '">删除</button>';
            html += '</td>';
            html += '</tr>';
        });

        $('#users-table').html(html);

        // 绑定编辑用户按钮事件
        $('.edit-user-btn').click(function() {
            const userId = $(this).data('id');
            editUser(userId);
        });

        // 绑定删除用户按钮事件
        $('.delete-user-btn').click(function() {
            const userId = $(this).data('id');
            if(confirm('确定要删除这个用户吗？')) {
                deleteUser(userId);
            }
        });
    }

    // 编辑用户
    function editUser(userId) {
        $.ajax({
            url: 'admin/user/' + userId,
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                $('#user-id').val(response.id);
                $('#user-name').val(response.userName);
                $('#user-email').val(response.email);
                $('#user-type').val(response.isAdmin ? 'admin' : 'user');
                $('#userModalTitle').text('编辑用户');
                $('#userModal').modal('show');
            },
            error: function() {
                alert('获取用户数据失败');
            }
        });
    }

    // 删除用户
    function deleteUser(userId) {
        $.ajax({
            url: 'admin/user/' + userId,
            type: 'DELETE',
            success: function() {
                alert('用户删除成功');
                loadUsers();
            },
            error: function() {
                alert('删除用户失败');
            }
        });
    }

    // 保存用户信息
    $('#save-user-btn').click(function() {
        const userId = $('#user-id').val();
        const userData = {
            id: userId,
            userName: $('#user-name').val(),
            email: $('#user-email').val(),
            isAdmin: $('#user-type').val() === 'admin'
        };

        $.ajax({
            url: 'admin/user' + (userId ? '/' + userId : ''),
            type: userId ? 'PUT' : 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function() {
                $('#userModal').modal('hide');
                loadUsers();
                alert(userId ? '用户更新成功' : '用户添加成功');
            },
            error: function() {
                alert('保存用户信息失败');
            }
        });
    });

    // 加载商品数据
    function loadProducts() {
        $.ajax({
            url: 'admin/products',
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                renderProducts(response.products);
            },
            error: function() {
                $('#products-table').html('<tr><td colspan="7" class="text-center">加载商品数据失败</td></tr>');
            }
        });
    }

    // 渲染商品列表
    function renderProducts(products) {
        if (!products || products.length === 0) {
            $('#products-table').html('<tr><td colspan="7" class="text-center">没有商品数据</td></tr>');
            return;
        }

        let html = '';
        products.forEach(function(product) {
            html += '<tr>';
            html += '<td>' + product.id + '</td>';
            html += '<td>' + product.name + '</td>';
            html += '<td>￥' + product.price.toFixed(2) + '</td>';
            html += '<td>' + product.stock + '</td>';
            html += '<td>' + product.category + '</td>';
            html += '<td>' + (product.active ? '上架' : '下架') + '</td>';
            html += '<td>';
            html += '<button class="btn btn-sm btn-primary edit-product-btn" data-id="' + product.id + '">编辑</button> ';
            html += '<button class="btn btn-sm btn-danger delete-product-btn" data-id="' + product.id + '">删除</button>';
            html += '</td>';
            html += '</tr>';
        });

        $('#products-table').html(html);

        // 绑定编辑商品按钮事件
        $('.edit-product-btn').click(function() {
            const productId = $(this).data('id');
            editProduct(productId);
        });

        // 绑定删除商品按钮事件
        $('.delete-product-btn').click(function() {
            const productId = $(this).data('id');
            if(confirm('确定要删除这个商品吗？')) {
                deleteProduct(productId);
            }
        });
    }

    // 添加商品按钮点击事件
    $('#add-product-btn').click(function() {
        $('#product-id').val('');
        $('#product-name').val('');
        $('#product-description').val('');
        $('#product-price').val('');
        $('#product-stock').val('');
        $('#product-category').val('数码');
        $('#product-image').val('');
        $('#product-active').prop('checked', true);
        $('#productModalTitle').text('添加商品');
        $('#productModal').modal('show');
    });

    // 编辑商品
    function editProduct(productId) {
        $.ajax({
            url: 'admin/product/' + productId,
            type: 'GET',
            dataType: 'json',
            success: function(product) {
                $('#product-id').val(product.id);
                $('#product-name').val(product.name);
                $('#product-description').val(product.description);
                $('#product-price').val(product.price);
                $('#product-stock').val(product.stock);
                $('#product-category').val(product.category);
                $('#product-image').val(product.imageUrl);
                $('#product-active').prop('checked', product.active);
                $('#productModalTitle').text('编辑商品');
                $('#productModal').modal('show');
            },
            error: function() {
                alert('获取商品数据失败');
            }
        });
    }

    // 删除商品
    function deleteProduct(productId) {
        $.ajax({
            url: 'admin/product/' + productId,
            type: 'DELETE',
            success: function() {
                alert('商品删除成功');
                loadProducts();
            },
            error: function() {
                alert('删除商品失败');
            }
        });
    }

    // 保存商品信息
    $('#save-product-btn').click(function() {
        const productId = $('#product-id').val();
        const productData = {
            id: productId,
            name: $('#product-name').val(),
            description: $('#product-description').val(),
            price: parseFloat($('#product-price').val()),
            stock: parseInt($('#product-stock').val()),
            category: $('#product-category').val(),
            imageUrl: $('#product-image').val(),
            active: $('#product-active').is(':checked')
        };

        $.ajax({
            url: 'admin/product' + (productId ? '/' + productId : ''),
            type: productId ? 'PUT' : 'POST',
            contentType: 'application/json',
            data: JSON.stringify(productData),
            success: function() {
                $('#productModal').modal('hide');
                loadProducts();
                alert(productId ? '商品更新成功' : '商品添加成功');
            },
            error: function() {
                alert('保存商品信息失败');
            }
        });
    });

    // 退出登录按钮点击事件
    $('#logout-btn').click(function(e) {
        e.preventDefault();
        if(confirm('确定要退出登录吗？')) {
            $.ajax({
                url: 'logout',
                type: 'POST',
                success: function() {
                    window.location.href = 'index.jsp';
                },
                error: function() {
                    alert('退出失败，请重试');
                }
            });
        }
    });

    // 默认加载控制台页面
    $('.nav-sidebar li:first-child a').click();
});
