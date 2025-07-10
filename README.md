# ess
mysql -u root -p

CREATE USER 'root'@'LAPTOP-EAP4QBRN' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'LAPTOP-EAP4QBRN' WITH GRANT OPTION;
FLUSH PRIVILEGES;


# 电子商城销售管理系统 - 数据流说明

系统的数据流贯穿前端到后端、用户界面到数据库的完整流程，下面从几个关键业务场景分析整个系统的数据流。

## 1. 用户认证流程

### 1.1 登录流程
```
前端登录表单 → LoginServlet → 数据库验证 → Session存储用户信息 → 返回用户数据和购物车数据 → 前端更新UI
```

**详细流程**:
1. 用户在前端输入邮箱和密码
2. `login_register.js`将表单数据发送到`LoginServlet`
3. `LoginServlet`通过JDBC查询数据库验证用户
4. 验证成功后，将用户信息存入Session
5. 调用`GetCartItemsServlet.getCartItems()`获取用户购物车
6. 返回JSON响应，包含用户信息、管理员标识和购物车数据
7. 前端将用户信息存入`sessionStorage`并更新UI

### 1.2 退出登录流程
```
退出按钮点击 → logoutServlet → 清除Session → 前端清除本地存储 → 页面刷新
```

## 2. 商品展示流程

### 2.1 首页商品加载
```
访问首页 → GetProductsServlet → 从数据库获取商品列表 → 存入请求属性 → 转发到index.jsp → 页面渲染
```

**详细流程**:
1. 用户访问首页或网站根目录
2. `web.xml`配置的欢迎文件将请求转发到`GetProductsServlet`
3. `GetProductsServlet`从数据库获取所有商品
4. 将商品列表存入request属性
5. 转发请求到`index.jsp`
6. JSP使用JSTL标签遍历商品列表并渲染到页面

### 2.2 商品分类筛选
```
点击分类链接 → category.js处理 → 前端筛选显示对应分类商品
```

## 3. 购物车操作流程

### 3.1 添加商品到购物车
```
点击添加按钮 → AddToCartServlet → 更新数据库 → 返回更新后的购物车 → 前端更新UI
```

**详细流程**:
1. 用户点击商品卡片上的添加按钮
2. `shopping_cart.js`发送AJAX请求到`AddToCartServlet`
3. `AddToCartServlet`检查该商品是否已在购物车
   - 若存在：更新数量
   - 若不存在：创建新记录
4. 调用`GetCartItemsServlet.getCartItems()`获取更新后的购物车
5. 返回JSON响应，包含成功状态和购物车数据
6. 前端更新购物车UI和会话存储

### 3.2 更新购物车商品数量
```
点击+/-按钮 → UpdateCartServlet → 更新数据库 → 返回更新后购物车 → 前端更新UI
```

## 4. 订单处理流程

### 4.1 创建订单
```
点击结算 → 填写订单信息 → PlaceOrderServlet → 事务处理(创建订单、减库存、清空购物车) → 前端更新UI
```

**详细流程**:
1. 用户点击结算按钮，`checkout.js`显示结算模态框
2. 用户填写收货信息并确认订单
3. `checkout.js`发送AJAX请求到`PlaceOrderServlet`
4. `PlaceOrderServlet`开始数据库事务：
   - 获取用户购物车商品
   - 为每个商品创建订单记录
   - 更新商品库存
   - 清空用户购物车
   - 提交事务
5. 返回成功响应和订单ID
6. 前端清空购物车UI并显示成功消息

### 4.2 查看订单
```
点击"我的订单" → GetUserOrdersServlet → 查询数据库 → 返回订单列表 → 前端渲染订单
```

## 5. 用户信息管理流程

### 5.1 修改个人信息
```
填写个人信息表单 → UpdateUserProfileServlet → 更新数据库 → 返回更新状态 → 前端更新UI
```

## 6. 管理员功能流程

### 6.1 商品管理
```
访问管理页面 → ProductServlet → 数据库操作 → 返回JSON数据 → 前端管理界面渲染
```

### 6.2 用户管理
```
访问用户管理页面 → UserServlet → 数据库查询 → 返回用户列表 → 前端管理界面渲染
```

## 7. 字符编码处理

整个系统采用UTF-8字符编码，确保中文和特殊字符正确显示：
1. JSP页面设置: `<%@ page contentType="text/html;charset=UTF-8" language="java" %>`
2. Servlet响应设置: `response.setCharacterEncoding("UTF-8")`
3. AJAX请求设置: `contentType: 'application/json; charset=utf-8'`
4. 数据库连接设置: `jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=UTF-8`

此数据流设计确保了系统各组件间的有效通信和数据的一致性，支持电子商城的完整业务流程。