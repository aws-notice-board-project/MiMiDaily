package com.mimidaily.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dto.ArticlesDTO;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/main.do")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션 디버깅
		HttpSession session = request.getSession(false);
		if (session != null) {
			System.out.println("MainServlet - 세션 정보:");
			System.out.println("Session ID: " + session.getId());
			System.out.println("loginUser: " + session.getAttribute("loginUser"));
			System.out.println("userRole: " + session.getAttribute("userRole"));
			System.out.println("visitCnt: " + session.getAttribute("visitCnt"));
		} else {
			System.out.println("MainServlet - 세션이 없습니다.");
		}
		
		ArticlesDAO dao=new ArticlesDAO();
		List<ArticlesDTO> viewestList=dao.viewestList(); // 실시간 관심기사 best4
		List<ArticlesDTO> toptenList = dao.selectTopArticles();// 게시글 상위 10개 조회
		dao.close();
		
		String searchField = request.getParameter("searchField");
        String searchWord = request.getParameter("searchWord");
        if (searchField != null && searchWord != null && !searchWord.trim().isEmpty()) { // 검색조건이 존재하면
            response.sendRedirect("/articles/newest.do?searchField=" + searchField + "&searchWord=" + searchWord);
            return;
        }
        request.setAttribute("actionUrl", "/articles/newest.do"); // 검색하면 최신기사로 검색되는 것을 위함
        request.setAttribute("viewestList", viewestList);
        request.setAttribute("bestnews", toptenList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/main.jsp");
        dispatcher.forward(request, response);        	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
