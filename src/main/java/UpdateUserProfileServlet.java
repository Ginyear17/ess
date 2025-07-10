import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import org.json.JSONObject;

@WebServlet("/updateUserProfile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 5 * 1024 * 1024, // 5MB
        maxRequestSize = 10 * 1024 * 1024 // 10MB
)
public class UpdateUserProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        // 获取会话中的用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "用户未登录");
            out.print(jsonResponse.toString());
            return;
        }

        try {
            // 获取表单数据
            String username = request.getParameter("username");
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");

            // 获取头像文件
            Part filePart = request.getPart("avatar");
            String avatarUrl = null;

            // 处理头像上传
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = user.getUserId() + "_" + System.currentTimeMillis() + getFileExtension(filePart);
                String uploadPath = getServletContext().getRealPath("/assets/images/avatars");
                System.out.println(getServletContext().getRealPath("/"));
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists())
                    uploadDir.mkdir();

                String filePath = uploadPath + File.separator + fileName;
                filePart.write(filePath);

                // 设置相对路径
                avatarUrl = "assets/images/avatars/" + fileName;
            }

            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                conn = JDBCUtil.getConnection();

                // 如果提供了当前密码，验证密码
                if (currentPassword != null && !currentPassword.isEmpty()) {
                    String checkSql = "SELECT password FROM user WHERE user_id = ?";
                    pstmt = conn.prepareStatement(checkSql);
                    pstmt.setInt(1, user.getUserId());
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        if (!storedPassword.equals(currentPassword)) {
                            jsonResponse.put("success", false);
                            jsonResponse.put("message", "当前密码不正确");
                            out.print(jsonResponse.toString());
                            return;
                        }
                    }
                    pstmt.close();
                }

                // 构建更新SQL
                StringBuilder updateSql = new StringBuilder("UPDATE user SET ");
                boolean hasSetClause = false;

                if (username != null && !username.isEmpty()) {
                    updateSql.append("user_name = ?");
                    hasSetClause = true;
                }

                if (newPassword != null && !newPassword.isEmpty()) {
                    if (hasSetClause) {
                        updateSql.append(", ");
                    }
                    updateSql.append("password = ?");
                    hasSetClause = true;
                }

                if (avatarUrl != null) {
                    if (hasSetClause) {
                        updateSql.append(", ");
                    }
                    updateSql.append("avatar_url = ?");
                    hasSetClause = true;
                }

                updateSql.append(" WHERE user_id = ?");

                pstmt = conn.prepareStatement(updateSql.toString());

                int paramIndex = 1;
                if (username != null && !username.isEmpty()) {
                    pstmt.setString(paramIndex++, username);
                    user.setUserName(username);
                }

                if (newPassword != null && !newPassword.isEmpty()) {
                    pstmt.setString(paramIndex++, newPassword);
                }

                if (avatarUrl != null) {
                    pstmt.setString(paramIndex++, avatarUrl);
                    user.setAvatarUrl(avatarUrl);
                }

                pstmt.setInt(paramIndex, user.getUserId());

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    // 更新会话中的用户信息
                    session.setAttribute("user", user);

                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "个人信息更新成功");
                    if (avatarUrl != null) {
                        jsonResponse.put("avatarUrl", avatarUrl);
                    }
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "更新失败，请稍后再试");
                }

            } finally {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "系统错误: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
    }

    private String getFileExtension(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                String fileName = item.substring(item.indexOf("=") + 2, item.length() - 1);
                if (fileName.contains(".")) {
                    return fileName.substring(fileName.lastIndexOf("."));
                } else {
                    return "";
                }
            }
        }
        return "";
    }
}
