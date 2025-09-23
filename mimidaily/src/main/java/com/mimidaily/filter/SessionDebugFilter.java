package com.mimidaily.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
// import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// 프로덕션 환경에서는 비활성화
// @WebFilter("/*")
public class SessionDebugFilter implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {
        // 비활성화됨
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // 디버그 필터 비활성화 - 그냥 체인으로 넘김
        chain.doFilter(request, response);
    }

    public void destroy() {
        // 비활성화됨
    }
}
