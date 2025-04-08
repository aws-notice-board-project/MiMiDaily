package com.mimidaily.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesTDAO;
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
		String referer = request.getHeader("Referer"); // 현재 페이지
		request.getSession().setAttribute("previousPage", referer); // 세션에 저장
		
		ArticlesTDAO dao=new ArticlesTDAO();
		List<ArticlesDTO> viewestList=dao.viewestList(); // 실시간 관심기사 best4
		for(ArticlesDTO i:viewestList) {
			i.getFormattedDate();
		}
		
		String searchField = request.getParameter("searchField");
        String searchWord = request.getParameter("searchWord");
        if (searchField != null && searchWord != null && !searchWord.trim().isEmpty()) { // 검색조건이 존재하면
        	System.out.println("검색어 존재"); 
            response.sendRedirect("/articles/newest.do?searchField=" + searchField + "&searchWord=" + searchWord);
            return; // 이후 코드 실행 방지 
        }

        request.setAttribute("actionUrl", "main.do");
        request.setAttribute("viewestList", viewestList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
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
