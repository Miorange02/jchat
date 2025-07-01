package edu.csust.servlet;

import edu.csust.dao.ChatroomDao;
import edu.csust.dao.MessageDao;
import edu.csust.dao.UserDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet({"/admin/deleteUser", "/admin/deleteChatroom", "/admin/deleteMessage", "/admin/batchDeleteMessages"})
public class AdminDeleteServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    private ChatroomDao chatroomDao = new ChatroomDao();
    private MessageDao messageDao = new MessageDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getServletPath();

        try {
            switch (pathInfo) {
                case "/admin/deleteUser":
                    handleDeleteUser(req, out);
                    break;
                case "/admin/deleteChatroom":
                    handleDeleteChatroom(req, out);
                    break;
                case "/admin/deleteMessage":
                    handleDeleteMessage(req, out);
                    break;
                case "/admin/batchDeleteMessages":
                    handleBatchDeleteMessages(req, out);
                    break;
                default:
                    out.write("{\"success\":false, \"message\":\"无效的请求路径\"}");
            }
        } catch (NumberFormatException e) {
            out.write("{\"success\":false, \"message\":\"无效的 ID，ID 必须为数字\"}");
        } catch (SQLException e) {
            out.write("{\"success\":false, \"message\":\"数据库操作出错: " + e.getMessage() + "\"}");
        }
    }

    private void handleDeleteUser(HttpServletRequest req, PrintWriter out) throws SQLException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            out.write("{\"success\":false, \"message\":\"用户 ID 参数缺失\"}");
            return;
        }
        int id = Integer.parseInt(idParam.trim());
        int result = userDao.delete(id);
        if (result > 0) {
            out.write("{\"success\":true, \"message\":\"用户删除成功\"}");
        } else {
            out.write("{\"success\":false, \"message\":\"用户删除失败\"}");
        }
    }

    private void handleDeleteChatroom(HttpServletRequest req, PrintWriter out) throws SQLException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            out.write("{\"success\":false, \"message\":\"聊天室 ID 参数缺失\"}");
            return;
        }
        int id = Integer.parseInt(idParam.trim());
        int result = chatroomDao.delete(id);
        if (result > 0) {
            out.write("{\"success\":true, \"message\":\"聊天室删除成功\"}");
        } else {
            out.write("{\"success\":false, \"message\":\"聊天室删除失败\"}");
        }
    }

    private void handleDeleteMessage(HttpServletRequest req, PrintWriter out) throws SQLException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            out.write("{\"success\":false, \"message\":\"消息 ID 参数缺失\"}");
            return;
        }
        int id = Integer.parseInt(idParam.trim());
        int result = messageDao.delete(id);
        if (result > 0) {
            out.write("{\"success\":true, \"message\":\"消息删除成功\"}");
        } else {
            out.write("{\"success\":false, \"message\":\"消息删除失败\"}");
        }
    }

    private void handleBatchDeleteMessages(HttpServletRequest req, PrintWriter out) throws SQLException {
        String idsParam = req.getParameter("ids");
        if (idsParam == null || idsParam.trim().isEmpty()) {
            out.write("{\"success\":false, \"message\":\"消息 ID 列表参数缺失\"}");
            return;
        }
        List<Integer> ids = Arrays.stream(idsParam.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        int result = messageDao.batchDelete(ids);
        if (result > 0) {
            out.write("{\"success\":true, \"message\":\"消息批量删除成功\"}");
        } else {
            out.write("{\"success\":false, \"message\":\"消息批量删除失败\"}");
        }
    }
}
