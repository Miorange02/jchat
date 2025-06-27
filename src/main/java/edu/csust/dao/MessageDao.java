package edu.csust.dao;

import edu.csust.entity.Message;
import edu.csust.util.DBHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class MessageDao {
    private static final String TABLE_NAME = "message";

    // 添加消息
    public int insert(Message message) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (chatroom_id, user_id, content, type) VALUES (?, ?, ?, ?)";
        return DBHelper.executeUpdate(sql,
            message.getChatroomId(),
            message.getUserId(),
            message.getContent(),
            message.getType());
    }

    // 根据ID删除消息
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id=?";
        return DBHelper.executeUpdate(sql, id);
    }

    // 根据聊天室ID获取消息列表
    public List<Message> findByChatroomId(int chatroomId) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE chatroom_id=? ORDER BY created_at ASC";
        return DBHelper.query(sql, this::mapRow, chatroomId);
    }

    // 批量删除消息
    public int batchDelete(List<Integer> messageIds) throws SQLException {
        if (messageIds == null || messageIds.isEmpty()) {
            return 0;
        }
        String placeholders = String.join(",", Collections.nCopies(messageIds.size(), "?"));
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id IN (" + placeholders + ")";
        return DBHelper.executeUpdate(sql, messageIds.toArray());
    }

    // 按聊天室删除所有消息
    public int deleteByChatroom(int chatroomId) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE chatroom_id=?";
        return DBHelper.executeUpdate(sql, chatroomId);
    }

    // 获取用户在某聊天室的消息
    public List<Message> findByUserAndChatroom(int userId, int chatroomId) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id=? AND chatroom_id=? ORDER BY created_at ASC";
        return DBHelper.query(sql, this::mapRow, userId, chatroomId);
    }

    // 获取最新N条消息
    public List<Message> findLatestMessages(int chatroomId, int limit) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE chatroom_id=? ORDER BY created_at DESC LIMIT ?";
        return DBHelper.query(sql, this::mapRow, chatroomId, limit);
    }

    // 模糊查询搜索消息
    public List<Message> searchMessages(int chatroomId, String keyword) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE chatroom_id=? AND content LIKE ? ORDER BY created_at ASC";
        return DBHelper.query(sql, this::mapRow, chatroomId, "%" + keyword + "%");
    }

    // 行映射方法
    private Message mapRow(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt("id"));
        message.setChatroomId(rs.getInt("chatroom_id"));
        message.setUserId(rs.getInt("user_id"));
        message.setContent(rs.getString("content"));
        message.setType(rs.getString("type"));
        message.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        return message;
    }
}
