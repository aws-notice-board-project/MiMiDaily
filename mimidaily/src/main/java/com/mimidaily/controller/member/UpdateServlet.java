package com.mimidaily.controller.member;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;

import com.mimidaily.dao.ArticlesDAO;
import com.mimidaily.dao.MemberDAO;
import com.mimidaily.dto.ArticlesDTO;
import com.mimidaily.dto.MemberDTO;
import com.mimidaily.utils.FileUtil;

@WebServlet("/update.do")
@MultipartConfig(maxFileSize = 1024 * 1024 * 3, // 파일업로드할때 최대 사이즈
maxRequestSize = 1024 * 1024 * 10 // 여러개의 파일 업로드할때 총합 사이즈
)
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/member/update.jsp";
        String memberId = (String) request.getSession().getAttribute("loginUser");
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = dao.getMember(memberId);
		request.setAttribute("member", dto);
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 한글 깨짐을 방지
		String url = "/member/update.jsp";
        // 수정 전 파일 관련 사항들
		String profile_idx = request.getParameter("prevprofile_idx");
        String prevOfile = request.getParameter("prevOfile");
        String prevSfile = request.getParameter("prevSfile");
        String prevfile_path = request.getParameter("prevfile_path");
        String prevfile_size_gp = request.getParameter("prevfile_size");
        long prevfile_size = 0;
        prevfile_size = Long.parseLong(prevfile_size_gp);
        String prevfile_type = request.getParameter("prevfile_type");
        // 폼에서 입력한 회원 정보 얻어오기
        String id = request.getParameter("id");
        String pwd = request.getParameter("pw");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String tel = request.getParameter("tel");
        String birth = request.getParameter("birth");
        String gender = request.getParameter("gender");
		Boolean marketing = "1".equals(request.getParameter("marketing"));
		MemberDTO dto = new MemberDTO();
		dto.setId(id);
		dto.setPwd(pwd);
		dto.setName(name);
		dto.setEmail(email);
		dto.setTel(tel);
		dto.setBirth(birth);
		dto.setGender(gender);
		dto.setMarketing(marketing);
		
		// 1. 파일 업로드 처리 =============================
        // 업로드 디렉터리의 물리적 경로 확인
        String saveDirectory = request.getServletContext().getRealPath("/uploads/profiles");

        // 파일 업로드
        Part filePart = null;
        try {
            filePart = request.getPart("ofile");
        } catch (IllegalStateException | FileSizeLimitExceededException ex) {
            request.setAttribute("errorMsg", "업로드 가능한 파일 크기는 최대 3MB입니다.");
            request.getRequestDispatcher("/main.do").forward(request, response);
            return;
        }
        
        String originalFileName = "";
        if (profile_idx != null) {
        	if (profile_idx.trim().isEmpty()) {
        		// profile_idx가 0인 경우의 처리 ---> 기존에 이미지가 없다
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
                        
                        dto.setOfile(originalFileName);  // 원래 파일 이름
                        dto.setSfile(savedFileName);  // 서버에 저장된 파일 이름
            
                        // 파일의 Part 객체에서 추가 정보를 추출합니다.
                        long fileSize = filePart.getSize(); // 파일 크기
                        String fileType = filePart.getContentType(); // 파일 유형(MIME 타입)
                        dto.setFile_size(fileSize);
                        dto.setFile_type(fileType);
                        dto.setFile_path("/uploads/profiles");
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
                        dto.setFile_path("/uploads/profiles");
                        
                        // 기존 파일 삭제
                        FileUtil.deleteFile(request, "/uploads/profiles", prevSfile);
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
        } else {
        	System.out.println("데이터 오류");
        }
		
        // DAO를 통해 DB에 게시 내용 저장
		MemberDAO dao = new MemberDAO();
		String updatedId = null;
		updatedId = dao.updateMember(dto, profile_idx);
		
		
		HttpSession session = request.getSession();
		if (updatedId == dto.getId()) {
			session.setAttribute("id", dto.getId());
			request.setAttribute("success_msg", "회원 정보 수정에 성공했습니다.");
			url = "main.do";
		} else {
			request.setAttribute("success_msg", "회원 정보 수정에 실패했습니다.");
		}
		dao.close();
		response.sendRedirect(url);
	}
}