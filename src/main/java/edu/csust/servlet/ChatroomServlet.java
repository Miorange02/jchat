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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");  // 返回 JSON

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.getWriter().write("{\"success\":false, \"message\":\"未登录\"}");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                handleCreate(request, response, user.getId());
            } else if ("search".equals(action)) {
                handleSearch(request, response);
            } else if ("join".equals(action)) {
                handleJoin(request, response, user.getId());
            } else if ("exit".equals(action)) {  // 新增退出群聊处理
                handleExit(request, response, user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response, int creatorId) throws Exception {
        Chatroom chatroom = new Chatroom();
        chatroom.setRname(request.getParameter("rname"));
        chatroom.setDescription(request.getParameter("description"));
        chatroom.setCreator(creatorId);
        int roomId = chatService.createChatroom(chatroom, creatorId);
        response.sendRedirect("/chat?chatroomId=" + roomId);
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String keyword = request.getParameter("keyword");
        List<Chatroom> results = chatService.searchChatrooms(keyword);
        request.setAttribute("searchResults", results);
        request.getRequestDispatcher("/chat.jsp").forward(request, response);
    }

    private void handleJoin(HttpServletRequest request, HttpServletResponse response, int userId) throws Exception {
        int chatroomId = Integer.parseInt(request.getParameter("chatroomId"));
        chatService.joinChatroom(userId, chatroomId);
        Chatroom chatroom = chatService.getChatroomById(chatroomId);
        String json = String.format(
                "{\"success\":true, \"message\":\"加入成功\", \"chatroom\":{\"id\":%d, \"rname\":\"%s\", \"memberCount\":%d}}",
                chatroom.getId(),
                chatroom.getRname(),
                chatroom.getMemberCount()
        );
        response.getWriter().write(json);
    }

    // 新增：处理退出群聊逻辑
    private void handleExit(HttpServletRequest request, HttpServletResponse response, int userId) throws Exception {
        int chatroomId = Integer.parseInt(request.getParameter("chatroomId"));

        // 检查用户是否在聊天室中
        if (!chatService.isUserInRoom(userId, chatroomId)) {
            response.getWriter().write("{\"success\":false, \"message\":\"您不在该聊天室中\"}");
            return;
        }

        // 检查是否是创建者（不允许退出）
        Chatroom chatroom = chatService.getChatroomById(chatroomId);
        if (chatroom.getCreator() == userId) {
            response.getWriter().write("{\"success\":false, \"message\":\"创建者不能退出聊天室\"}");
            return;
        }

        // 执行退出操作（删除关联记录）
        int affectedRows = chatService.removeUserFromRoom(userId, chatroomId);
        if (affectedRows > 0) {
            response.getWriter().write("{\"success\":true, \"message\":\"退出成功\"}");
        } else {
            response.getWriter().write("{\"success\":false, \"message\":\"退出失败\"}");
        }
    }
}
