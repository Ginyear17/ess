<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理员控制台</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="./assets/css/admin.css">
</head>
<body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#sidebar" aria-expanded="false" aria-controls="sidebar">
                    <span class="sr-only">切换导航</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">ESS 管理系统</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#"><i class="fas fa-bell"></i> 通知</a></li>
                    <li><a href="#" id="theme-toggle"><i class="fas fa-sun"></i> 主题</a></li>
                    <li><a href="getProducts"><i class="fas fa-sign-out-alt"></i> 返回商城</a></li>
                </ul>
                <form class="navbar-form navbar-right">
                    <input type="text" class="form-control" placeholder="搜索...">
                </form>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-3 col-md-2 sidebar" id="sidebar">
                <ul class="nav nav-sidebar">
                    <li class="active"><a href="#dashboard" data-toggle="tab"><i class="fas fa-tachometer-alt"></i> 仪表盘</a></li>
                    <li><a href="#products" data-toggle="tab"><i class="fas fa-box"></i> 商品管理</a></li>
                    <li><a href="#users" data-toggle="tab"><i class="fas fa-users"></i> 用户管理</a></li>
                    <li><a href="#orders" data-toggle="tab"><i class="fas fa-shopping-cart"></i> 订单管理</a></li>
                </ul>
            </div>

            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <div class="tab-content">
                    <!-- 仪表盘 -->
                    <div class="tab-pane active" id="dashboard">
                        <h1 class="page-header">仪表盘</h1>
                        <div class="row placeholders">
                            <div class="col-xs-6 col-sm-3 placeholder">
                                <div class="panel panel-primary">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">总销售额</h3>
                                    </div>
                                    <div class="panel-body">
                                        <h4>¥ 28,350</h4>
                                        <span class="text-success"><i class="fas fa-arrow-up"></i> 15%</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6 col-sm-3 placeholder">
                                <div class="panel panel-info">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">总订单数</h3>
                                    </div>
                                    <div class="panel-body">
                                        <h4>157</h4>
                                        <span class="text-success"><i class="fas fa-arrow-up"></i> 8%</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6 col-sm-3 placeholder">
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">总用户数</h3>
                                    </div>
                                    <div class="panel-body">
                                        <h4>85</h4>
                                        <span class="text-success"><i class="fas fa-arrow-up"></i> 12%</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6 col-sm-3 placeholder">
                                <div class="panel panel-warning">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">库存警告</h3>
                                    </div>
                                    <div class="panel-body">
                                        <h4>5</h4>
                                        <span class="text-danger"><i class="fas fa-exclamation-triangle"></i> 需关注</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <h2 class="sub-header">热门商品</h2>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>商品名称</th>
                                        <th>类别</th>
                                        <th>销量</th>
                                        <th>收入</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>1</td>
                                        <td>智能手表</td>
                                        <td>数码</td>
                                        <td>45</td>
                                        <td>¥ 9,450</td>
                                    </tr>
                                    <tr>
                                        <td>2</td>
                                        <td>无线耳机</td>
                                        <td>数码</td>
                                        <td>38</td>
                                        <td>¥ 5,320</td>
                                    </tr>
                                    <tr>
                                        <td>3</td>
                                        <td>化妆品套装</td>
                                        <td>美妆</td>
                                        <td>32</td>
                                        <td>¥ 4,800</td>
                                    </tr>
                                    <tr>
                                        <td>4</td>
                                        <td>沙发</td>
                                        <td>家具</td>
                                        <td>12</td>
                                        <td>¥ 8,400</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- 商品管理 -->
                    <div class="tab-pane" id="products">
                        <h1 class="page-header">商品管理</h1>
                        
                        <div class="product-actions">
                            <button class="btn btn-primary" id="add-product-btn">
                                <i class="fas fa-plus"></i> 添加新商品
                            </button>
                            <div class="form-inline pull-right">
                                <div class="form-group">
                                    <select class="form-control" id="category-filter">
                                        <option value="">所有类别</option>
                                        <option value="数码">数码</option>
                                        <option value="家电">家电</option>
                                        <option value="家具">家具</option>
                                        <option value="服装">服装</option>
                                        <option value="美妆">美妆</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <input type="text" class="form-control" id="product-search" placeholder="搜索商品...">
                                </div>
                            </div>
                        </div>
                        
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>图片</th>
                                        <th>商品名称</th>
                                        <th>价格</th>
                                        <th>库存</th>
                                        <th>类别</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody id="product-list">
                                    <!-- 商品数据会通过AJAX加载 -->
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- 用户管理 -->
                    <div class="tab-pane" id="users">
                        <h1 class="page-header">用户管理</h1>
                        <div class="form-inline pull-right user-search">
                            <div class="form-group">
                                <input type="text" class="form-control" id="user-search" placeholder="搜索用户...">
                                <button class="btn btn-primary" id="search-user-btn">搜索</button>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>用户名</th>
                                        <th>邮箱</th>
                                        <th>注册时间</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody id="user-list">
                                    <!-- 用户数据会通过AJAX加载 -->
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- 订单管理 -->
                    <div class="tab-pane" id="orders">
                        <h1 class="page-header">订单管理</h1>
                        <div class="order-filters">
                            <div class="form-inline pull-right">
                                <div class="form-group">
                                    <select class="form-control" id="order-status">
                                        <option value="">所有状态</option>
                                        <option value="待付款">待付款</option>
                                        <option value="待发货">待发货</option>
                                        <option value="已发货">已发货</option>
                                        <option value="已完成">已完成</option>
                                        <option value="已取消">已取消</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <input type="text" class="form-control" id="order-search" placeholder="订单号/用户...">
                                    <button class="btn btn-primary" id="search-order-btn">搜索</button>
                                </div>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>订单号</th>
                                        <th>客户</th>
                                        <th>订单日期</th>
                                        <th>金额</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody id="order-list">
                                    <!-- 订单数据会通过AJAX加载 -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

<!-- 添加商品模态框 -->
<div class="modal fade" id="add-product-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                <h4 class="modal-title">添加新商品</h4>
            </div>
            <div class="modal-body">
                <form id="add-product-form">
                    <div class="form-group">
                        <label>商品名称</label>
                        <input type="text" class="form-control" name="product_name" required>
                    </div>
                    <div class="form-group">
                        <label>商品描述</label>
                        <textarea class="form-control" name="product_description" rows="3" required></textarea>
                    </div>
                    <div class="form-group">
                        <label>价格</label>
                        <input type="number" step="0.01" class="form-control" name="product_price" required>
                    </div>
                    <div class="form-group">
                        <label>库存数量</label>
                        <input type="number" class="form-control" name="product_stock_quantity" required>
                    </div>
                    <div class="form-group">
                        <label>类别</label>
                        <select class="form-control" name="category" required>
                            <option value="">请选择类别</option>
                            <option value="数码">数码</option>
                            <option value="家电">家电</option>
                            <option value="家具">家具</option>
                            <option value="服装">服装</option>
                            <option value="美妆">美妆</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>商品图片</label>
                        <input type="file" class="form-control" name="product_image" accept="image/*">
                        <p class="help-block">支持jpg、png格式图片</p>
                    </div>
                    <div class="form-group">
                        <label>状态</label>
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="is_active" checked> 上架商品
                            </label>
                        </div>
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

<!-- 编辑商品模态框 -->
<div class="modal fade" id="edit-product-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                <h4 class="modal-title">编辑商品</h4>
            </div>
            <div class="modal-body">
                <form id="edit-product-form">
                    <input type="hidden" name="product_id">
                    <div class="form-group">
                        <label>商品名称</label>
                        <input type="text" class="form-control" name="product_name" required>
                    </div>
                    <div class="form-group">
                        <label>商品描述</label>
                        <textarea class="form-control" name="product_description" rows="3" required></textarea>
                    </div>
                    <div class="form-group">
                        <label>价格</label>
                        <input type="number" step="0.01" class="form-control" name="product_price" required>
                    </div>
                    <div class="form-group">
                        <label>库存数量</label>
                        <input type="number" class="form-control" name="product_stock_quantity" required>
                    </div>
                    <div class="form-group">
                        <label>类别</label>
                        <select class="form-control" name="category" required>
                            <option value="">请选择类别</option>
                            <option value="数码">数码</option>
                            <option value="家电">家电</option>
                            <option value="家具">家具</option>
                            <option value="服装">服装</option>
                            <option value="美妆">美妆</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>当前图片</label>
                        <img id="current-product-image" src="" class="img-thumbnail" style="max-height: 100px;">
                    </div>
                    <div class="form-group">
                        <label>更新图片</label>
                        <input type="file" class="form-control" name="product_image" accept="image/*">
                        <p class="help-block">支持jpg、png格式图片</p>
                    </div>
                    <div class="form-group">
                        <label>状态</label>
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="is_active"> 上架商品
                            </label>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="update-product-btn">保存修改</button>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript库引用 -->
<script>
window.baseUrl = './';  // 根目录不需要前缀
</script>
<script src="vendors/jquery-3.6.0.js"></script>
<script src="vendors/bootstrap-3.4.1/js/bootstrap.min.js"></script>
<script src="assets/js/admin.js"></script>

</body>
</html>
