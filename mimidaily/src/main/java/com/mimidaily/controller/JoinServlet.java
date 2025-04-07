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

@WebServlet("/join.do")
public class JoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public JoinServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "member/join.jsp";
		// 기자인지 일반 회원인지 구분
//		String type;
//		if ("reporter".equals(request.getParameter("role"))) {
//		    type = "reporter";
//		} else {
//		    type = "user";
//		}
//		request.setAttribute("job", type);
//		System.out.println(request.getParameter("role"));
//		System.out.println(type);
		
		// id 중복 체크 전달(id, error_msg)
		String id = request.getParameter("id");
		String ide;
		MemberDAO mDao = new MemberDAO();
		int idOk = mDao.confirmID(id);
		if(idOk == 1) {
			ide = "사용 불가능한 아이디 입니다.";
		} else {
			ide = "사용 가능한 아이디 입니다.";
		}
		request.setAttribute("id_error", ide);
		
		// 기자 인증 코드 확인?
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);//주소가 변경되지 않음.
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "member/join.jsp";
		String id = request.getParameter("id");
		String pwd = request.getParameter("pw");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		MemberDTO mDto = new MemberDTO();
		mDto.setId(id);
		mDto.setPwd(pwd);
		mDto.setName(name);
		mDto.setEmail(email);
		mDto.setTel(tel);
		MemberDAO mDao = new MemberDAO();
		int result = mDao.insertMember(mDto);
		HttpSession session = request.getSession();
		if (result == 1) {
			session.setAttribute("id", mDto.getId());
			request.setAttribute("success_msg", "회원 가입에 성공했습니다.");
			url = "member/login.jsp";
		} else {
			request.setAttribute("success_msg", "회원 가입에 실패했습니다.");
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
}