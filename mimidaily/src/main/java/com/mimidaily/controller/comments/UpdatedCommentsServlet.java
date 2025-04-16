package com.mimidaily.controller.comments;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.CommentsDAO;
import com.mimidaily.dto.CommentsDTO;

/**
 * Servlet implementation class UpdatedCommentsServlet
 */
@WebServlet("/comments/update.do")
public class UpdatedCommentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatedCommentsServlet() {
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
		CommentsDAO dao=new CommentsDAO();
		int commentIdx=Integer.parseInt(request.getParameter("commentIdx"));
		String context=request.getParameter("comment");
		System.out.println(context);
		if(commentIdx>0) {
			CommentsDTO updatedComment=dao.updateComment(commentIdx, context);
			if(updatedComment!=null) {
				// 응답 설정
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				
				// JSON 문자열 수동 생성
				String jsonResponse = 
						"{" +
						"\"idx\":" + updatedComment.getIdx() + "," +
						"\"context\":\"" + updatedComment.getContext().replace("\"", "\\\"") + "\"," +
						"\"is_updated\":\"" + updatedComment.getIs_updated() + "\"" +
						"}";
				
				System.out.println("댓글 수정 성공");
				// 클라이언트에 응답 전송
				response.getWriter().print(jsonResponse);
			}
		}else{System.out.println("유효하지 않은 숫자 : "+commentIdx);}
		dao.close();
		
	}

}
