package com.mimidaily.controller.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.MemberDAO;

/**
 * Servlet implementation class PwdChkServlet
 */
@WebServlet("/member/pwdchk.do")
public class PwdChkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PwdChkServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDAO dao=new MemberDAO();
		String memberId = (String) request.getSession().getAttribute("loginUser");
		String pwd=request.getParameter("chkpwd");
		int result=dao.pwdChk(pwd, memberId);
		dao.close();
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

        // JSON 문자열 수동 생성
		if(result>0) { // 비밀번호 일치
			String jsonResponse = 
			        "{" +
			        "\"result\":\"success\"," +
			        "\"pwdchk\":" + true +
			        "}";			
			// 클라이언트에 응답 전송
			response.getWriter().print(jsonResponse);
		}else { // 비밀번호 불일치
			String jsonResponse = 
			        "{" +
			        "\"result\":\"success\"," +
			        "\"pwdchk\":" + false +
			        "}";		
			// 클라이언트에 응답 전송
			response.getWriter().print(jsonResponse);
		}
	}

}
