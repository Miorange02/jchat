package edu.csust.servlet;

import edu.csust.dao.UserDao;
import edu.csust.dao.ChatroomUserDao;
import edu.csust.dao.ChatroomDao;
import edu.csust.entity.Chatroom;
import edu.csust.entity.User;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    private ChatroomUserDao chatroomUserDao = new ChatroomUserDao();
    private ChatroomDao chatroomDao = new ChatroomDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String uname = request.getParameter("uname");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String email = request.getParameter("email");

        // 验证密码一致性
        if (!password.equals(password2)) {
            forwardWithError(request, response, "两次密码输入不一致");
            return;
        }

        try {
            // 检查用户名是否已存在
            if (userDao.findByUsername(uname) != null) {
                forwardWithError(request, response, "用户名已存在");
                return;
            }

            // 创建新用户
            User user = new User();
            user.setUname(uname);
            user.setPassword(password); // 注意：实际应加密存储
            user.setEmail(email);
            user.setAvatarUrl("default_1.jpg"); // 使用默认头像

            int result = userDao.insert(user);

            if (result > 0) {
                // 获取新用户完整信息（包含生成的ID）
                User newUser = userDao.findByUsername(uname);

                // 获取第一个聊天室（假设ID为1）
                Chatroom firstChatroom = chatroomDao.findById(1);

                if (firstChatroom != null) {
                    // 加入聊天室
                    chatroomUserDao.addUserToRoom(newUser.getId(), firstChatroom.getId());
                }

                response.sendRedirect("login.jsp");
            } else {
                forwardWithError(request, response, "注册失败，请重试");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            forwardWithError(request, response, "数据库错误：" + e.getMessage());
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMsg)
            throws ServletException, IOException {
        request.setAttribute("error", errorMsg);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
