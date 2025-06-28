package edu.csust.websocket;

import edu.csust.entity.Message;
import edu.csust.service.ChatService;
import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
// 添加缺失的导入
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@ServerEndpoint(value = "/ws/chat", configurator = ChatEndpoint.Configurator.class)
public class ChatEndpoint {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    public static class Configurator extends ServerEndpointConfig.Configurator {
        @Override
        public void modifyHandshake(ServerEndpointConfig config,
                                   HandshakeRequest request,
                                   HandshakeResponse response) {
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            if (httpSession != null) {
                String username = (String) httpSession.getAttribute("username");
                config.getUserProperties().put("username", username);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        String username = (String) session.getUserProperties().get("username");
        if (username != null) {
            broadcastSystemMessage(username + "进入了聊天室");
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        String username = (String) session.getUserProperties().get("username");
        if (username != null) {
            broadcastSystemMessage(username + "离开了聊天室");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JSONObject msg = new JSONObject(message);
            if ("message".equals(msg.optString("type"))) {
                // 添加字段校验和类型转换
                int chatroomId = msg.optInt("chatroomId", 0);
                int userId = msg.optInt("userId", 0);
                String content = msg.optString("content", "");

                if (chatroomId == 0 || userId == 0 || content.isEmpty()) {
                    System.err.println("无效的消息格式: " + message);
                    return;
                }

                Message dbMsg = new Message();
                dbMsg.setChatroomId(chatroomId);
                dbMsg.setUserId(userId);
                dbMsg.setContent(content);
                dbMsg.setCreatedAt(LocalDateTime.now());

                new ChatService().sendMessage(dbMsg);
                msg.put("timestamp", System.currentTimeMillis());
                broadcastMessage(msg.toString());
            }
        } catch (Exception e) {
            System.err.println("消息处理错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getUsernameFromSession(Session session) {
        return (String) session.getUserProperties().get("username");
    }

    private static void broadcastMessage(String message) {
        sessions.forEach(session -> {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        });
    }

    private static void broadcastSystemMessage(String content) {
        JSONObject msg = new JSONObject();
        msg.put("type", "system");
        msg.put("content", content);
        broadcastMessage(msg.toString());
    }
}
