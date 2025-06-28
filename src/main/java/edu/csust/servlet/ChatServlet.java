package edu.csust.servlet;

import edu.csust.entity.Chatroom;
import edu.csust.entity.ChatroomUser;
import edu.csust.entity.Message;
import edu.csust.entity.User;
import edu.csust.service.ChatService;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private ChatService chatService = new ChatService();

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("/login");
            return;
        }

        try {
            // 获取用户聊天室和消息数据
            List<ChatroomUser> chatrooms = chatService.getUserChatrooms(currentUser.getId());
            int chatroomId = getChatroomIdFromRequest(request, chatrooms);

            // 获取当前聊天室对象
            Chatroom currentChatroom = chatService.getChatroomById(chatroomId);
            List<ChatroomUser> members = chatService.getChatroomMembers(chatroomId);
            List<Message> messages = chatService.getChatroomMessages(chatroomId);

            // 设置请求属性
            request.setAttribute("chatrooms", chatrooms);
            request.setAttribute("messages", messages);
            request.setAttribute("members", members);
            request.setAttribute("currentChatroomId", chatroomId);
            request.setAttribute("currentChatroom", currentChatroom); // 添加当前聊天室对象

            // 转发到聊天页面
            request.getRequestDispatcher("/chat.jsp").forward(request, response);

        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    private int getChatroomIdFromRequest(HttpServletRequest request, List<ChatroomUser> chatrooms) {
        String idParam = request.getParameter("chatroomId");
        if (idParam != null) {
            return Integer.parseInt(idParam);
        }
        return chatrooms.isEmpty() ? 0 : chatrooms.get(0).getRoomId();
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("error", "操作失败: " + e.getMessage());
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
