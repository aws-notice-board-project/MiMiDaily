package com.mimidaily.controller.articles;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dto.ArticlesDTO;
import com.mimidaily.utils.FileUtil;

/**
 * Servlet implementation class WriteServlet
 */
@WebServlet("/articles/edit.do")
@MultipartConfig(maxFileSize = 1024 * 1024 * 1, // 파일업로드할때 최대 사이즈
        maxRequestSize = 1024 * 1024 * 10 // 여러개의 파일 업로드할때 총합 사이즈
)
public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String idx = request.getParameter("idx");
		ArticlesDAO dao = new ArticlesDAO();
		ArticlesDTO dto = dao.selectView(idx);
		request.setAttribute("dto", dto);
		request.getRequestDispatcher("/articles/edit.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. 파일 업로드 처리 =============================
        // 업로드 디렉터리의 물리적 경로 확인
        String saveDirectory = request.getServletContext().getRealPath("/uploads");
        System.out.println(saveDirectory);

        // 파일 업로드
        Part filePart = request.getPart("ofile");
        String originalFileName = "";
        if (filePart != null && filePart.getSize() > 0) {
            try {
                originalFileName = FileUtil.uploadFile(request, saveDirectory);
            } catch (Exception e) {
                System.out.println("파일 업로드 오류입니다.");
                e.printStackTrace();
                // 파일 업로드 오류가 발생해도 글은 작성되도록 처리할 수 있습니다.
                originalFileName = "";
            }
        } else {
            System.out.println("파일이 선택되지 않았습니다.");
        }
          
        // 2. 파일 업로드 외 처리 =============================
        // 수정 내용을 매개변수에서 얻어옴
		String idx = request.getParameter("idx");
		String members_id = request.getParameter("members_id");
        String prevOfile = request.getParameter("prevOfile");
        String prevSfile = request.getParameter("prevSfile");
        String title = request.getParameter("title");                                         
        String content = request.getParameter("content");


        ArticlesDTO dto = new ArticlesDTO();
		dto.setMembers_id(members_id);
        dto.setTitle(title);
        dto.setContent(content);


        String categoryParam = request.getParameter("category");
        if (categoryParam != null && !categoryParam.isEmpty()) {
            try {
                dto.setCategory(Integer.parseInt(categoryParam));
            } catch (NumberFormatException e) {
                dto.setCategory(1); // 기본값 예시
            }
        } else {
            dto.setCategory(1); // 기본값
        }

        String createdAtParam = request.getParameter("created_at");
        if (createdAtParam != null && !createdAtParam.isEmpty()) {
            dto.setCreated_at(Timestamp.valueOf(createdAtParam));
        } else {
            dto.setCreated_at(new Timestamp(System.currentTimeMillis())); // 기본값
        }
        

   // 원본 파일명과 저장된 파일 이름 설정
        if (originalFileName != "") {             
        	String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
        	
            dto.setOfile(originalFileName);  // 원래 파일 이름
            dto.setSfile(savedFileName);  // 서버에 저장된 파일 이름

            // 기존 파일 삭제
            FileUtil.deleteFile(request, "/uploads", prevSfile);
        }
        else {
            // 첨부 파일이 없으면 기존 이름 유지
            dto.setOfile(prevOfile);
            dto.setSfile(prevSfile);
        }

        // DAO를 통해 DB에 게시 내용 저장

        ArticlesDAO dao = new ArticlesDAO();
        int articleId = dao.updatePost(dto);
        // 해시태그 문자열은 "hashtags" 파라미터로 전달 (예: "#여행, #맛집, #공부")
        String hashtagStr = request.getParameter("hashtags");
        // 게시글 번호와 해시태그 문자열을 넘겨 해시태그 처리
        if (articleId > 0) {
            dao.processHashtags(articleId, hashtagStr);
            response.sendRedirect("../articles/view.do?idx=" + idx);
        } else {
            System.out.println("기사 수정 실패");
        }
        dao.close();
    }

}
