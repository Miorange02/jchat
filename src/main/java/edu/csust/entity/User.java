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
public class User {
    private Integer id;
    private String uname;
    private String password;
    private String email;
    private String avatarUrl = "default_1.jpg";
    private String status = "offline"; // "online" 或 "offline"
    private LocalDateTime createdAt;
    private LocalDateTime lastActive;
}
