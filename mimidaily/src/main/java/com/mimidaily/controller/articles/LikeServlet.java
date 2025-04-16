package com.mimidaily.controller.articles;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesDAO;

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
		ArticlesDAO dao = new ArticlesDAO();
		String memberId = (String) request.getSession().getAttribute("loginUser");
		String articlesIdStr = request.getParameter("articleIdx");
	    int articleIdx = 0;
	    try {
	        // 문자열을 int로 변환
	        articleIdx = Integer.parseInt(articlesIdStr);
	    } catch (NumberFormatException e) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid article ID");
	        return; // 잘못된 ID인 경우 종료
	    }
	    

	    // 좋아요 추가 또는 제거 로직
	    boolean isLiked = dao.isLiked(memberId, articleIdx);
	    boolean success;

	    if (isLiked) {
	        success = dao.removeLike(memberId, articleIdx);
	    } else {
	        success = dao.addLike(memberId, articleIdx);
	    }
	    dao.close();

	    // JSON 응답
	    response.setContentType("application/json; charset=UTF-8");
	    response.getWriter().print("{\"success\": " + success + ", \"liked\": " + !isLiked + "}");
	}

}
