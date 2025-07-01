package edu.csust.filter;

import edu.csust.entity.User;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AuthFilter implements Filter {
    // 允许访问的路径（新增/admin/login）
    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList(
        "/login",
        "/register",
        "/static",
        "/login.jsp",
        "/register.jsp",
        "/admin"
    ));

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // 允许静态资源和登录注册页面
        if (isAllowed(path)) {
            chain.doFilter(req, res);
            return;
        }

        // 管理员路径检查
        if (path.startsWith("/admin")) {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null || !"admin".equals(user.getUname())) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
        }
        // 普通用户检查
        else {
            if (request.getSession().getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private boolean isAllowed(String path) {
        return ALLOWED_PATHS.stream().anyMatch(path::startsWith);
    }
}
