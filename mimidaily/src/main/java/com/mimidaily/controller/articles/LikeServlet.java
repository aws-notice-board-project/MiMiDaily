package com.mimidaily.controller.articles;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.LikeDAO;

/**
 * Servlet implementation class LikeServlet
 */
@WebServlet("/articles/like.do")
public class LikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String idx = request.getParameter("id");
		String userId = (String) request.getSession().getAttribute("loginUser");

		LikeDAO dao = new LikeDAO();
		boolean isLiked = dao.toggleLike(idx, userId);  // 좋아요 처리
		int likeCnt = dao.getLikeCnt(idx);  // 좋아요 개수
		dao.close();

		// 응답 헤더 세팅
	    response.setContentType("application/json; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");

	    // JSON 문자열 수동 생성
	    String jsonResponse = String.format(
	        "{\"success\": true, \"liked\": %b, \"likeCount\": %d}",
	        isLiked,
	        likeCnt
	    );

	    // JSON 응답 출력
	    response.getWriter().print(jsonResponse);
	}

}
