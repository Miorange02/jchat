package edu.csust.servlet;

import edu.csust.service.UserService;
import edu.csust.entity.User;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 获取当前用户
            User user = (User) request.getSession().getAttribute("user");
            
            if (user != null) {
                // 更新用户状态为离线
                user.setStatus("offline");
                user.setLastActive(LocalDateTime.now());
                userService.updateUserStatus(user, "offline");
                request.getSession().removeAttribute("username"); // 新增清理
            }
            
            // 使session失效
            request.getSession().invalidate();

            // 使用URL参数传递登出标识
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "登出失败");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
