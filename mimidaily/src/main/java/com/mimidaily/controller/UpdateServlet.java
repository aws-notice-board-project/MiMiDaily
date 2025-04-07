package com.mimidaily.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.MemberDAO;
import com.mimidaily.dto.MemberDTO;

@WebServlet("/update.do")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "member/update.jsp";
		String userid = request.getParameter("userid");
		MemberDAO mDao = new MemberDAO();
		MemberDTO mDto = mDao.getMember(userid);
		request.setAttribute("mDto", mDto);
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 한글 깨짐을 방지
		// 폼에서 입력한 회원 정보 얻어오기
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
//		String admin = request.getParameter("admin");
		// 회원 정보를 저장할 Model 객체 생성
		MemberDTO mDto = new MemberDTO();
		mDto.setId(id);
		mDto.setPwd(pwd);
		mDto.setEmail(email);
		mDto.setTel(tel);
//		mDto.setMarketing(Boolean(admin));
		MemberDAO mDao = new MemberDAO();
		mDao.updateMember(mDto);
		response.sendRedirect("login.do");
	}
}