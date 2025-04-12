package com.mimidaily.controller.comments;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dao.CommentsDAO;
import com.mimidaily.dto.ArticlesDTO;
import com.mimidaily.dto.CommentsDTO;
import com.mimidaily.dto.MemberDTO;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/comments.do")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
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
		String memberId = (String) request.getParameter("memberId"); // 현재 유저
		String idxStr = request.getParameter("articleIdx"); // 문자열로 가져오기
		int articleIdx = 0; // 기본값 설정
		if (idxStr != null && !idxStr.isEmpty()) {
		    try {
		    	articleIdx = Integer.parseInt(idxStr); // 문자열을 정수로 변환
		    } catch (NumberFormatException e) {
		        System.out.println("유효하지 않은 숫자입니다: " + idxStr);
		        // 예외 처리 (예: 기본값 사용, 에러 메시지 전송 등)
		    }
		}
		CommentsDAO cDao = new CommentsDAO();
		CommentsDTO cDto = new CommentsDTO();
		
		// 댓글 생성 + 값 받아오기
        String context = request.getParameter("comment"); // 댓글 내용 가져오기
        cDto.setContext(context);
        cDto.setMembers_id(memberId);
        cDto.setArticles_idx(articleIdx);
        CommentsDTO insertedComment = cDao.insertComments(cDto); // 댓글 생성
        
        cDao.close();
        request.setAttribute("insertedComment", insertedComment); // 생성된 댓글
        
        response.getWriter().print("성공");
	}

}
