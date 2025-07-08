import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/updateCart")
public class UpdateCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"请先登录\"}");
            return;
        }

        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantityChange = Integer.parseInt(request.getParameter("quantityChange"));

        try {
            Connection conn = JDBCUtil.getConnection();

            // 首先获取当前数量
            String checkSql = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, user.getUser_id());
            checkStmt.setInt(2, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity + quantityChange;

                // 如果新数量小于等于0，则从购物车中移除该项
                if (newQuantity <= 0) {
                    String deleteSql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                    deleteStmt.setInt(1, user.getUser_id());
                    deleteStmt.setInt(2, productId);
                    deleteStmt.executeUpdate();
                    deleteStmt.close();
                } else {
                    // 更新数量
                    String updateSql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setInt(1, newQuantity);
                    updateStmt.setInt(2, user.getUser_id());
                    updateStmt.setInt(3, productId);
                    updateStmt.executeUpdate();
                    updateStmt.close();
                }
            }

            rs.close();
            checkStmt.close();

            // 更新购物车数据
            List<GetCartItemsServlet.CartItem> cartItems = GetCartItemsServlet.getCartItems(user.getUser_id());
            session.setAttribute("cartItems", cartItems);

            conn.close();

            // 返回成功信息
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"success\": true, \"message\": \"更新成功\", \"cartItems\": " + new Gson().toJson(cartItems) + "}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"更新失败: " + e.getMessage() + "\"}");
        }
    }
}
