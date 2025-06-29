package edu.csust.dao;

import edu.csust.entity.Chatroom;
import edu.csust.entity.ChatroomUser;
import edu.csust.entity.User;
import edu.csust.util.DBHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ChatroomUserDao {
    private static final String TABLE_NAME = "chatroom_user";

    // 添加用户到聊天室
    public int addUserToRoom(int userId, int roomId) throws SQLException {
        String sql = "INSERT INTO chatroom_user (user_id, room_id) VALUES (?, ?)";
        return DBHelper.executeUpdate(sql, userId, roomId);
    }

    // 从聊天室移除用户
    public int removeUserFromRoom(int userId, int roomId) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE user_id=? AND room_id=?";
        return DBHelper.executeUpdate(sql, userId, roomId);
    }

    // 检查用户是否在聊天室中
    public boolean isUserInRoom(int userId, int roomId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE user_id=? AND room_id=?";
        List<Integer> result = DBHelper.query(sql, rs -> rs.getInt(1), userId, roomId);
        return !result.isEmpty() && result.get(0) > 0;
    }

    // 获取用户加入的所有聊天室
    public List<ChatroomUser> findByUserId(int userId) throws SQLException {
        String sql = "SELECT cu.*, c.*, u.* " +
                "FROM " + TABLE_NAME + " cu " +
                "JOIN chatroom c ON cu.room_id = c.id " +
                "JOIN user u ON c.creator_id = u.id " +
                "WHERE cu.user_id=?";
        return DBHelper.query(sql, this::mapRowWithAssociations, userId);
    }

    // 获取聊天室的所有成员
    public List<ChatroomUser> findByRoomId(int roomId) throws SQLException {
        String sql = "SELECT cu.*, u.* " +
                "FROM " + TABLE_NAME + " cu " +
                "JOIN user u ON cu.user_id = u.id " +
                "WHERE cu.room_id=?";
        return DBHelper.query(sql, this::mapRowWithAssociations, roomId);
    }

    // 行映射方法
    private ChatroomUser mapRow(ResultSet rs) throws SQLException {
        ChatroomUser chatroomUser = new ChatroomUser();
        chatroomUser.setUserId(rs.getInt("user_id"));
        chatroomUser.setRoomId(rs.getInt("room_id"));
        chatroomUser.setJoinTime(rs.getObject("join_time", LocalDateTime.class));
        return chatroomUser;
    }

    // 完整的行映射方法（处理关联对象）
    private ChatroomUser mapRowWithAssociations(ResultSet rs) throws SQLException {
        ChatroomUser chatroomUser = new ChatroomUser();
        chatroomUser.setUserId(rs.getInt("user_id"));
        chatroomUser.setRoomId(rs.getInt("room_id"));
        chatroomUser.setJoinTime(rs.getObject("join_time", LocalDateTime.class));

        try {
            // 设置关联的User对象
            User user = new User();
            user.setId(rs.getInt("u.id"));
            user.setUname(rs.getString("u.uname"));
            user.setEmail(rs.getString("u.email"));
            user.setAvatarUrl(rs.getString("u.avatar_url"));
            user.setStatus(rs.getString("u.status"));
            user.setLastActive(rs.getObject("u.last_active", LocalDateTime.class));
            chatroomUser.setUser(user);

            // 尝试设置关联的Chatroom对象（仅findByUserId需要）
            try {
                Chatroom chatroom = new Chatroom();
                chatroom.setId(rs.getInt("c.id"));
                chatroom.setRname(rs.getString("c.rname"));
                chatroom.setDescription(rs.getString("c.description"));
                chatroom.setCreator(rs.getInt("c.creator_id"));
                chatroom.setMemberCount(rs.getInt("c.member_count"));
                chatroom.setCreatedAt(rs.getObject("c.created_at", LocalDateTime.class));
                chatroomUser.setChatroom(chatroom);
            } catch (SQLException e) {
                // 如果c.*列不存在（如findByRoomId查询），则忽略
            }
        } catch (SQLException e) {
            throw new SQLException("映射关联对象时出错", e);
        }

        return chatroomUser;
    }
}
