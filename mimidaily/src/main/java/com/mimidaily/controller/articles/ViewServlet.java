package com.mimidaily.controller.articles;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dao.CommentsDAO;
import com.mimidaily.dao.MemberDAO;
import com.mimidaily.dto.ArticlesDTO;
import com.mimidaily.dto.CommentsDTO;
import com.mimidaily.dto.MemberDTO;

/**
 * Servlet implementation class ViewServlet
 */
@WebServlet("/articles/view.do")
public class ViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String memberId = (String) request.getSession().getAttribute("loginUser");
        String redirectURL = request.getParameter("redirectURL");
		// 게시물
		ArticlesDAO aDao = new ArticlesDAO();
		// 글쓴이(기자)
		MemberDAO mDao = new MemberDAO();
		// 댓글 갯수
		CommentsDAO cDao = new CommentsDAO();
		
        String idx = request.getParameter("idx");
        aDao.updateVisitCount(idx);  // 조회수 1 증가
        ArticlesDTO aDto = aDao.selectView(idx, memberId); // 게시글 불러오기
        List<ArticlesDTO> viewestList=aDao.viewestList(); // 실시간 관심기사 best4
        MemberDTO wDto = mDao.userInfo(aDto.getMembers_id()); // 글쓴이 정보
        MemberDTO mDto = mDao.userInfo(memberId); // 접속자 정보
        
        int intIdx = Integer.parseInt(idx);
        int commentCnt=cDao.commentsCount(intIdx); //  댓글 갯수
        

        // 줄바꿈 처리
        aDto.setContent(aDto.getContent().replaceAll("\r\n", "<br/>"));
        
        // 자원회수
        aDao.close();
        mDao.close();
        cDao.close();
        
        //첨부파일 확장자 추출 및 이미지 타입 확인
        // String ext = null, fileName = dto.getSfile();
        // if(fileName!=null) {
        // 	ext = fileName.substring(fileName.lastIndexOf(".")+1);
        // }
        // String[] mimeStr = {"png","jpg","gif"};
        // List<String> mimeList = Arrays.asList(mimeStr);
        // boolean isImage = false;
        // if(mimeList.contains(ext)) {
        // 	isImage = true;
        // }
        
        request.setAttribute("redirectURL", redirectURL);
        // 게시물(dto) 저장 후 뷰로 포워드
        request.setAttribute("commentCnt", commentCnt); // 댓글 갯수
        request.setAttribute("writer", wDto); // 글쓴이 정보
        request.setAttribute("member", mDto); // 접속자 정보
        request.setAttribute("article", aDto); // 게시글 정보
        request.setAttribute("viewestList", viewestList); // 최신 기사
        //request.setAttribute("isImage", isImage);
        request.getRequestDispatcher("/articles/view.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
