package edu.csust.service;

import edu.csust.dao.ChatroomDao;
import edu.csust.dao.ChatroomUserDao;
import edu.csust.dao.MessageDao;
import edu.csust.dao.UserDao;
import edu.csust.entity.Chatroom;
import edu.csust.entity.ChatroomUser;
import edu.csust.entity.Message;
import edu.csust.entity.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ChatService {
    private ChatroomDao chatroomDao = new ChatroomDao();
    private ChatroomUserDao chatroomUserDao = new ChatroomUserDao();
    private MessageDao messageDao = new MessageDao();
    private UserDao userDao = new UserDao();

    // 获取用户可访问的聊天室列表
    public List<ChatroomUser> getUserChatrooms(int userId) throws Exception {
        List<ChatroomUser> chatroomUsers = chatroomUserDao.findByUserId(userId);

        // 确保关联对象已加载
        for (ChatroomUser cu : chatroomUsers) {
            if (cu.getChatroom() == null) {
                Chatroom chatroom = chatroomDao.findById(cu.getRoomId());
                cu.setChatroom(chatroom);
            }
            if (cu.getUser() == null) {
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
    public List<Message> getChatroomMessages(int chatroomId) throws Exception {
        return messageDao.findByChatroomId(chatroomId);
    }

    // 创建新聊天室
    public int createChatroom(Chatroom chatroom, int creatorId) throws Exception {
        int roomId = chatroomDao.create(chatroom);
        chatroomUserDao.addUserToRoom(creatorId, roomId);
        return roomId;
    }

    // 搜索聊天室
    public List<Chatroom> searchChatrooms(String keyword) throws Exception {
        return chatroomDao.searchByName(keyword);
    }

    public Chatroom getChatroomById(int chatroomId) throws Exception {
        return chatroomDao.findById(chatroomId);
    }

    // 加入聊天室（新增系统消息逻辑）
    public int joinChatroom(int userId, int chatroomId) throws Exception {
        // 1. 检查聊天室是否存在（原有逻辑）
        Chatroom chatroom = chatroomDao.findById(chatroomId);
        if (chatroom == null) {
            throw new Exception("目标聊天室不存在");
        }

        // 2. 检查用户是否已加入（原有逻辑）
        boolean isAlreadyJoined = isUserInRoom(userId, chatroomId);
        if (isAlreadyJoined) {
            throw new Exception("您已加入该聊天室");
        }

        // 3. 执行加入操作（原有逻辑）
        int affectedRows = chatroomUserDao.addUserToRoom(userId, chatroomId);

        // 新增：生成系统消息（用户加入）
        if (affectedRows > 0) {
            User user = userDao.findById(userId); // 获取用户信息
            Message systemMsg = new Message();
            systemMsg.setChatroomId(chatroomId);
            systemMsg.setType("system"); // 标记为系统消息
            systemMsg.setContent(user.getUname() + " 加入了聊天室");
            systemMsg.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            messageDao.insert(systemMsg); // 保存到消息表
        }
        return affectedRows;
    }

    // 发送消息
    public int sendMessage(Message message) throws Exception {
        return messageDao.insert(message);
    }

    //检查用户是否在聊天室中
    public boolean isUserInRoom(int userId, int chatroomId) throws Exception {
        return chatroomUserDao.isUserInRoom(userId, chatroomId);
    }

    // 退出群聊
    public int removeUserFromRoom(int userId, int chatroomId) {
        try {
            int affectedRows = chatroomUserDao.removeUserFromRoom(userId, chatroomId);
            // 新增：生成系统消息（用户退出）
            if (affectedRows > 0) {
                User user = userDao.findById(userId);
                Message systemMsg = new Message();
                systemMsg.setChatroomId(chatroomId);
                systemMsg.setType("system");
                systemMsg.setContent(user.getUname() + " 退出了聊天室");
                systemMsg.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                messageDao.insert(systemMsg);
            }
            return affectedRows;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}

