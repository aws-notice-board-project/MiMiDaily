package com.mimidaily.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesTDAO;
import com.mimidaily.dto.ArticlesDTO;

import utils.ArticlesPagination;

/**
 * Servlet implementation class TravelServlet
 */
@WebServlet("/articles/travel.do")
public class TravelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TravelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArticlesTDAO dao=new ArticlesTDAO();
        
        // 전달할 매개변수 저장용 맵(유연성, 가독성, 재사용성)
        Map<String,Object> map=new HashMap<String,Object>();
        
        int totalCnt=dao.selectCnt(map); // 게시물 갯수
        int pageSize=10; // 한 페이지에 출력할 글의 갯수
        int blockPage=5; // 페이지 번호의 갯수
        
        int pageNum=1; // 기본값(첫페이지)
        String pageTemp=request.getParameter("pageNum");
        // 파라미터에 값이 있을 시, 요청 받은 페이지로 수정
        if(pageTemp!=null && !pageTemp.equals("")) pageNum=Integer.parseInt(pageTemp);
        
        // 목록에 출력할 게시물 범위 계산
        int start=((pageNum-1)*pageSize)+1; // 1 11 21 ...(첫 게시물 번호)
        int end=pageNum*pageSize; // 10 20 30 ...(마지막 게시물 번호)
        map.put("start", start);
        map.put("end", end);
        
        List<ArticlesDTO> articleLists=dao.selectListPage(map); // 게시물 목록 받기
        dao.close(); // DB연결 close
        
        // pagenation
        String paging=ArticlesPagination.pagingBox(totalCnt, pageSize, blockPage, pageNum, "../articles/travel.do");
        map.put("paging", paging);
        map.put("totalCnt", totalCnt);
        map.put("pageSize", pageSize);
		map.put("pageNum", pageNum);
		
        // 전달할 데이터 request 영역에 저장 ("이름",데이터)
        request.setAttribute("articleLists", articleLists);
        request.setAttribute("map", map);
		request.getRequestDispatcher("/articles/travel.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
