import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/admin/orders")
public class OrdersServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://192.168.159.228:3306/electronic_mall?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            List<Orders> orders = getAllOrders(conn);
            JSONArray jsonArray = new JSONArray();

            for (Orders order : orders) {
                JSONObject json = new JSONObject();
                json.put("order_id", order.getOrderId());
                json.put("user_id", order.getUserId());
                json.put("product_id", order.getProductId());
                json.put("order_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getOrderDate()));
                json.put("price", order.getPrice());
                json.put("quantity", order.getQuantity());
                json.put("total", order.getTotal());
                json.put("real_name", order.getRealName());
                json.put("phone", order.getPhone());
                json.put("address", order.getAddress());
                json.put("payment_method", order.getPaymentMethod());
                jsonArray.put(json);
            }

            response.getWriter().write(jsonArray.toString());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"数据库错误: " + e.getMessage() + "\"}");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            if ("update".equals(action)) {
                updateOrder(conn, request, response);
            } else if ("delete".equals(action)) {
                deleteOrder(conn, request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"无效的操作类型\"}");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"数据库错误: " + e.getMessage() + "\"}");
        }
    }

    private List<Orders> getAllOrders(Connection conn) throws SQLException {
        List<Orders> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setProductId(rs.getInt("product_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setPrice(rs.getBigDecimal("price"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotal(rs.getBigDecimal("total"));
                order.setRealName(rs.getString("real_name"));
                order.setPhone(rs.getString("phone"));
                order.setAddress(rs.getString("address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                orders.add(order);
            }
        }
        return orders;
    }

    private void updateOrder(Connection conn, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int orderId = Integer.parseInt(request.getParameter("order_id"));
        int userId = Integer.parseInt(request.getParameter("user_id"));
        int productId = Integer.parseInt(request.getParameter("product_id"));
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String realName = request.getParameter("real_name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("payment_method");

        String sql = "UPDATE orders SET user_id = ?, product_id = ?, price = ?, quantity = ?, " +
                "real_name = ?, phone = ?, address = ?, payment_method = ? WHERE order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            pstmt.setBigDecimal(3, price);
            pstmt.setInt(4, quantity);
            pstmt.setString(5, realName);
            pstmt.setString(6, phone);
            pstmt.setString(7, address);
            pstmt.setString(8, paymentMethod);
            pstmt.setInt(9, orderId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                response.getWriter().write("{\"success\":\"订单更新成功\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"订单不存在\"}");
            }
        }
    }

    private void deleteOrder(Connection conn, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int orderId = Integer.parseInt(request.getParameter("order_id"));

        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                response.getWriter().write("{\"success\":\"订单删除成功\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"订单不存在\"}");
            }
        }
    }
}