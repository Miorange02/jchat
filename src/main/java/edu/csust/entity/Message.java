package edu.csust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    private Integer id;
    private Integer chatroomId;
    private Integer userId;
    private String content;
    private String type = "text"; // "text" 或 "system"
    private Timestamp createdAt;

    private User user;
}
