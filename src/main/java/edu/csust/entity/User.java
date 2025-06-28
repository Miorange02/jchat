package edu.csust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public String getLastActiveDisplay() {
        if (lastActive == null) {
            return "从未活跃";
        }

        // 更严谨的状态判断
        if ("online".equalsIgnoreCase(status)) {
            return "在线";
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastActive, now);
        long minutes = duration.toMinutes();

        // 更精确的时间判断
        if (minutes < 1) {
            return "刚刚";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (duration.toHours() <= 24) {
            return lastActive.format(DateTimeFormatter.ofPattern("今天 HH:mm"));
        } else if (duration.toHours() <= 48) {
            return lastActive.format(DateTimeFormatter.ofPattern("昨天 HH:mm"));
        } else {
            return lastActive.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }
}
