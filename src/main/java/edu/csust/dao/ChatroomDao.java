package edu.csust.dao;

import edu.csust.entity.Chatroom;
import edu.csust.util.DBHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class ChatroomDao {
    private static final String TABLE_NAME = "chatroom";

    // 创建聊天室
    public int create(Chatroom chatroom) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (rname, description, creator_id) VALUES (?, ?, ?)";
        Connection connection = DBHelper.getConnection();
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            // 关键修改：设置返回自动生成的主键
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, chatroom.getRname());
            statement.setString(2, chatroom.getDescription());
            statement.setInt(3, chatroom.getCreator());
            statement.executeUpdate();

            // 获取生成的主键ID
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // 返回数据库生成的自增ID
            } else {
                throw new SQLException("创建聊天室失败，未获取到自增ID");
            }
        } finally {
            DBHelper.close(generatedKeys, statement, connection);
        }
    }

    // 根据ID查询聊天室
    public Chatroom findById(int id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id=?";
        return DBHelper.query(sql, this::mapRow, id).stream().findFirst().orElse(null);
    }

    // 根据名称模糊查询聊天室
    public List<Chatroom> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE rname LIKE ?";
        return DBHelper.query(sql, this::mapRow, "%" + name + "%");
    }

    // 获取用户创建的所有聊天室
    public List<Chatroom> findByCreator(int creatorId) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE creator_id=?";
        return DBHelper.query(sql, this::mapRow, creatorId);
    }

    // 获取所有聊天室
    public List<Chatroom> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return DBHelper.query(sql, this::mapRow);
    }

    // 更新聊天室信息
    public int update(Chatroom chatroom) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET rname=?, description=? WHERE id=?";
        return DBHelper.executeUpdate(sql,
            chatroom.getRname(),
            chatroom.getDescription(),
            chatroom.getId());
    }

    // 删除聊天室
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id=?";
        return DBHelper.executeUpdate(sql, id);
    }

    // 行映射方法
    private Chatroom mapRow(ResultSet rs) throws SQLException {
        Chatroom chatroom = new Chatroom();
        chatroom.setId(rs.getInt("id"));
        chatroom.setRname(rs.getString("rname"));
        chatroom.setDescription(rs.getString("description"));
        chatroom.setCreator(rs.getInt("creator_id"));
        chatroom.setMemberCount(rs.getInt("member_count"));
        chatroom.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        return chatroom;
    }
}
