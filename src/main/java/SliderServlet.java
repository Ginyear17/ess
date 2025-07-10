import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/admin/sliders")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 10 * 1024 * 1024, // 10MB
        maxRequestSize = 15 * 1024 * 1024 // 15MB
)
public class SliderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray = new JSONArray();

        try (Connection conn = JDBCUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM sliders ORDER BY position")) {

            while (rs.next()) {
                JSONObject slider = new JSONObject();
                slider.put("id", rs.getInt("id"));
                slider.put("imagePath", rs.getString("image_path"));
                slider.put("title", rs.getString("title"));
                slider.put("subtitle", rs.getString("subtitle"));
                slider.put("link", rs.getString("link"));
                slider.put("position", rs.getInt("position"));

                jsonArray.put(slider);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"加载轮播图失败\"}");
            return;
        }

        out.print(jsonArray.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addSlider(request, response);
        } else if ("update".equals(action)) {
            updateSlider(request, response);
        } else if ("delete".equals(action)) {
            deleteSlider(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"无效的操作\"}");
        }
    }

    private void addSlider(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        String title = request.getParameter("sliderTitle");
        String subtitle = request.getParameter("sliderSubtitle");
        String link = request.getParameter("sliderLink");
        if (link == null || link.isEmpty()) {
            link = "/";
        }

        Part filePart = request.getPart("sliderImage");
        if (filePart == null || filePart.getSize() <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "请选择要上传的图片");
            out.print(jsonResponse.toString());
            return;
        }

        // 处理文件上传
        String fileName = "slider_" + System.currentTimeMillis() + getFileExtension(filePart);
        String uploadPath = getServletContext().getRealPath("/assets/images/album");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        // 保存到数据库的相对路径
        String imagePath = "assets/images/album/" + fileName;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();

            // 获取当前最大position
            int position = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(position) as max_pos FROM sliders");
            if (rs.next()) {
                position = rs.getInt("max_pos") + 1;
            }
            rs.close();
            stmt.close();

            // 插入新的轮播图
            String sql = "INSERT INTO sliders (image_path, title, subtitle, link, position, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, imagePath);
            pstmt.setString(2, title);
            pstmt.setString(3, subtitle);
            pstmt.setString(4, link);
            pstmt.setInt(5, position);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "轮播图添加成功");
                jsonResponse.put("imagePath", imagePath);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "轮播图添加失败");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "数据库错误: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.print(jsonResponse.toString());
    }

    private void updateSlider(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        int sliderId = Integer.parseInt(request.getParameter("sliderId"));
        String title = request.getParameter("sliderTitle");
        String subtitle = request.getParameter("sliderSubtitle");
        String link = request.getParameter("sliderLink");
        if (link == null || link.isEmpty()) {
            link = "/";
        }

        Part filePart = request.getPart("sliderImage");
        String imagePath = null;

        // 如果上传了新图片，则处理图片
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = "slider_" + System.currentTimeMillis() + getFileExtension(filePart);
            String uploadPath = getServletContext().getRealPath("/assets/images/album");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);

            // 保存到数据库的相对路径
            imagePath = "assets/images/album/" + fileName;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();

            // 构建更新SQL
            StringBuilder sql = new StringBuilder("UPDATE sliders SET ");
            List<Object> params = new ArrayList<>();

            sql.append("title = ?, subtitle = ?, link = ?");
            params.add(title);
            params.add(subtitle);
            params.add(link);

            if (imagePath != null) {
                sql.append(", image_path = ?");
                params.add(imagePath);
            }

            sql.append(", updated_at = NOW() WHERE id = ?");
            params.add(sliderId);

            pstmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "轮播图更新成功");
                if (imagePath != null) {
                    jsonResponse.put("imagePath", imagePath);
                }
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "轮播图更新失败");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "数据库错误: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.print(jsonResponse.toString());
    }

    private void deleteSlider(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        int sliderId = Integer.parseInt(request.getParameter("sliderId"));

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();

            // 先获取图片路径，以便删除文件
            String sql = "SELECT image_path FROM sliders WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sliderId);
            ResultSet rs = pstmt.executeQuery();

            String imagePath = null;
            if (rs.next()) {
                imagePath = rs.getString("image_path");
            }

            rs.close();
            pstmt.close();

            // 从数据库中删除记录
            sql = "DELETE FROM sliders WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sliderId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // 尝试删除物理文件
                if (imagePath != null) {
                    String realPath = getServletContext().getRealPath("/" + imagePath);
                    File file = new File(realPath);
                    if (file.exists()) {
                        file.delete();
                    }
                }

                jsonResponse.put("success", true);
                jsonResponse.put("message", "轮播图删除成功");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "轮播图删除失败");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "数据库错误: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.print(jsonResponse.toString());
    }

    private String getFileExtension(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");

        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                int lastDot = fileName.lastIndexOf(".");
                if (lastDot > 0) {
                    return fileName.substring(lastDot);
                }
            }
        }

        return "";
    }

}
