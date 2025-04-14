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

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dto.ArticlesDTO;
import com.mimidaily.utils.FileUtil;

/**
 * Servlet implementation class WriteServlet
 */
@WebServlet("/articles/write.do")
@MultipartConfig(maxFileSize = 1024 * 1024 * 3, // 파일업로드할때 최대 사이즈
        maxRequestSize = 1024 * 1024 * 10 // 여러개의 파일 업로드할때 총합 사이즈
)
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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String referer = request.getHeader("Referer"); // 이전 페이지
		request.getSession().setAttribute("previousPage", referer); // 세션에 저장
        request.getRequestDispatcher("/articles/write.jsp").forward(request, response);
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

        // 파일 업로드
        Part filePart = null;
        try {
            filePart = request.getPart("ofile");
        } catch (IllegalStateException | FileSizeLimitExceededException ex) {
            request.setAttribute("errorMsg", "업로드 가능한 파일 크기는 최대 3MB입니다.");
            request.getRequestDispatcher("/main.do").forward(request, response);
        }
        String originalFileName = "";
        if (filePart != null && filePart.getSize() > 0) {
            try {
                originalFileName = FileUtil.uploadFile(request, saveDirectory);
            } catch (Exception e) {
                System.out.println("파일 업로드 오류입니다.");
                e.printStackTrace();
                originalFileName = "";
            }
        } else {
            System.out.println("파일이 선택되지 않았습니다.");
        }
          
        // 2. 파일 업로드 외 처리 =============================
        // 폼값을 DTO에 저장
        ArticlesDTO dto = new ArticlesDTO();
        dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        String categoryParam = request.getParameter("category");
        if (categoryParam != null && !categoryParam.isEmpty()) {
            try {
                dto.setCategory(Integer.parseInt(categoryParam));
            } catch (NumberFormatException e) {
                dto.setCategory(1);
            }
        } else {
            dto.setCategory(1);
        }

        String createdAtParam = request.getParameter("created_at");
        if (createdAtParam != null && !createdAtParam.isEmpty()) {
            dto.setCreated_at(Timestamp.valueOf(createdAtParam));
        } else {
            dto.setCreated_at(new Timestamp(System.currentTimeMillis()));
        }
        dto.setMembers_id(request.getParameter("members_id"));

        // 원본 파일명과 저장된 파일 이름 설정
        if (originalFileName != "") {
            // 파일명 변경 후 저장 (파일명 중복 방지를 위해)
            String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
            dto.setOfile(originalFileName); // 원래 파일 이름
            dto.setSfile(savedFileName); // 서버에 저장된 파일 이름

            // 파일의 Part 객체에서 추가 정보를 추출합니다.
            long fileSize = filePart.getSize(); // 파일 크기
            String fileType = filePart.getContentType(); // 파일 유형(MIME 타입)
            dto.setFile_size(fileSize);
            dto.setFile_type(fileType);
            dto.setFile_path("/uploads/");
        }

        // DAO를 통해 DB에 게시 내용 저장
        ArticlesDAO dao = new ArticlesDAO();
        int articleId = dao.insertWrite(dto);
        // 해시태그 문자열은 "hashtags" 파라미터로 전달 (예: "#여행, #맛집, #공부")
        String hashtagStr = request.getParameter("hashtags");
        // 게시글 번호와 해시태그 문자열을 넘겨 해시태그 처리
        
        // 글 작성 후 페이지 이동 처리
        String url = "";
        String previousPage = (String) request.getSession().getAttribute("previousPage");
        String lastPath = null;
        if (previousPage != null) {
        	String[] parts = previousPage.split("/");
        	lastPath="/"+parts[parts.length - 1];
        }
        if (articleId > 0) {
        	dao.processHashtags(articleId, hashtagStr);
        	
        	if(lastPath != null && lastPath.equals("/musteat.do")){
        		url = "/articles/musteat.do";
        	}else if(lastPath != null && lastPath.equals("/travel.do")) {
        		url = "/articles/travel.do";
        	}else if(lastPath != null && lastPath.equals("/newest.do")) {
        		url = "/articles/newest.do";
        	}else {url = "/newest.do";}
        	
        	response.sendRedirect(url); //주소변경
        } else {
        	System.out.println("글쓰기 실패");
        }
        dao.close();
        
    }

}