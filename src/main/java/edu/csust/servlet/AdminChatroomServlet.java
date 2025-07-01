package edu.csust.servlet;

import edu.csust.service.ChatroomService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/admin/updateChatroom")
public class AdminChatroomServlet extends HttpServlet {
    private final ChatroomService chatroomService = new ChatroomService(); // 假设已有ChatroomService

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            // 从表单获取参数
            int id = Integer.parseInt(req.getParameter("id"));
            String rname = req.getParameter("rname");
            String description = req.getParameter("description");

            // 更新聊天室（假设ChatroomService有update方法）
            int success = chatroomService.updateChatroom(id, rname, description);

            if (success!=0) {
                out.write("{\"success\":true, \"message\":\"更新成功\"}");
            } else {
                out.write("{\"success\":false, \"message\":\"更新失败\"}");
            }
        } catch (NumberFormatException | SQLException e) {
            out.write("{\"success\":false, \"message\":\"无效的聊天室ID\"}");
        }
    }
}
