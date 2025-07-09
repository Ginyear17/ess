import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import java.sql.Statement;

@WebServlet("/placeOrder")
public class PlaceOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // 添加此行设置响应编码
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        HttpSession session = request.getSession();
        // 修改后
        User user = (User) session.getAttribute("user");
        if (user == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "用户未登录");
            out.print(jsonResponse.toString());
            return;
        }
        int userId = user.getUser_id(); // 或 user.getUserId() 取决于实际使用的方法

        // 解析前端发送的JSON数据
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }
        JSONObject jsonData = new JSONObject(sb.toString());

        // 获取订单基本信息
        String recipient = jsonData.getString("recipient");
        String phone = jsonData.getString("phone");
        String address = jsonData.getString("address");
        String paymentMethod = jsonData.getString("paymentMethod");

        Connection conn = null;
        PreparedStatement pstmtGetCart = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtUpdateStock = null;
        PreparedStatement pstmtClearCart = null;
        ResultSet rs = null;
        int firstOrderId = -1;

        try {
            conn = JDBCUtil.getConnection();
            conn.setAutoCommit(false); // 开始事务

            // 1. 获取购物车中的商品
            pstmtGetCart = conn.prepareStatement(
                    "SELECT c.product_id, p.product_name, p.product_price, c.quantity " +
                            "FROM shopping_cart c " +
                            "JOIN products p ON c.product_id = p.product_id " +
                            "WHERE c.user_id = ?");

            pstmtGetCart.setInt(1, userId);
            rs = pstmtGetCart.executeQuery();

            List<CartItem> cartItems = new ArrayList<>();

            // 检查购物车是否为空
            boolean hasItems = false;
            while (rs.next()) {
                hasItems = true;
                hasItems = true;
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name"); // 更新列名
                BigDecimal price = rs.getBigDecimal("product_price"); // 更新列名
                int quantity = rs.getInt("quantity");
                BigDecimal subtotal = price.multiply(new BigDecimal(quantity));

                CartItem item = new CartItem();
                item.setProductId(productId);
                item.setProductName(productName);
                item.setPrice(price);
                item.setQuantity(quantity);
                item.setSubtotal(subtotal);

                cartItems.add(item);
            }

            if (!hasItems) {
                conn.rollback();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "购物车为空");
                out.print(jsonResponse.toString());
                return;
            }

            // 2. 为每个购物车商品创建单独的订单记录
            // 修改后的SQL语句，移除了total
            String insertOrderSql = "INSERT INTO orders (user_id, product_id, order_date, price, quantity, real_name, phone, address, payment_method) "
                    +
                    "VALUES (?, ?, NOW(), ?, ?, ?, ?, ?, ?)";

            pstmtOrder = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);

            // 3. 更新商品库存
            String updateStockSql = "UPDATE products SET product_stock = product_stock - ? WHERE product_id = ? AND product_stock >= ?";

            pstmtUpdateStock = conn.prepareStatement(updateStockSql);

            // 处理每个购物车项
            boolean firstOrder = true;
            for (CartItem item : cartItems) {
                // 添加到orders表
                pstmtOrder.setInt(1, userId);
                pstmtOrder.setInt(2, item.getProductId());
                pstmtOrder.setBigDecimal(3, item.getPrice());
                pstmtOrder.setInt(4, item.getQuantity());
                // pstmtOrder.setBigDecimal(5, item.getSubtotal());
                pstmtOrder.setString(5, recipient);
                pstmtOrder.setString(6, phone);
                pstmtOrder.setString(7, address);
                pstmtOrder.setString(8, paymentMethod);

                int affectedRows = pstmtOrder.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "创建订单失败");
                    out.print(jsonResponse.toString());
                    return;
                }

                // 记录第一个订单ID用于返回
                if (firstOrder) {
                    ResultSet generatedKeys = pstmtOrder.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        firstOrderId = generatedKeys.getInt(1);
                        firstOrder = false;
                    }
                }

                // 更新库存
                pstmtUpdateStock.setInt(1, item.getQuantity());
                pstmtUpdateStock.setInt(2, item.getProductId());
                pstmtUpdateStock.setInt(3, item.getQuantity());

                int stockUpdated = pstmtUpdateStock.executeUpdate();
                if (stockUpdated == 0) {
                    // 库存不足
                    conn.rollback();
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "商品" + item.getProductName() + "库存不足");
                    out.print(jsonResponse.toString());
                    return;
                }
            }

            // 4. 清空用户购物车
            pstmtClearCart = conn.prepareStatement("DELETE FROM shopping_cart WHERE user_id = ?");
            pstmtClearCart.setInt(1, userId);
            pstmtClearCart.executeUpdate();

            // 提交事务
            conn.commit();

            // 返回成功响应
            jsonResponse.put("success", true);
            jsonResponse.put("orderId", firstOrderId);
            out.print(jsonResponse.toString());

        } catch (Exception e) {
            // 回滚事务
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "处理订单时发生错误: " + e.getMessage());
            out.print(jsonResponse.toString());

        } finally {
            // 关闭资源
            JDBCUtil.close(conn, pstmtOrder, rs);
            JDBCUtil.close(null, pstmtGetCart, null);
            JDBCUtil.close(null, pstmtClearCart, null);
            JDBCUtil.close(null, pstmtUpdateStock, null);
        }
    }

    // 购物车项内部类保持不变
    // 购物车项内部类
    private static class CartItem {
        private int productId;
        private String productName;
        private BigDecimal price;
        private int quantity;
        private BigDecimal subtotal;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }
    }

}
