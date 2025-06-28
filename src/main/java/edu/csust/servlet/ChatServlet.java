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
import java.time.LocalDateTime;
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

            // 新增时间处理逻辑
            messages.forEach(msg -> {
                if(msg.getCreatedAt() == null) {
                    msg.setCreatedAt(LocalDateTime.now());
                }
            });

            // 设置请求属性
            request.setAttribute("chatrooms", chatrooms);
            request.setAttribute("currentUserId", currentUser.getId());
            request.setAttribute("messages", messages);
            request.setAttribute("members", members);
            // 在service方法中
            request.setAttribute("currentChatroomId", chatroomId);
            // 添加日志输出
            System.out.println("[ChatServlet] 当前聊天室ID: " + chatroomId
                + ", 用户ID: " + currentUser.getId());
            request.setAttribute("currentChatroom", currentChatroom);

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
