<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理员控制台</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="./vendors/bootstrap-3.4.1/dist/css/bootstrap.min.css">
    <!-- 自定义 CSS -->
    <link rel="stylesheet" href="./assets/css/admin.css">
</head>
<body>
    <!-- 导航栏 -->
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">电商管理系统</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#">欢迎, ${sessionScope.user.userName}</a></li>
                    <li><a href="#" id="logout-btn">退出登录</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- 主体内容 -->
    <div class="container-fluid">
        <div class="row">
            <!-- 侧边栏 -->
            <div class="col-sm-3 col-md-2 sidebar">
                <ul class="nav nav-sidebar">
                    <li class="active"><a href="#" data-toggle="tab" data-target="#dashboard">控制台首页</a></li>
                    <li><a href="#" data-toggle="tab" data-target="#users">用户管理</a></li>
                    <li><a href="#" data-toggle="tab" data-target="#products">商品管理</a></li>
                </ul>
            </div>

            <!-- 主内容区 -->
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <div class="tab-content">
                    <!-- 控制台首页 -->
                    <div class="tab-pane active" id="dashboard">
                        <h1 class="page-header">控制台首页</h1>
                        <div class="row placeholders">
                            <div class="col-xs-6 col-sm-3 placeholder">
                                <div class="panel panel-primary">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">用户总数</h3>
                                    </div>
                                    <div class="panel-body">
                                        <h4 id="user-count">加载中...</h4>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6 col-sm-3 placeholder">
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">商品总数</h3>
                                    </div>
                                    <div class="panel-body">
                                        <h4 id="product-count">加载中...</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 用户管理 -->
                    <div class="tab-pane" id="users">
                        <h1 class="page-header">用户管理</h1>
                        <div class="table-responsive">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">用户列表</h3>
                                </div>
                                <div class="panel-body">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>用户名</th>
                                                <th>邮箱</th>
                                                <th>用户类型</th>
                                                <th>注册时间</th>
                                                <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody id="users-table">
                                            <!-- 用户数据将通过AJAX加载 -->
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 商品管理 -->
                    <div class="tab-pane" id="products">
                        <h1 class="page-header">商品管理</h1>
                        <div class="row">
                            <div class="col-md-12">
                                <button class="btn btn-primary pull-right" id="add-product-btn">添加商品</button>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">商品列表</h3>
                                </div>
                                <div class="panel-body">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>商品名称</th>
                                                <th>价格</th>
                                                <th>库存</th>
                                                <th>分类</th>
                                                <th>状态</th>
                                                <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody id="products-table">
                                            <!-- 商品数据将通过AJAX加载 -->
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 添加/编辑商品的模态框 -->
    <div class="modal fade" id="productModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                    <h4 class="modal-title" id="productModalTitle">添加商品</h4>
                </div>
                <div class="modal-body">
                    <form id="productForm">
                        <input type="hidden" id="product-id">
                        <div class="form-group">
                            <label for="product-name">商品名称</label>
                            <input type="text" class="form-control" id="product-name" required>
                        </div>
                        <div class="form-group">
                            <label for="product-description">商品描述</label>
                            <textarea class="form-control" id="product-description" rows="3" required></textarea>
                        </div>
                        <div class="form-group">
                            <label for="product-price">价格</label>
                            <input type="number" step="0.01" class="form-control" id="product-price" required>
                        </div>
                        <div class="form-group">
                            <label for="product-stock">库存</label>
                            <input type="number" class="form-control" id="product-stock" required>
                        </div>
                        <div class="form-group">
                            <label for="product-category">分类</label>
                            <select class="form-control" id="product-category" required>
                                <option value="数码">数码</option>
                                <option value="家具">家具</option>
                                <option value="家电">家电</option>
                                <option value="服装">服装</option>
                                <option value="美妆">美妆</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="product-image">商品图片URL</label>
                            <input type="text" class="form-control" id="product-image" required>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" id="product-active" checked> 是否激活
                            </label>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="save-product-btn">保存</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 编辑用户的模态框 -->
    <div class="modal fade" id="userModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                    <h4 class="modal-title" id="userModalTitle">编辑用户</h4>
                </div>
                <div class="modal-body">
                    <form id="userForm">
                        <input type="hidden" id="user-id">
                        <div class="form-group">
                            <label for="user-name">用户名</label>
                            <input type="text" class="form-control" id="user-name" required>
                        </div>
                        <div class="form-group">
                            <label for="user-email">邮箱</label>
                            <input type="email" class="form-control" id="user-email" required>
                        </div>
                        <div class="form-group">
                            <label for="user-type">用户类型</label>
                            <select class="form-control" id="user-type" required>
                                <option value="user">普通用户</option>
                                <option value="admin">管理员</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="save-user-btn">保存</button>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script src="./vendors/jquery-3.6.0.js"></script>
    <script src="./vendors/bootstrap-3.4.1/dist/js/bootstrap.min.js"></script>
    <script src="./assets/js/admin.js"></script>
</body>
</html>
