package edu.csust.service;

import edu.csust.entity.Chatroom;
import edu.csust.dao.ChatroomDao;

import java.sql.SQLException;

public class ChatroomService {
    public int updateChatroom(int id, String rname, String description) throws SQLException {
        ChatroomDao chatroomDao = new ChatroomDao();
        Chatroom c = new Chatroom();
        c.setId(id);
        c.setRname(rname);
        c.setDescription(description);
        return chatroomDao.update(c);
    }
}
