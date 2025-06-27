package edu.csust.dao;

import edu.csust.entity.Chatroom;
import edu.csust.entity.ChatroomUser;
import edu.csust.entity.User;
import edu.csust.util.DBHelper;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ChatroomUserDao {
    private static final String TABLE_NAME = "chatroom_user";

    // 添加用户到聊天室
    public int addUserToRoom(int userId, int roomId) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, room_id) VALUES (?, ?)";
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
        String sql = "SELECT cu.*, c.*, u.uname " +  // 移除了as creator_name
                "FROM " + TABLE_NAME + " cu " +
                "JOIN chatroom c ON cu.room_id = c.id " +
                "JOIN user u ON c.creator_id = u.id " +
                "WHERE cu.user_id=?";
        return DBHelper.query(sql, this::mapRowWithAssociations, userId);
    }

    // 获取聊天室的所有成员
    public List<ChatroomUser> findByRoomId(int roomId) throws SQLException {
        String sql = "SELECT cu.*, u.* FROM chatroom_user cu \n" +
                "JOIN user u ON cu.user_id = u.id \n" +
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

    private ChatroomUser mapRowWithAssociations(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        for(int i=1; i<=metaData.getColumnCount(); i++) {
            System.out.println("Column " + i + ": " + metaData.getColumnName(i));
        }
        ChatroomUser chatroomUser = new ChatroomUser();
        chatroomUser.setUserId(rs.getInt("user_id"));
        chatroomUser.setRoomId(rs.getInt("room_id"));
        chatroomUser.setJoinTime(rs.getObject("join_time", LocalDateTime.class));

        // 设置关联的Chatroom
        Chatroom chatroom = new Chatroom();
        chatroom.setId(rs.getInt("id"));
        chatroom.setRname(rs.getString("rname"));
        chatroom.setDescription(rs.getString("description"));
        chatroom.setCreator(rs.getInt("creator_id"));
        chatroom.setMemberCount(rs.getInt("member_count"));
        chatroom.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        chatroomUser.setChatroom(chatroom);

        // 设置关联的User
        User user = new User();
        user.setId(rs.getInt("creator_id"));
        user.setUname(rs.getString("uname"));
        user.setEmail(rs.getString("email"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setStatus(rs.getString("status"));
        user.setLastActive(rs.getObject("last_active", LocalDateTime.class));
        chatroomUser.setUser(user);


        return chatroomUser;
    }
}
