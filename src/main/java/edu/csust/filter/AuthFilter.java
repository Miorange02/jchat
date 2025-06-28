package edu.csust.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AuthFilter implements Filter {
    // Java 8兼容的初始化方式
    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList(
        "/login",
        "/register",
        "/static",
        "/login.jsp",
        "/register.jsp"
    ));

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        // 检查是否为允许路径或已登录
        boolean isAllowed = false;
        for (String allowedPath : ALLOWED_PATHS) {
            if (path.startsWith(allowedPath)) {
                isAllowed = true;
                break;
            }
        }

        if (isAllowed || request.getSession().getAttribute("user") != null) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码（如有需要）
    }

    @Override
    public void destroy() {
        // 清理代码（如有需要）
    }
}
