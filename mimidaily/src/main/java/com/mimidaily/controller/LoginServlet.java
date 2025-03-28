package com.mimidaily.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mimidaily.dao.MemberDAO;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 String url = "member/login.jsp"; 
		
		/*
		 * HttpSession session = request.getSession(); // session 객체 구하기 if
		 * (session.getAttribute("loginUser") != null) {// 이미 로그인 된 사용자이면 url =
		 * "main.jsp"; // 메인 페이지로 이동한다. }
		 */
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);//주소가 변경되지 않음.
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "member/login.jsp";
		String userid = request.getParameter("userid");
		String pwd = request.getParameter("pwd");
		/* MemberDAO mDao = new MemberDAO(); */
		 MemberDAO mDao = MemberDAO.getInstance(); 
		int result = mDao.userCheck(userid, pwd);
		if (result == 1) {// id,비밀번호가 일치할 때
			//MemberDTO mVo = mDao.getMember(userid); /* 로그인할때마다 정보가져오는거 (굳이 로그인할때마다 정보를 다 가져온다? 별로같아서 주석 */
			HttpSession session = request.getSession();
			//  session.setAttribute("loginUser", mVo);
			 session.setAttribute("loginUser", userid);
			url = "main.do";
			
			response.sendRedirect(url); //주소변경
		} else if (result == 0) {//비밀번호가 일치하지 않을 때
			request.setAttribute("message", "비밀번호가 맞지 않습니다.");
			RequestDispatcher dispatcher = request.getRequestDispatcher(url);
			dispatcher.forward(request, response);
		} else if (result == -1) {//id가 존재하지 않을 떄
			request.setAttribute("message", "존재하지 않는 id입니다.");
			RequestDispatcher dispatcher = request.getRequestDispatcher(url);
			dispatcher.forward(request, response);

		}
	}

}
