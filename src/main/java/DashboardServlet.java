import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            // 统计用户数量
            String userCountSql = "SELECT COUNT(*) FROM users";
            int userCount = JDBCUtil.queryForCount(userCountSql);

            // 统计商品种类数量
            String productCountSql = "SELECT COUNT(*) FROM products";
            int productCount = JDBCUtil.queryForCount(productCountSql);

            // 统计商品库存总数
            String productStockTotalSql = "SELECT SUM(product_stock) FROM products";
            int productStockTotal = JDBCUtil.queryForCount(productStockTotalSql);

            // 统计订单总数
            String orderCountSql = "SELECT COUNT(DISTINCT order_id) FROM orders";
            int orderCount = JDBCUtil.queryForCount(orderCountSql);

            jsonResponse.put("userCount", userCount);
            jsonResponse.put("productCount", productCount);
            jsonResponse.put("productStockTotal", productStockTotal);
            jsonResponse.put("orderCount", orderCount);

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            jsonResponse.put("error", e.getMessage());
            out.print(jsonResponse.toString());
        }
    }
}
