package com.mimidaily.controller.articles;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dao.MemberDAO;
import com.mimidaily.dto.ArticlesDTO;
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
		// 게시물 불러오기
		ArticlesDAO aDao = new ArticlesDAO();
		// 글쓴이(기자) 불러오기
		MemberDAO mDao = new MemberDAO();
		
        String idx = request.getParameter("idx");
        aDao.updateVisitCount(idx);  // 조회수 1 증가
        ArticlesDTO aDto = aDao.selectView(idx, memberId);
        List<ArticlesDTO> viewestList=aDao.viewestList(); // 실시간 관심기사 best4
        MemberDTO wDto = mDao.writer(aDto.getMembers_id()); // 글쓴이
        MemberDTO mDto = mDao.writer(aDto.getMembers_id()); // 현재 유저(수정필요)
        // 줄바꿈 처리
        aDto.setContent(aDto.getContent().replaceAll("\r\n", "<br/>"));
        
        aDao.close();
        mDao.close();
        
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
        request.setAttribute("writer", wDto); // 글쓴이
        request.setAttribute("member", mDto); // 현재 유저
        request.setAttribute("article", aDto);
        request.setAttribute("viewestList", viewestList);
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
