import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 统计用户数量
            String userCountSql = "SELECT COUNT(user_id) FROM user";
            int userCount = JDBCUtil.queryForCount(userCountSql);

            // 统计商品数量
            String productCountSql = "SELECT SUM(product_stock) FROM products";
            int productCount = JDBCUtil.queryForCount(productCountSql);

            // 创建响应对象
            Map<String, Integer> data = new HashMap<>();
            data.put("userCount", userCount);
            data.put("productCount", productCount);

            // 返回JSON响应
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new Gson().toJson(data, response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}