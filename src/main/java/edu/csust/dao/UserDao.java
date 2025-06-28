package edu.csust.dao;

import edu.csust.entity.User;
import edu.csust.util.DBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UserDao implements BaseDao<User> {
    private static final String TABLE_NAME = "user";

    public int updateStatus(User user) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET status=?, last_active=? WHERE id=?";
        return DBHelper.executeUpdate(sql,
                user.getStatus(),
                user.getLastActive(),
                user.getId());
    }

    @Override
    public int insert(User user) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (uname, password, email, avatar_url) VALUES (?, ?, ?, ?)";
        return DBHelper.executeUpdate(sql,
            user.getUname(),
            user.getPassword(),
            user.getEmail(),
            user.getAvatarUrl());
    }

    @Override
    public int update(User user) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET uname=?, password=?, email=?, avatar_url=?, status=?, last_active=? WHERE id=?";
        return DBHelper.executeUpdate(sql,
            user.getUname(),
            user.getPassword(),
            user.getEmail(),
            user.getAvatarUrl(),
            user.getStatus(),
            user.getLastActive(),
            user.getId());
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id=?";
        return DBHelper.executeUpdate(sql, id);
    }

    @Override
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id=?";
        return DBHelper.query(sql, this::mapRow, id).stream().findFirst().orElse(null);
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return DBHelper.query(sql, this::mapRow);
    }

    public User findByUsername(String uname) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE uname=?";
        return DBHelper.query(sql, this::mapRow, uname).stream().findFirst().orElse(null);
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUname(rs.getString("uname"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        user.setLastActive(rs.getObject("last_active", LocalDateTime.class));
        return user;
    }
}
