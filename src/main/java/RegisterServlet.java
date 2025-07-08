import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 编码设置
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        try {
            // 获取请求参数
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirmPassword");
            String username = req.getParameter("username"); // 获取前端传来的用户名

            // 验证密码是否匹配
            if (!password.equals(confirmPassword)) {
                resp.getWriter().write("{\"success\":false,\"message\":\"两次输入的密码不一致!\"}");
                return;
            }

            // 检查用户名是否已存在
            String checkUsernameSql = "select * from user where user_name=?";
            User existUserByName = JDBCUtil.queryForObject(checkUsernameSql, User.class, username);

            if (existUserByName != null) {
                // 用户名已存在，返回错误信息
                resp.getWriter().write("{\"success\":false,\"message\":\"用户名已存在，请使用其他用户名!\"}");
                return;
            }

            // 检查邮箱是否已存在
            String checkSql = "select * from user where email=?";
            User existUser = JDBCUtil.queryForObject(checkSql, User.class, email);

            if (existUser != null) {
                // 用户已存在，返回错误信息
                resp.getWriter().write("{\"success\":false,\"message\":\"邮箱已存在，请使用其他邮箱!\"}");
            } else {
                // 用户不存在，执行注册操作
                String insertSql = "insert into user(email, user_name, password) values(?, ?, ?)";
                // 使用邮箱前缀作为默认用户名
                // 判断用户名是否为空，为空时使用邮箱前缀作为默认用户名
                if (username == null || username.trim().isEmpty()) {
                    username = email.split("@")[0];
                }
                int result = JDBCUtil.update(insertSql, email, username, password);

                if (result > 0) {
                    // 注册成功
                    resp.getWriter().write("{\"success\":true,\"message\":\"注册成功!\"}");
                } else {
                    // 注册失败
                    resp.getWriter().write("{\"success\":false,\"message\":\"注册失败，请稍后重试!\"}");
                }
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
