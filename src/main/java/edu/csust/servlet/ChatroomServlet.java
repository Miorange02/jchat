package edu.csust.servlet;

import edu.csust.entity.Chatroom;
import edu.csust.entity.User;
import edu.csust.service.ChatService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/chatroom")
public class ChatroomServlet extends HttpServlet {
    private ChatService chatService = new ChatService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                handleCreate(request, response, user.getId());
            } else if ("search".equals(action)) {
                handleSearch(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "操作失败: " + e.getMessage());
            request.getRequestDispatcher("/chat.jsp").forward(request, response);
        }
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response, int creatorId)
        throws Exception {
        Chatroom chatroom = new Chatroom();
        chatroom.setRname(request.getParameter("rname"));
        chatroom.setDescription(request.getParameter("description"));

        int roomId = chatService.createChatroom(chatroom);
        response.sendRedirect("/chat?chatroomId=" + roomId);
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        String keyword = request.getParameter("keyword");
        List<Chatroom> results = chatService.searchChatrooms(keyword);
        request.setAttribute("searchResults", results);
        request.getRequestDispatcher("/chat.jsp").forward(request, response);
    }
}
