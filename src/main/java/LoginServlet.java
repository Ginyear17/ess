import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 编码设置
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        try {
            // 获取请求参数
            String email = req.getParameter("email");
            String pd = req.getParameter("password");

            // 操作数据库
            String sql = "select * from user where email=? and password=?";
            System.out.println("接收到登录请求 - Email: " + email + ", Password: " + pd);
            User user = JDBCUtil.queryForObject(sql, User.class, email, pd);

            if (user != null) {
                // 登录成功，将用户信息存储在session中
                req.getSession().setAttribute("user", user);
                // 创建JSON响应
                Gson gson = new Gson();
                String userJson = gson.toJson(user);

                // 检查用户是否为管理员
                if (user != null && "admin".equals(user.getUserType())) {
                    // 用户是管理员，告知前端跳转到管理员页面
                    resp.getWriter().write("{\"success\":true,\"user\":" + userJson + ",\"isAdmin\":true}");
                } else {
                    // 用户是普通客户，返回普通响应
                    resp.getWriter().write("{\"success\":true,\"user\":" + userJson + ",\"isAdmin\":false}");
                }
            } else {
                // 登录失败
                resp.getWriter().write("{\"success\":false,\"message\":\"邮箱或密码有误!\"}");
            }
        } catch (Exception e) {
            // 处理所有异常，确保返回有效的JSON
            resp.getWriter().write("{\"success\":false,\"message\":\"服务器处理异常: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
