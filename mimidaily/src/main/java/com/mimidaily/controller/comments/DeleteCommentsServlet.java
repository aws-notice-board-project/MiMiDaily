package com.mimidaily.controller.comments;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.CommentsDAO;

/**
 * Servlet implementation class DeleteServlet
 */
@WebServlet("/comments/delete.do")
public class DeleteCommentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCommentsServlet() {
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
		if(commentIdx>0) {
			int result=dao.deleteComments(commentIdx);
			if(result>0) {
				System.out.println("댓글 삭제 성공");
			}
		}else{System.out.println("유효하지 않은 숫자 : "+commentIdx);}
		dao.close();
		
		response.getWriter().print("Delete Comment Servlet");
	}

}
