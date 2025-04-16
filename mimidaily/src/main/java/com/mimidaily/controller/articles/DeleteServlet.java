package com.mimidaily.controller.articles;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesDAO;

@WebServlet("/articles/delete.do")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
    public DeleteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String referer = request.getHeader("Referer"); // 이전 페이지
		request.getSession().setAttribute("previousPage", referer); // 세션에 저장
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  // 게시글 idx 파라미터 얻기 (삭제할 게시글 번호)
        String idx = request.getParameter("idx");
        
        // DAO를 통해 삭제 수행
        ArticlesDAO dao = new ArticlesDAO();
        int result = dao.deletePost(idx); // 삭제된 행의 수
        
        // 삭제 성공 시 페이지 이동 처리
        String redirectURL = request.getParameter("redirectURL");
        if (result > 0) {
            if(redirectURL != null && !redirectURL.trim().isEmpty()){
                response.sendRedirect(request.getContextPath() + "/articles/" + redirectURL);
            } else {
                // 파라미터가 없으면 기본 리스트 페이지로 이동
                response.sendRedirect(request.getContextPath() + "/main.do");
            }
        } else {
            System.out.println("기사 삭제 실패");
        }

	}

}
