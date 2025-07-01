-- 1. 创建测试用户
INSERT INTO user (uname, password, email, avatar_url)
VALUES ('admin', '123', '92616044@qq.com', 'default_0.jpg');
INSERT INTO user (uname, password, email)
VALUES ('123', '123', '123@123.com');
INSERT INTO user (uname, password, email)
VALUES ('慧慧', '123', '');
INSERT INTO user (uname, password, email)
VALUES ('David', '123', '711@gmail.com');
INSERT INTO user (uname, password, email)
VALUES ('牢大', '123', '');

-- 2. 创建测试聊天室（初始 member_count = 0）
INSERT INTO chatroom (rname, description, creator_id)
VALUES ('世界频道', '默认创建用户第一个加入的群聊', 1);
INSERT INTO chatroom (rname, description, creator_id)
VALUES ('web开发小组', '呜呜呜这个项目怎么这么难', 1);

-- 3. 加入聊天室
insert into chatroom_user
VALUES (1, 1, DEFAULT);
insert into chatroom_user
VALUES (1, 2, DEFAULT);
insert into chatroom_user
VALUES (1, 3, DEFAULT);
insert into chatroom_user
VALUES (1, 4, DEFAULT);
insert into chatroom_user
VALUES (1, 5, DEFAULT);
insert into chatroom_user (room_id, user_id)
VALUES (2, 1);
insert into chatroom_user (room_id, user_id)
VALUES (2, 2);
insert into chatroom_user (room_id, user_id)
VALUES (2, 3);
insert into chatroom_user (room_id, user_id)
VALUES (2, 4),
       (2, 5);

-- 4. 添加消息
TRUNCATE TABLE message;
INSERT INTO message (chatroom_id, user_id, content, type, created_at)
VALUES (1, 1, '大家好，欢迎加入技术交流群！', 'text', NOW() - INTERVAL 3 MINUTE),
       (1, 2, '请问有人熟悉Spring Boot吗？', 'text', NOW() - INTERVAL 2 MINUTE),
       (1, 1, '我也在学习，一起讨论吧！', 'text', NOW()),
       (1, NULL, 'David已加入群聊', 'system', NOW()),
       (2, 2, '今天的会议几点开始？', 'text', NOW() - INTERVAL 10 MINUTE),
       (2, 5, '我昨天刚升级了我的项目，目前运行得很好', 'text', NOW() - INTERVAL 8 MINUTE),
       (2, 4, '谁问你了?', 'text', NOW() - INTERVAL 6 MINUTE),
       (2, NULL, '黑曼巴已退出群聊', 'system', NOW() - INTERVAL 1 MINUTE);
