package edu.csust.service;

import edu.csust.dao.ChatroomDao;
import edu.csust.dao.ChatroomUserDao;
import edu.csust.dao.MessageDao;
import edu.csust.dao.UserDao;
import edu.csust.entity.Chatroom;
import edu.csust.entity.ChatroomUser;
import edu.csust.entity.Message;
import edu.csust.entity.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChatService {
    private ChatroomDao chatroomDao = new ChatroomDao();
    private ChatroomUserDao chatroomUserDao = new ChatroomUserDao();
    private MessageDao messageDao = new MessageDao();
    private UserDao userDao = new UserDao();

    // 获取用户可访问的聊天室列表
    public List<ChatroomUser> getUserChatrooms(int userId) throws Exception {
        List<ChatroomUser> chatroomUsers = chatroomUserDao.findByUserId(userId);

        // 确保关联对象已加载
        for(ChatroomUser cu : chatroomUsers) {
            if(cu.getChatroom() == null) {
                Chatroom chatroom = chatroomDao.findById(cu.getRoomId());
                cu.setChatroom(chatroom);
            }
            if(cu.getUser() == null) {
                User user = userDao.findById(cu.getUserId());
                cu.setUser(user);
            }
        }
        return chatroomUsers;
    }

    // 获取聊天室成员（新增方法）
    public List<ChatroomUser> getChatroomMembers(int roomId) throws Exception {
        return chatroomUserDao.findByRoomId(roomId);
    }

    // 获取指定聊天室的消息
    // 修改获取消息方法，添加异常处理
    public List<Message> getChatroomMessages(int chatroomId) {
        try {
            return messageDao.findByChatroomId(chatroomId).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt)) // 确保时间排序
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("获取消息失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // 创建新聊天室
    public int createChatroom(Chatroom chatroom) throws Exception {
        return chatroomDao.create(chatroom);
    }

    // 搜索聊天室
    public List<Chatroom> searchChatrooms(String keyword) throws Exception {
        return chatroomDao.searchByName(keyword);
    }

    public Chatroom getChatroomById(int chatroomId) throws Exception {
        return chatroomDao.findById(chatroomId);
    }

    // 加入聊天室
    public int joinChatroom(int userId, int chatroomId) throws Exception {
        return chatroomUserDao.addUserToRoom(userId, chatroomId);
    }

    // 发送消息
    public void sendMessage(Message message) {
        try {
            // 添加字段验证
            if (message.getChatroomId() == null || message.getUserId() == null) {
                throw new IllegalArgumentException("消息缺少必要字段");
            }
            messageDao.insert(message);
        } catch (Exception e) {
            System.err.println("存储消息失败: " + e.getMessage());
            throw new RuntimeException("消息发送失败", e);
        }
    }
}
