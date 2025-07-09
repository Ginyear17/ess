import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/getCartItems")
public class GetCartItemsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // 用户未登录，返回空列表
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("[]");
            return;
        }

        try {
            // 查询购物车数据
            List<CartItem> cartItems = getCartItems(user.getUser_id());

            // 将购物车数据放入request
            request.setAttribute("cartItems", cartItems);

            // 转发到JSP页面或返回JSON数据
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = new Gson().toJson(cartItems);
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // 获取购物车数据的方法
    public static List<CartItem> getCartItems(int userId) throws Exception {
        List<CartItem> cartItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            // 修改SQL查询，加入商品价格字段
            String sql = "SELECT sc.cart_id, sc.user_id, sc.product_id, sc.quantity, sc.added_at, " +
                    "p.product_name, p.product_price " + // 添加商品价格
                    "FROM shopping_cart sc " +
                    "JOIN products p ON sc.product_id = p.product_id " +
                    "WHERE sc.user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCart_id(rs.getInt("cart_id"));
                item.setUser_id(rs.getInt("user_id"));
                item.setProduct_id(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setAdded_at(rs.getTimestamp("added_at"));
                item.setProduct_name(rs.getString("product_name"));
                item.setProduct_price(rs.getDouble("product_price")); // 设置商品价格

                cartItems.add(item);
            }
        } finally {
            // 关闭资源
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }

        return cartItems;
    }

    // 内部静态类用于表示购物车项
    public static class CartItem {
        private int cart_id;
        private int user_id;
        private int product_id;
        private String product_name;
        private double product_price;
        private int quantity;
        private java.sql.Timestamp added_at;

        // Getters and Setters
        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public double getProduct_price() {
            return product_price;
        }

        public void setProduct_price(double product_price) {
            this.product_price = product_price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public java.sql.Timestamp getAdded_at() {
            return added_at;
        }

        public void setAdded_at(java.sql.Timestamp added_at) {
            this.added_at = added_at;
        }
    }

}
