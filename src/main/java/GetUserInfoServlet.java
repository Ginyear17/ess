import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/getUserInfo")
public class GetUserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        try {
            String email = req.getParameter("email");

            // 检查完整的SQL语句，确保查询所有字段
            String sql = "select * from user where email=?";
            User user = JDBCUtil.queryForObject(sql, User.class, email);

            if (user != null) {
                // 打印用户对象信息，便于调试
                System.out.println("User found: " + user.getEmail() + ", " + user.getUserName());

                // 创建包含完整用户信息的JSON对象
                Gson gson = new Gson();
                String userJson = gson.toJson(user);

                // 返回成功响应和用户数据
                resp.getWriter().write("{\"success\":true,\"user\":" + userJson + "}");
            } else {
                resp.getWriter().write("{\"success\":false,\"message\":\"未找到该用户信息\"}");
            }
        } catch (Exception e) {
            resp.getWriter().write("{\"success\":false,\"message\":\"服务器处理异常: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}
