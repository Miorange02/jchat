package edu.csust.service;

import edu.csust.dao.UserDao;
import edu.csust.entity.User;
import java.sql.SQLException;

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

    /**
     * 其他用户相关业务方法可以继续添加...
     */
}
