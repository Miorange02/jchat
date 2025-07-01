package edu.csust.service;

import edu.csust.dao.UserDao;
import edu.csust.entity.User;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDao userDao = new UserDao();

    /**
     * 更新用户状态和最后活跃时间
     */
    public void updateUserStatus(User user, String status) throws SQLException {
        user.setStatus(status);
        user.setLastActive(java.time.LocalDateTime.now());
        userDao.updateStatus(user);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDao.findAll();
    }

    public User getUserById(int id) throws SQLException {
        return userDao.findById(id);
    }

    public boolean updateUser(int id, String uname, String email, String password) throws SQLException {
        User user = new User();
        user.setId(id);
        user.setUname(uname);
        user.setEmail(email);
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        return userDao.updateUser(user);
    }

    public int deleteUser(int id) throws SQLException {
        return userDao.delete(id);
    }
    /**
     * 其他用户相关业务方法可以继续添加...
     */
}

