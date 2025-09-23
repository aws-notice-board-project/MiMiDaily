package com.mimidaily.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class SessionDebugFilter implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {
        System.out.println("SessionDebugFilter 초기화됨");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        
        // 정적 리소스는 로깅하지 않음
        if (requestURI.contains(".css") || requestURI.contains(".js") || 
            requestURI.contains(".png") || requestURI.contains(".jpg") || 
            requestURI.contains(".ico") || requestURI.contains(".svg")) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpSession session = httpRequest.getSession(false);
        
        System.out.println("=== 세션 디버그 ===");
        System.out.println("요청 URI: " + requestURI);
        
        if (session != null) {
            System.out.println("세션 ID: " + session.getId());
            System.out.println("세션 생성 시간: " + new java.util.Date(session.getCreationTime()));
            System.out.println("마지막 접근 시간: " + new java.util.Date(session.getLastAccessedTime()));
            System.out.println("로그인 사용자: " + session.getAttribute("loginUser"));
            System.out.println("사용자 역할: " + session.getAttribute("userRole"));
            System.out.println("방문 횟수: " + session.getAttribute("visitCnt"));
            
            // 세션이 유효한지 확인
            try {
                session.getAttribute("loginUser"); // 세션 접근 테스트
                System.out.println("세션 상태: 활성");
            } catch (IllegalStateException e) {
                System.out.println("세션 상태: 무효화됨");
            }
        } else {
            System.out.println("세션 없음");
        }
        System.out.println("==================");
        
        chain.doFilter(request, response);
    }

    public void destroy() {
        System.out.println("SessionDebugFilter 종료됨");
    }
}
