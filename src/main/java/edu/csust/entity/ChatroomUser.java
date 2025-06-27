package edu.csust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatroomUser {
    private Integer userId;
    private Integer roomId;
    private LocalDateTime joinTime;

    private Chatroom chatroom;
    private User user;
}
