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
@WebServlet("/articles/edit.do")
@MultipartConfig(maxFileSize = 1024 * 1024 * 3, // 파일업로드할때 최대 사이즈
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
        String memberId = (String) request.getSession().getAttribute("loginUser");
		String idx = request.getParameter("idx");
		ArticlesDAO dao = new ArticlesDAO();
		ArticlesDTO dto = dao.selectView(idx, memberId);
		request.setAttribute("dto", dto);
		String referer = request.getHeader("Referer"); // 이전 페이지
		request.getSession().setAttribute("previousPage", referer); // 세션에 저장
		request.getRequestDispatcher("/articles/edit.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 수정 내용을 매개변수에서 얻어옴 - jsp hidden
		String idx = request.getParameter("idx");
		String thumb_idx = request.getParameter("prevthumbnails_idx");
		String members_id = request.getParameter("members_id");
        String prevOfile = request.getParameter("prevOfile");
        String prevSfile = request.getParameter("prevSfile");
        String prevfile_path = request.getParameter("prevfile_path");
        String prevfile_size_gp = request.getParameter("prevfile_size");
        long prevfile_size = 0;
        prevfile_size = Long.parseLong(prevfile_size_gp);
        String prevfile_type = request.getParameter("prevfile_type");
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
        
        if (thumb_idx != null && !thumb_idx.trim().isEmpty()) {
        	int thumbnailId = Integer.parseInt(thumb_idx.trim());
        	if (thumbnailId == 0) {
        		// thumb_idx가 0인 경우의 처리 ---> 기존에 이미지가 없다
                if (filePart != null && filePart.getSize() > 0) {
                    //---> 새로운 이미지 삽입
                    try {
                        originalFileName = FileUtil.uploadFile(request, saveDirectory);
                    } catch (Exception e) {
                        System.out.println("파일 업로드 오류입니다.");
                        e.printStackTrace();
                        originalFileName = "";
                    }
                    if (originalFileName != "") { 
                        String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
                        
                        dto.setOfile(originalFileName);  // 원본 파일 이름
                        dto.setSfile(savedFileName);  // 서버에 저장된 파일 이름
            
                        // 파일의 Part 객체에서 추가 정보를 추출합니다.
                        long fileSize = filePart.getSize(); // 파일 크기
                        String fileType = filePart.getContentType(); // 파일 유형(MIME 타입)
                        dto.setFile_size(fileSize);
                        dto.setFile_type(fileType);
                        dto.setFile_path("/uploads/");
                    }
                } else {
                    //---> 이미지 그대로 없음
                    System.out.println("이미지 추가 없음");
                } 
        	} else {
        		// thumb_idx가 0이 아닌 경우의 처리 ---> 기존에 이미지가 있다
                if (filePart != null && filePart.getSize() > 0) {
                    //---> 기존에 이미지 수정
                    try {
                        originalFileName = FileUtil.uploadFile(request, saveDirectory);
                    } catch (Exception e) {
                        System.out.println("파일 업로드 오류입니다.");
                        e.printStackTrace();
                        originalFileName = "";
                    }
                    if (originalFileName != "") { 
                        String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
                        
                        dto.setOfile(originalFileName);  // 원래 파일 이름
                        dto.setSfile(savedFileName);  // 서버에 저장된 파일 이름
            
                        // 파일의 Part 객체에서 추가 정보를 추출합니다.
                        long fileSize = filePart.getSize(); // 파일 크기
                        String fileType = filePart.getContentType(); // 파일 유형(MIME 타입)
                        dto.setFile_size(fileSize);
                        dto.setFile_type(fileType);
                        dto.setFile_path("/uploads/");
                        
                        // 기존 파일 삭제
                        FileUtil.deleteFile(request, "/uploads", prevSfile);
                    }
                } else {
                    //---> 기존에 이미지 그대로
                    dto.setOfile(prevOfile);
                    dto.setSfile(prevSfile);
                    dto.setFile_size(prevfile_size);
                    dto.setFile_type(prevfile_type);
                    dto.setFile_path(prevfile_path);
                }
        	}
        }
       
        // DAO를 통해 DB에 게시 내용 저장
        ArticlesDAO dao = new ArticlesDAO();
        int articleId = 0;
        articleId = dao.updatePost(dto, idx, thumb_idx);
        
        // 글 작성 후 페이지 이동 처리
        String previousPage = (String) request.getSession().getAttribute("previousPage");
        String lastPath = null;
        if (previousPage != null) {
            String[] redirectSplit = previousPage.split("redirectURL=");
            if (redirectSplit.length > 1) {
                String lastRedirect = redirectSplit[redirectSplit.length - 1];
                int ampIndex = lastRedirect.indexOf("&");
                lastPath = (ampIndex > -1) 
                    ? lastRedirect.substring(0, ampIndex) 
                    : lastRedirect;
            } else {
                String[] parts = previousPage.split("/");
                lastPath = parts[parts.length - 1];
            }
        }
        // 해시태그 문자열은 "hashtags" 파라미터로 전달 (예: "#여행, #맛집, #공부")
        String hashtagStr = request.getParameter("hashtags");
        // 게시글 번호와 해시태그 문자열을 넘겨 해시태그 처리
        if (articleId > 0) {
            dao.updateArticleHashtagsSelective(articleId, hashtagStr);
            response.sendRedirect("../articles/view.do?idx=" + idx+"&redirectURL="+lastPath);
        } else {
            System.out.println("기사 수정 실패");
        }
        dao.close();
    }

}
