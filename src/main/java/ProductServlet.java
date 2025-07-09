import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/products")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Map<String, Object>> products = new ArrayList<>();
            String sql = "SELECT product_id, product_name, product_description, product_price, " +
                    "product_stock, category, image_url, is_active, created_at FROM products";

            try (Connection conn = JDBCUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", rs.getInt("product_id"));
                    product.put("productName", rs.getString("product_name"));
                    product.put("productDescription", rs.getString("product_description"));
                    product.put("productPrice", rs.getBigDecimal("product_price"));
                    product.put("productStock", rs.getInt("product_stock"));
                    product.put("category", rs.getString("category"));
                    product.put("imageUrl", rs.getString("image_url"));
                    product.put("isActive", rs.getBoolean("is_active"));
                    product.put("createdAt", rs.getTimestamp("created_at"));
                    products.add(product);
                }
            }

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String json = gson.toJson(products);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "获取商品数据失败");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("update".equals(action)) {
            updateProduct(request, response);
        } else if ("delete".equals(action)) {
            deleteProduct(request, response);
        } else if ("add".equals(action)) {
            addProduct(request, response);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String productName = request.getParameter("productName");
            String productDescription = request.getParameter("productDescription");
            BigDecimal productPrice = new BigDecimal(request.getParameter("productPrice"));
            int productStock = Integer.parseInt(request.getParameter("productStock"));
            String category = request.getParameter("category");
            String imageUrl = request.getParameter("imageUrl");
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

            String sql = "UPDATE products SET product_name=?, product_description=?, product_price=?, " +
                    "product_stock=?, category=?, image_url=?, is_active=? WHERE product_id=?";

            try (Connection conn = JDBCUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, productName);
                ps.setString(2, productDescription);
                ps.setBigDecimal(3, productPrice);
                ps.setInt(4, productStock);
                ps.setString(5, category);
                ps.setString(6, imageUrl);
                ps.setBoolean(7, isActive);
                ps.setInt(8, productId);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"商品更新成功\"}");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "商品更新失败");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "商品更新失败");
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));

            String sql = "DELETE FROM products WHERE product_id=?";
            try (Connection conn = JDBCUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, productId);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"商品删除成功\"}");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "商品删除失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "商品删除失败");
        }
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String productName = request.getParameter("productName");
            String productDescription = request.getParameter("productDescription");
            BigDecimal productPrice = new BigDecimal(request.getParameter("productPrice"));
            int productStock = Integer.parseInt(request.getParameter("productStock"));
            String category = request.getParameter("category");
            String imageUrl = request.getParameter("imageUrl");
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

            String sql = "INSERT INTO products (product_name, product_description, product_price, " +
                    "product_stock, category, image_url, is_active, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

            try (Connection conn = JDBCUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, productName);
                ps.setString(2, productDescription);
                ps.setBigDecimal(3, productPrice);
                ps.setInt(4, productStock);
                ps.setString(5, category);
                ps.setString(6, imageUrl);
                ps.setBoolean(7, isActive);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"商品添加成功\"}");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "商品添加失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "商品添加失败");
        }
    }
}