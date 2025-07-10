import java.io.IOException;
import java.io.PrintWriter;
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
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/getUserOrders")
public class GetUserOrdersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "用户未登录");
            out.print(jsonResponse.toString());
            return;
        }

        int userId = user.getUser_id();
        List<Orders> ordersList = getUserOrders(userId);

        JSONArray ordersArray = new JSONArray();
        for (Orders order : ordersList) {
            JSONObject orderObj = new JSONObject();
            orderObj.put("orderId", order.getOrderId());
            orderObj.put("orderDate", order.getOrderDate().toString());
            orderObj.put("productId", order.getProductId());
            orderObj.put("price", order.getPrice());
            orderObj.put("quantity", order.getQuantity());
            orderObj.put("total", order.getTotal());
            orderObj.put("realName", order.getReal_name());
            orderObj.put("phone", order.getPhone());
            orderObj.put("address", order.getAddress());
            orderObj.put("paymentMethod", order.getPayment_method());

            // 获取商品名称
            String productName = getProductName(order.getProductId());
            orderObj.put("productName", productName);

            ordersArray.put(orderObj);
        }

        jsonResponse.put("success", true);
        jsonResponse.put("orders", ordersArray);
        out.print(jsonResponse.toString());
    }

    private List<Orders> getUserOrders(int userId) {
        List<Orders> ordersList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Orders order = new Orders();
                order.setOrder_id(rs.getInt("order_id"));
                order.setUser_id(rs.getInt("user_id"));
                order.setProduct_id(rs.getInt("product_id"));
                order.setOrder_date(rs.getTimestamp("order_date"));
                order.setPrice(rs.getBigDecimal("price"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotal(rs.getBigDecimal("total"));
                order.setReal_name(rs.getString("real_name"));
                order.setPhone(rs.getString("phone"));
                order.setAddress(rs.getString("address"));
                order.setPayment_method(rs.getString("payment_method"));

                ordersList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }

        return ordersList;
    }

    private String getProductName(int productId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String productName = "未知商品";

        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT product_name FROM products WHERE product_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                productName = rs.getString("product_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }

        return productName;
    }
}
