package com.mimidaily.controller.member;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dao.MemberDAO;
import com.mimidaily.dto.ArticlesDTO;
import com.mimidaily.dto.MemberDTO;

@WebServlet("/update.do")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/member/update.jsp";
        String memberId = (String) request.getSession().getAttribute("loginUser");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.getMember(memberId);
		request.setAttribute("member", dto);
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 한글 깨짐을 방지
		String url = "/member/update.jsp";
		// 폼에서 입력한 회원 정보 얻어오기
		String id = request.getParameter("id");
		String pwd = request.getParameter("pw");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		String birth = request.getParameter("birth");
		String gender = request.getParameter("gender");
		int role = Integer.parseInt(request.getParameter("role"));
		Boolean marketing = "1".equals(request.getParameter("marketing"));
		MemberDTO mDto = new MemberDTO();
		mDto.setId(id);
		mDto.setPwd(pwd);
		mDto.setName(name);
		mDto.setEmail(email);
		mDto.setTel(tel);
		mDto.setBirth(birth);
		mDto.setGender(gender);
		mDto.setRole(role);
		mDto.setMarketing(marketing);
		MemberDAO mDao = new MemberDAO();
		int result = mDao.updateMember(mDto);
		HttpSession session = request.getSession();
		if (result == 1) {
			session.setAttribute("id", mDto.getId());
			request.setAttribute("success_msg", "회원 정보 수정에 성공했습니다.");
			url = "main.do";
		} else {
			request.setAttribute("success_msg", "회원 정보 수정에 실패했습니다.");
		}
		mDao.close();
		response.sendRedirect(url);
	}
}