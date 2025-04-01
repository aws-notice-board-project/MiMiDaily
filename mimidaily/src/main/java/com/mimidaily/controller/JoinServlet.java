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
import com.mimidaily.dto.MemberDTO;

/**
 * Servlet implementation class JoinServlet
 */
@WebServlet("/join.do")
public class JoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JoinServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "member/join.jsp"; 
		String reporter = request.getParameter("who");
		request.setAttribute("reporter", reporter);
		System.out.println(reporter);
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);//주소가 변경되지 않음.
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "member/login.jsp";
		String id = request.getParameter("id");
		String pwd = request.getParameter("pw");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
//		String birth = request.getParameter("birth");
//		String gender = request.getParameter("gender");
		MemberDTO mDto = new MemberDTO();
		mDto.setId(id);
		mDto.setPwd(pwd);
		mDto.setName(name);
		mDto.setEmail(email);
		mDto.setTel(tel);
//		mDto.setAdmin(Integer.parseInt(admin));
//		MemberDAO mDao = MemberDAO.getInstance();
//		int result = mDao.insertMember(mDto);
//		HttpSession session = request.getSession();
//		if (result == 1) {
//			session.setAttribute("id", mDto.getUserid());
//			request.setAttribute("message", "회원 가입에 성공했습니다.");
//			RequestDispatcher dispatcher = request.getRequestDispatcher(url);
//		} else {
//			request.setAttribute("message", "회원 가입에 실패했습니다.");
//		}
//		dispatcher.forward(request, response);
	}

}
