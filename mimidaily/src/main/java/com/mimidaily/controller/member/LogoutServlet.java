package com.mimidaily.controller.member;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout.do")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.invalidate(); // session 비활성 모든 session attribute 삭제
		
		// 로그아웃 시 url
		String previousPage = request.getHeader("Referer"); // 이전 페이지
		String url=null;
		String lastPath = null;
		if (previousPage != null) {
			String[] parts = previousPage.split("/");
			lastPath="/"+parts[parts.length - 1];
		}
		if(lastPath != null && !lastPath.equals("/") && !lastPath.equals("/main.do")) {
			url = previousPage;
		}else {url = "main.do";}

		response.sendRedirect(url); //주소변경
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
