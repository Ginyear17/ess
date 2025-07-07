import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/checkName")
public class CheckName extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 编码设置
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        // 1.获取前端提交过来的参数
        String name = req.getParameter("name");

        // 2.操作数据库
        String sql = "select * from user where user_name=?";
        User user = JDBCUtil.queryForObject(sql, User.class, name);

        // 3.响应结果
        if (user != null) {// 昵称不可用
            resp.getWriter().print("不可用");
        } else {
            resp.getWriter().print("可用");
        }
    }
}
