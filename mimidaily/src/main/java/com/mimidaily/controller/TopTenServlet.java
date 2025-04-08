package com.mimidaily.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.TopTenDAO;

/**
 * Servlet implementation class Top10Servlet
 */
@WebServlet("/articles/topten.do")
public class TopTenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopTenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// DAO 생성
		TopTenDAO ttdao = new TopTenDAO();
		
		// 뷰에 전달할 매개변수 저장용 맵 생성
//        Map<String, Object> map = new HashMap<String, Object>();
//
//        String searchField = req.getParameter("searchField");
//        String searchWord = req.getParameter("searchWord");
//        if (searchWord != null) {
//            // 쿼리스트링으로 전달받은 매개변수 중 검색어가 있다면 map에 저장
//            map.put("searchField", searchField);
//            map.put("searchWord", searchWord);
//        }
//        int totalCount = dao.selectCount(map);  // 게시물 개수
//
//        /* 페이지 처리 start */
////	        ServletContext application = getServletContext();
////	        int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
////	        int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));
//        int pageSize = 10; //한 페이지에 출려할 글의 개수
//        int blockPage = 5; // 페이지 번호의 개수. 1.2.3.4.5
//        
//        // 현재 페이지 확인
//        int pageNum = 1;  // 기본값
//        String pageTemp = req.getParameter("pageNum");
//        if (pageTemp != null && !pageTemp.equals(""))
//            pageNum = Integer.parseInt(pageTemp); // 요청받은 페이지로 수정
//
//        // 목록에 출력할 게시물 범위 계산
//        int start = (pageNum - 1) * pageSize + 1;  // 첫 게시물 번호
//        int end = pageNum * pageSize; // 마지막 게시물 번호
//        map.put("start", start);
//        map.put("end", end);
//        /* 페이지 처리 end */
//
//        List<MVCBoardDTO> boardLists = dao.selectListPage(map);  // 게시물 목록 받기
//        dao.close(); // DB 연결 닫기
//
//        // 뷰에 전달할 매개변수 추가
//        // 1.2.3.4.5 페이지번호 생성
//        String pagingImg = BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum, "../mvcboard/list.do");  // 바로가기 영역 HTML 문자열
//        map.put("pagingImg", pagingImg);
//        map.put("totalCount", totalCount);
//        map.put("pageSize", pageSize);
//        map.put("pageNum", pageNum);
//
//        // 전달할 데이터를 request 영역에 저장 후 List.jsp로 포워드
//        req.setAttribute("boardLists", boardLists);
//        req.setAttribute("map", map);
        request.getRequestDispatcher("/articles/topten.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
