import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取当前登录的用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 用户未登录，返回需要登录的信息
        if (user == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"请先登录\"}");
            return;
        }

        // 获取商品ID和数量
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = 1; // 默认加入1个

        try {
            // 检查购物车是否已有该商品
            String checkSql = "SELECT * FROM shopping_cart WHERE user_id = ? AND product_id = ?";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, user.getUser_id());
            checkStmt.setInt(2, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // 商品已在购物车中，更新数量
                int currentQuantity = rs.getInt("quantity");
                String updateSql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, currentQuantity + quantity);
                updateStmt.setInt(2, user.getUser_id());
                updateStmt.setInt(3, productId);
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                // 商品不在购物车中，新增记录
                String insertSql = "INSERT INTO shopping_cart (user_id, product_id, quantity, added_at) VALUES (?, ?, ?, NOW())";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, user.getUser_id());
                insertStmt.setInt(2, productId);
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();
                insertStmt.close();
            }

            rs.close();
            checkStmt.close();
            conn.close();

            // 返回成功信息
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": true, \"message\": \"添加成功\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"添加失败: " + e.getMessage() + "\"}");
        }
    }
}
