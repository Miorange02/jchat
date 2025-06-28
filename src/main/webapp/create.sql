DROP TABLE IF EXISTS message;      
DROP TABLE IF EXISTS chatroom_user;
DROP TABLE IF EXISTS chatroom;   

CREATE TABLE `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `uname` VARCHAR(50) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL, -- 加密存储
  `email` VARCHAR(100) UNIQUE NULL,
  `avatar_url` VARCHAR(255) DEFAULT 'default_1.jpg',
  `status` ENUM('online', 'offline') DEFAULT 'offline',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `last_active` DATETIME,
  INDEX idx_username (uname)
);

-- 聊天室表
CREATE TABLE `chatroom` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `rname` VARCHAR(100) NOT NULL,
  `description` VARCHAR(255),
  `creator_id` INT NOT NULL,
  `member_count` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (creator_id) REFERENCES user(id)
);
-- 聊天室-用户关联表
CREATE TABLE `chatroom_user` (
  `user_id` INT,
  `room_id` INT,
  `join_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, room_id),
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (room_id) REFERENCES chatroom(id)
);

-- 触发器更改聊天室人数
DELIMITER //
CREATE TRIGGER after_chatroom_user_insert
AFTER INSERT ON chatroom_user
FOR EACH ROW
BEGIN
    -- 更新 chatroom 表的 member_count +1
    UPDATE chatroom 
    SET member_count = member_count + 1 
    WHERE id = NEW.room_id; 
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_chatroom_user_delete
AFTER DELETE ON chatroom_user
FOR EACH ROW
BEGIN
    -- 更新 chatroom 表的 member_count -1
    UPDATE chatroom 
    SET member_count = member_count - 1 
    WHERE id = OLD.room_id;  
END //
DELIMITER ;

-- 消息表
CREATE TABLE `message` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    chatroom_id INT NOT NULL,
    user_id INT NULL,
    content TEXT NOT NULL,
    type ENUM('text', 'system') DEFAULT 'text',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chatroom_id) REFERENCES chatroom(id),
    FOREIGN KEY (user_id) REFERENCES user(id),
		INDEX idx_chatroom_id (chatroom_id),
    INDEX idx_user_id (user_id)
);