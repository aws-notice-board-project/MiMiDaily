package com.mimidaily.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mimidaily.dao.ArticlesEDAO;
import com.mimidaily.dto.ArticlesDTO;

/**
 * Servlet implementation class WriteServlet
 */
@WebServlet("/articles/write.do")
public class WriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/articles/write.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   // 1. 파일 업로드 처리 =============================
        // 업로드 디렉터리의 물리적 경로 확인
        //String saveDirectory = request.getServletContext().getRealPath("/Uploads");
        //System.out.println(saveDirectory);
        // 파일 업로드
        //String originalFileName = "";
        //try {
        //	originalFileName = FileUtil.uploadFile(request, saveDirectory);
        //}
        //catch (Exception e) {
        //	JSFunction.alertLocation(response, "파일 업로드 오류입니다.",
        //            "../mvcboard/write.do");
        //	return;
		//}

        // 2. 파일 업로드 외 처리 =============================
        // 폼값을 DTO에 저장
		ArticlesDTO dto = new ArticlesDTO(); 
        //dto.setIdx(request.getParameter("idx"));
		dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        dto.setCategory(1);
        // String categoryParam = request.getParameter("category");
        // if (categoryParam != null && !categoryParam.isEmpty()) {
        //     try {
        //         int category = Integer.parseInt(categoryParam);
        //         if (category == 1 || category == 2) {
        //             dto.setCategory(category);
        //             System.out.println("카테고리 값: " + category);
        //         } else {
        //             System.out.println("잘못된 카테고리 입력");
        //             response.sendRedirect("/write.jsp?error=category");
        //             return;
        //         }
        //     } catch (NumberFormatException e) {
        //         System.out.println("카테고리 숫자 변환 실패");
        //         response.sendRedirect("/write.jsp?error=category");
        //         return;
        //     }
        // } else {
        //     System.out.println("카테고리 입력 없음");
        //     response.sendRedirect("/write.jsp?error=category");
        //     return;
        // }
        
        String createdAtParam = request.getParameter("created_at");
        if (createdAtParam != null && !createdAtParam.isEmpty()) {
            dto.setCreated_at(Timestamp.valueOf(createdAtParam));
        } else {
            dto.setCreated_at(new Timestamp(System.currentTimeMillis())); // 기본값
        }
        //dto.setCreated_at(Timestamp.valueOf(request.getParameter("created_at")));
        //dto.setVisitcnt(request.getParameter("visitcnt"));
        dto.setMembers_id(request.getParameter("members_id"));
        //dto.setThumnails_idx(request.getParameter("thumnails_idx"));
        
        // 원본 파일명과 저장된 파일 이름 설정
        //if (originalFileName != "") { 
        	// 파일명 변경
        //	String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);	
        //    dto.setOfile(originalFileName);  // 원래 파일 이름
        //    dto.setSfile(savedFileName);  // 서버에 저장된 파일 이름
        //}

        // DAO를 통해 DB에 게시 내용 저장
        ArticlesEDAO dao = new ArticlesEDAO();
        int result = dao.insertWrite(dto);
        dao.close();

        // 성공 or 실패?
        if (result == 1) {  // 글쓰기 성공
        	response.sendRedirect("/articles/musteat.do");
        }
        else {  // 글쓰기 실패
        	// JSFunction.alertLocation(response, "글쓰기에 실패했습니다.",
            //         "../mvcboard/write.do");
        	System.out.println("글쓰기 실패");
        }
	}

}
