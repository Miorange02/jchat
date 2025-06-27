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
public class Chatroom {
    private Integer id;
    private String rname;
    private String description;
    private Integer creator;
    private Integer memberCount = 0;
    private LocalDateTime createdAt = LocalDateTime.now();
}
