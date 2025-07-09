import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/users")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Map<String, Object>> users = new ArrayList<>();
            String sql = "SELECT user_id, email, password, user_name, user_type, created_at, avatar_url FROM user";

            try (Connection conn = JDBCUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", rs.getInt("user_id"));
                    user.put("email", rs.getString("email"));
                    user.put("password", rs.getString("password")); // 获取密码，但不建议在前端显示
                    user.put("userName", rs.getString("user_name"));
                    user.put("userType", rs.getString("user_type"));
                    user.put("createdAt", rs.getTimestamp("created_at"));
                    user.put("avatarUrl", rs.getString("avatar_url"));
                    users.add(user);
                }
            }

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); // 设置日期格式
            String json = gson.toJson(users);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "获取用户数据失败");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            // 更新用户的代码 (与之前相同)
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String userName = request.getParameter("userName");
                String userEmail = request.getParameter("userEmail");
                String userType = request.getParameter("userType");

                String sql = "UPDATE user SET user_name=?, email=?, user_type=? WHERE user_id=?";
                try (Connection conn = JDBCUtil.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setString(1, userName);
                    ps.setString(2, userEmail);
                    ps.setString(3, userType);
                    ps.setInt(4, userId);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("{\"message\": \"用户更新成功\"}");
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "用户更新失败");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "用户更新失败");
            }
        } else if ("delete".equals(action)) {
            // 删除用户的代码
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));

                String sql = "DELETE FROM user WHERE user_id=?";
                try (Connection conn = JDBCUtil.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setInt(1, userId);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("{\"message\": \"用户删除成功\"}");
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "用户删除失败");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "用户删除失败");
            }
        }
    }
}