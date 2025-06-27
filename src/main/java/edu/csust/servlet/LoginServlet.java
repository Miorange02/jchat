package edu.csust.servlet;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String uname = req.getParameter("uname");
        String password = req.getParameter("password");

        // 查询数据库验证用户
        String sql = "SELECT * FROM user WHERE uname = ? AND password = ?";
        User user = DBHelper.query(sql, new DBHelper.RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUname(rs.getString("uname"));
                u.setEmail(rs.getString("email"));
                u.setAvatarUrl(rs.getString("avatar_url"));
                u.setStatus("online"); // 获取更新后的状态
                u.setLastActive(rs.getObject("last_active", LocalDateTime.class));
                return u;
            }
        }, uname, password).stream().findFirst().orElse(null);

        System.out.println(user);

        if (user != null) {
            // 登录成功，设置session并跳转到聊天室
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/chat");
        } else {
            // 登录失败，返回错误信息
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
