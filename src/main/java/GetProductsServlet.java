import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// import com.google.gson.Gson;

@WebServlet("/getProducts")
public class GetProductsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 使用JDBCUtil获取商品列表
            List<Products> productsList = JDBCUtil.queryAll(
                    "SELECT * FROM products WHERE is_active = 1",
                    Products.class);

            // 将商品列表存入request
            request.setAttribute("products", productsList);

            // 获取当前登录用户的购物车数据
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                // 获取购物车数据
                List<GetCartItemsServlet.CartItem> cartItems = GetCartItemsServlet.getCartItems(user.getUser_id());
                request.setAttribute("cartItems", cartItems);
            }

            // 转发到JSP页面
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
