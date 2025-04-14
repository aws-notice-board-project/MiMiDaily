package com.mimidaily.controller.comments;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.CommentsDAO;
import com.mimidaily.dao.MemberDAO;
import com.mimidaily.dto.CommentsDTO;
import com.mimidaily.dto.MemberDTO;

/**
 * Servlet implementation class ListCommentsServlet
 */
@WebServlet("/comments/list.do")
public class ListCommentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListCommentsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 댓글 더보기
		int page = Integer.parseInt(request.getParameter("page")); // js로 받은 page 번호
		int limit = 10; // 10개씩
		int start = (page - 1) * limit;
		int end = page * limit;
		
		// 댓글 목록
		CommentsDAO cDao = new CommentsDAO();
		int articleIdx = Integer.parseInt(request.getParameter("articleIdx"));
		List<CommentsDTO> commentsList=cDao.selectComments(articleIdx, start, end); // 댓글 목록
        
        
        
        // 현재 유저
        MemberDAO mDao = new MemberDAO();
        String memberId = (String) request.getSession().getAttribute("loginUser");
        MemberDTO mDto = mDao.userInfo(memberId); // 현재 유저(수정필요/프로필사진)
        
        cDao.close();
        mDao.close();
        
        request.setAttribute("articleIdx", articleIdx); // 현재 유저
        request.setAttribute("member", mDto); // 현재 유저
        request.setAttribute("commentsList", commentsList); // 댓글 목록
        
        request.getRequestDispatcher("/components/comments.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
