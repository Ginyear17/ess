import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logoutServlet") // 这里的 URL 应该与你前端 AJAX 请求的 URL 一致
public class logoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取 session
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 使 session 失效
            session.invalidate();
        }

        // 可以选择设置响应状态码
        response.setStatus(HttpServletResponse.SC_OK);
    }
}