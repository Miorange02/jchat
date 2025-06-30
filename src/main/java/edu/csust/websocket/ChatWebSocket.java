package edu.csust.websocket;

import edu.csust.entity.Message;
import edu.csust.service.ChatService;
import edu.csust.util.JsonUtil;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{chatroomId}") // WebSocket端点，路径包含聊天室ID
public class ChatWebSocket {
    // 存储聊天室ID到会话的映射（key: 聊天室ID，value: 会话集合）
    private static final Map<Integer, Map<String, Session>> CHATROOM_SESSIONS = new ConcurrentHashMap<>();

    @OnOpen // 连接建立时触发
    public void onOpen(Session session, @PathParam("chatroomId") int chatroomId) {
        // 将当前会话加入对应聊天室的会话集合
        CHATROOM_SESSIONS.computeIfAbsent(chatroomId, k -> new ConcurrentHashMap<>())
                        .put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(Session session, String messageJson, @PathParam("chatroomId") int chatroomId) {
        Message message = JsonUtil.fromJson(messageJson, Message.class);
        // 保存消息到数据库（调用 ChatService）
        try {
            ChatService chatService = new ChatService();
            chatService.sendMessage(message); // 调用 sendMessage 方法保存
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 广播消息
        broadcastToChatroom(chatroomId, message);
    }

    @OnClose // 连接关闭时触发
    public void onClose(Session session, @PathParam("chatroomId") int chatroomId) {
        // 从聊天室会话集合中移除当前会话
        CHATROOM_SESSIONS.getOrDefault(chatroomId, new ConcurrentHashMap<>()).remove(session.getId());
    }

    // 广播消息到指定聊天室的所有会话
    private static void broadcastToChatroom(int chatroomId, Message message) {
        Map<String, Session> sessions = CHATROOM_SESSIONS.get(chatroomId);
        if (sessions != null) {
            String messageJson = JsonUtil.toJson(message);
            sessions.values().forEach(session -> {
                try {
                    session.getBasicRemote().sendText(messageJson); // 发送消息
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
