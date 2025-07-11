import java.io.IOException;
import java.io.PrintWriter;
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

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/searchProducts")
public class SearchProductsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        if (keyword == null || keyword.trim().isEmpty()) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "请输入搜索关键词");
            out.print(jsonResponse.toString());
            return;
        }

        List<Products> searchResults = searchProductsByName(keyword);
        JSONArray productsArray = new JSONArray();

        for (Products product : searchResults) {
            JSONObject productObj = new JSONObject();
            productObj.put("productId", product.getProductId());
            productObj.put("productName", product.getProductName());
            productObj.put("productPrice", product.getProductPrice());
            productObj.put("category", product.getCategory());
            productObj.put("imageUrl", product.getImageUrl());
            productsArray.put(productObj);
        }

        jsonResponse.put("success", true);
        jsonResponse.put("products", productsArray);
        out.print(jsonResponse.toString());
    }

    private List<Products> searchProductsByName(String keyword) {
        List<Products> results = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM products WHERE product_name LIKE ? AND is_active = 1";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Products product = new Products();
                product.setProduct_id(rs.getInt("product_id"));
                product.setProduct_name(rs.getString("product_name"));
                product.setProduct_price(rs.getBigDecimal("product_price"));
                product.setCategory(rs.getString("category"));
                product.setImage_url(rs.getString("image_url"));
                results.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }

        return results;
    }
}
