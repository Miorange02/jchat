package edu.csust.servlet;

import edu.csust.service.ChatService;
import edu.csust.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = req.getParameter("action");

        String pathInfo = req.getPathInfo();
        try {
            UserService userService = new UserService();
            ChatService chatService = new ChatService();

            // 统一获取管理数据
                req.setAttribute("users", userService.getAllUsers());
                req.setAttribute("chatrooms", chatService.getAllChatrooms());
                req.setAttribute("messages", chatService.getAllMessages());
                req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
