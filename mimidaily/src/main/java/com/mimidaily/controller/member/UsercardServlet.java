package com.mimidaily.controller.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.MemberDAO;
import com.mimidaily.dto.MemberInfoDTO;

/**
 * Servlet implementation class UsercardServlet
 */
@WebServlet("/member/usercard.do")
public class UsercardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsercardServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String memberId = (String) request.getSession().getAttribute("loginUser");
		MemberDAO dao = new MemberDAO();
		MemberInfoDTO dto = dao.getMemberInfo(memberId);
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

        // JSON 문자열 수동 생성
		String jsonResponse = 
			    "{" +
			    "\"id\":\"" + dto.getId() + "\"," +
			    "\"name\":\"" + dto.getName() + "\"," +
			    "\"articleCnt\":" + dto.getArticleCount() + "," +
			    "\"commentCnt\":" + dto.getCommentCount() + "," +
			    "\"createdAt\":\"" + dto.getCreatedAt() + "\"" +
			    "}";
		dao.close();
        // 클라이언트에 응답 전송
        response.getWriter().print(jsonResponse);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
