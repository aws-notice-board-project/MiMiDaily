package com.mimidaily.controller.articles;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dto.ArticlesDTO;

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
		 // 게시물 불러오기
		ArticlesDAO dao = new ArticlesDAO();
        String idx = request.getParameter("idx");
        dao.updateVisitCount(idx);  // 조회수 1 증가
        ArticlesDTO dto = dao.selectView(idx);
        List<ArticlesDTO> viewestList=dao.viewestList(); // 실시간 관심기사 best4
        dao.close();

        // 줄바꿈 처리
        dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
        
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
        
        
        // 게시물(dto) 저장 후 뷰로 포워드
        request.setAttribute("dto", dto);
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
