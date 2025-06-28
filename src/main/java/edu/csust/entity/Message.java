package edu.csust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private LocalDateTime createdAt;

    private User user;

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        return createdAt != null ? createdAt.format(formatter) : "未知时间";
    }
}
