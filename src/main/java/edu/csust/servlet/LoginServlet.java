package edu.csust.servlet;

import edu.csust.service.UserService;
import edu.csust.entity.User;
import edu.csust.util.DBHelper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String uname = req.getParameter("uname");
        String password = req.getParameter("password");

        // 如果是登出后的重定向，且没有提交登录表单，则不显示错误
        if ((uname == null || password == null)) {
            req.removeAttribute("error");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = authenticateUser(uname, password);

            if (user != null) {
                // 在登录成功后设置用户属性
                userService.updateUserStatus(user, "online");
                req.getSession().setAttribute("user", user);
                // 在登录成功部分添加
                req.getSession().setAttribute("username", user.getUname());

                resp.sendRedirect(req.getContextPath() + "/chat");
            } else {
                handleLoginFailure(req, resp);
            }
        } catch (SQLException e) {
            handleDatabaseError(req, resp, e);
        }
    }

    private User authenticateUser(String uname, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE uname = ? AND password = ?";
        return DBHelper.query(sql, this::mapUser, uname, password)
                     .stream()
                     .findFirst()
                     .orElse(null);
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUname(rs.getString("uname"));
        user.setEmail(rs.getString("email"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setStatus(rs.getString("status"));
        user.setLastActive(rs.getObject("last_active", LocalDateTime.class));
        return user;
    }

    private void handleLoginFailure(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("error", "用户名或密码错误");
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    private void handleDatabaseError(HttpServletRequest req, HttpServletResponse resp, SQLException e)
            throws ServletException, IOException {
        e.printStackTrace();
        req.setAttribute("error", "系统错误，请稍后再试");
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
}
