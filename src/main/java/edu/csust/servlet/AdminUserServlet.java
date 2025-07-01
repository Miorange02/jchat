package edu.csust.servlet;

import com.alibaba.fastjson.JSON;
import edu.csust.entity.User;
import edu.csust.service.UserService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet({"/admin/getUser", "/admin/updateUser"})
public class AdminUserServlet extends HttpServlet {
    private final UserService userService = new UserService();

    static class Result {
        private boolean success;
        private String message;
        private Object data;

        public Result(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getter 方法，FastJSON 需要这些方法来序列化对象
        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                out.write(JSON.toJSONString(new Result(false, "用户 ID 参数缺失", null)));
                return;
            }
            int userId = Integer.parseInt(idParam.trim());
            User user = userService.getUserById(userId);

            if (user != null) {
                out.write(JSON.toJSONString(new Result(true, "用户存在", user)));
            } else {
                out.write(JSON.toJSONString(new Result(false, "用户不存在", null)));
            }
        } catch (NumberFormatException e) {
            out.write(JSON.toJSONString(new Result(false, "无效的用户 ID，ID 必须为数字", null)));
        } catch (SQLException e) {
            out.write(JSON.toJSONString(new Result(false, "数据库查询出错: " + e.getMessage(), null)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                out.write(JSON.toJSONString(new Result(false, "用户 ID 参数缺失", null)));
                return;
            }
            int id = Integer.parseInt(idParam.trim());
            String uname = req.getParameter("uname");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            boolean success = userService.updateUser(id, uname, email, password);
            out.write(JSON.toJSONString(new Result(success, success ? "更新成功" : "更新失败", null)));
        } catch (NumberFormatException e) {
            out.write(JSON.toJSONString(new Result(false, "无效的用户 ID，ID 必须为数字", null)));
        } catch (SQLException e) {
            out.write(JSON.toJSONString(new Result(false, "数据库更新出错: " + e.getMessage(), null)));
        }
    }
}
