
// src/main/java/SendVerificationCodeServlet.java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;

@WebServlet("/sendVerificationCode")
public class SendVerificationCodeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        try {
            String email = req.getParameter("email");

            // 检查邮箱是否已存在
            String checkSql = "select * from user where email=?";
            User existUser = JDBCUtil.queryForObject(checkSql, User.class, email);

            if (existUser != null) {
                // 用户已存在，返回错误信息
                resp.getWriter().write("{\"success\":false,\"message\":\"邮箱已存在，请使用其他邮箱!\"}");
                return;
            }

            // 生成6位随机验证码
            String verificationCode = generateVerificationCode();

            // 将验证码存储在会话中，同时记录发送时间
            HttpSession session = req.getSession();
            session.setAttribute("verificationCode", verificationCode);
            session.setAttribute("verificationEmail", email);
            session.setAttribute("verificationTime", System.currentTimeMillis());

            // 发送验证码到用户邮箱
            EmailUtil.sendVerificationCode(email, verificationCode);

            resp.getWriter().write("{\"success\":true,\"message\":\"验证码已发送，请查收邮件!\"}");
        } catch (Exception e) {
            resp.getWriter().write("{\"success\":false,\"message\":\"发送验证码失败: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
