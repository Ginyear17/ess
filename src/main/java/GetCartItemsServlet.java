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

@WebServlet("/getCartItems")
public class GetCartItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // 用户未登录，不需要处理
            return;
        }

        List<CartItem> cartItems = getCartItems(user.getUser_id());
        request.setAttribute("cartItems", cartItems);
    }

    private List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection conn = JDBCUtil.getConnection()) {
            String sql = "SELECT c.cart_id, c.product_id, c.quantity, p.product_name " +
                    "FROM shopping_cart c " +
                    "JOIN products p ON c.product_id = p.product_id " +
                    "WHERE c.user_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCart_id(rs.getInt("cart_id"));
                item.setProduct_id(rs.getInt("product_id"));
                item.setProduct_name(rs.getString("product_name"));
                item.setQuantity(rs.getInt("quantity"));

                cartItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    // 内部类，用于存储购物车项目信息
    public static class CartItem {
        private int cart_id;
        private int product_id;
        private String product_name;
        private int quantity;

        // Getters and setters
        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
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

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
