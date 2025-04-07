package com.mimidaily.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.ArticlesDTO;

public class ArticlesEDAO extends DBConnPool {
    public ArticlesEDAO() {
        super();
    }

    // 검색 조건에 맞는 게시물의 개수를 반환합니다.
    public int selectCount(Map<String, Object> map) {
        int totalCount = 0;
        String query = "SELECT COUNT(*) FROM articles";
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " "
                    + " LIKE '%" + map.get("searchWord") + "%'";
        }
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            rs.next();
            totalCount = rs.getInt(1);
        } catch (Exception e) {
            System.out.println("게시물 카운트 중 예외 발생");
            e.printStackTrace();
        }

        return totalCount;
    }

    // 검색 조건에 맞는 게시물 목록을 반환합니다(페이징 기능 지원).
    // public List<ArticlesDTO> selectListPage(Map<String, Object> map) {
    //     List<ArticlesDTO> board = new ArrayList<ArticlesDTO>();
    //     String query = "SELECT * FROM ( " +
    //             "  SELECT a.idx, a.title, a.content, a.category, a.created_at, a.visitcnt, a.members_id, a.thumnails_idx, " +
    //             "         t.sfile AS thumbnailSfile, t.file_path AS thumbnailPath, " +
    //             "         ROW_NUMBER() OVER (ORDER BY a.created_at DESC) AS rnum " +
    //             "  FROM articles a " +
    //             "  LEFT JOIN thumbnails t ON a.thumnails_idx = t.idx ";
	// 	 if (map.get("searchWord") != null) {
	// 	     query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%' ";
	// 	 }
	// 		 query += " ) WHERE rnum BETWEEN ? AND ?";
    //     try {
    //         psmt = con.prepareStatement(query);
    //         psmt.setString(1, map.get("start").toString());
    //         psmt.setString(2, map.get("end").toString());
    //         rs = psmt.executeQuery();
    //         while (rs.next()) {
    //             // Model객체에 게시글 데이터 저장
    //             ArticlesDTO dto = new ArticlesDTO();

    //             dto.setIdx(rs.getInt(1));
    //             dto.setTitle(rs.getString(2));
    //             dto.setContent(rs.getString(3));
    //             dto.setCategory(rs.getInt(4));
    //             dto.setCreated_at(rs.getTimestamp(5));
    //             dto.setVisitcnt(rs.getInt(6));
    //             dto.setMembers_id(rs.getString(7));
    //             dto.setThumnails_idx(rs.getInt(8));
    //             dto.setSfile(rs.getString(9));
    //             dto.setFile_path(rs.getString(10));

    //             board.add(dto);
    //         }
    //     } catch (Exception e) {
    //         System.out.println("게시물 조회 중 예외 발생");
    //         e.printStackTrace();
    //     }
    //     return board;
    // }
    public List<ArticlesDTO> selectListPage(Map<String, Object> map) {
        List<ArticlesDTO> board = new ArrayList<ArticlesDTO>();
        String query = "SELECT * FROM ( " +
                "  SELECT a.idx, a.title, a.content, a.category, a.created_at, a.visitcnt, a.members_id, a.thumnails_idx, " +
                "         t.sfile AS thumbnailSfile, t.file_path AS thumbnailPath, " +
                "         ROW_NUMBER() OVER (ORDER BY a.created_at DESC) AS rnum " +
                "  FROM articles a " +
                "  LEFT JOIN thumbnails t ON a.thumnails_idx = t.idx ";
        
        // 항상 a.category = 2 조건을 추가하고, 검색어가 있으면 추가 조건을 붙임
        if (map.get("searchWord") != null) {
            query += " WHERE a.category = 2 AND " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%' ";
        } else {
            query += " WHERE a.category = 2 ";
        }
        
        query += " ) WHERE rnum BETWEEN ? AND ?";
        
        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, map.get("start").toString());
            psmt.setString(2, map.get("end").toString());
            rs = psmt.executeQuery();
            while (rs.next()) {
                ArticlesDTO dto = new ArticlesDTO();
                dto.setIdx(rs.getInt(1));
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString(3));
                dto.setCategory(rs.getInt(4));
                dto.setCreated_at(rs.getTimestamp(5));
                dto.setVisitcnt(rs.getInt(6));
                dto.setMembers_id(rs.getString(7));
                dto.setThumnails_idx(rs.getInt(8));
                dto.setSfile(rs.getString(9));
                dto.setFile_path(rs.getString(10));
                board.add(dto);
            }
        } catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
        return board;
    }
    

    // 게시글 데이터를 받아 DB에 추가합니다(파일 업로드 지원).
    // public int insertWrite(ArticlesDTO dto) {
    //     int result = 0;
    //     try {
    //         String query = "INSERT INTO articles ( "
    //                + " idx, title, content, category, created_at, visitcnt, members_id, thumnails_idx) "
    //                + " VALUES ( "
    //                + " articles_seq.NEXTVAL,?,?,?,?,0,?, thumbnails_seq.NEXTVAL)";
    //         psmt = con.prepareStatement(query);
    //         psmt.setString(1, dto.getTitle());
    //         psmt.setString(2, dto.getContent());
    //         psmt.setInt(3, dto.getCategory());
    //         psmt.setTimestamp(4, dto.getCreated_at());
    //         psmt.setString(5, dto.getMembers_id());
    //         result = psmt.executeUpdate();
    //     } catch (Exception e) {
    //         System.out.println("게시물 입력 중 예외 발생");
    //         e.printStackTrace();
    //     }
    //     return result;
    // }

    


    
    public int insertWrite(ArticlesDTO dto) {
        int result = 0;
        try {
            String query = 
                "DECLARE " +
                "  v_t_seq NUMBER; " +
                "  v_a_seq NUMBER; " +
                "BEGIN " +
                "  SELECT thumbnails_seq.NEXTVAL, articles_seq.NEXTVAL INTO v_t_seq, v_a_seq FROM dual; " +
                "  INSERT INTO thumbnails (idx, ofile, sfile, file_path, file_size, file_type, created_at) " +
                "  VALUES (v_t_seq, ?, ?, ?, ?, ?, ?); " +
                "  INSERT INTO articles (idx, title, content, category, created_at, visitcnt, members_id, thumnails_idx) " +
                "  VALUES (v_a_seq, ?, ?, ?, ?, 0, ?, v_t_seq); " +
                "END;";
            
            psmt = con.prepareStatement(query);
            
            // thumbnails INSERT 파라미터 설정 (순서대로)
            psmt.setString(1, dto.getOfile());         // ofile
            psmt.setString(2, dto.getSfile());           // sfile
            psmt.setString(3, dto.getFile_path());       // file_path
            psmt.setLong(4, dto.getFile_size());         // file_size
            psmt.setString(5, dto.getFile_type());       // file_type
            psmt.setTimestamp(6, dto.getCreated_at());   // created_at
            
            // articles INSERT 파라미터 설정 (순서대로)
            psmt.setString(7, dto.getTitle());           // title
            psmt.setString(8, dto.getContent());         // content
            psmt.setInt(9, dto.getCategory());           // category
            psmt.setTimestamp(10, dto.getCreated_at());    // created_at
            psmt.setString(11, dto.getMembers_id());     // members_id
            
            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 입력 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    
    

    // 주어진 일련번호에 해당하는 게시물을 DTO에 담아 반환합니다.
    public ArticlesDTO selectView(String idx) {
        ArticlesDTO dto = new ArticlesDTO(); // DTO 객체 생성
        String query = "SELECT * FROM articles WHERE idx=?"; // 쿼리문 템플릿 준비
        try {
            psmt = con.prepareStatement(query); // 쿼리문 준비
            psmt.setString(1, idx); // 인파라미터 설정
            rs = psmt.executeQuery(); // 쿼리문 실행

            if (rs.next()) { // 결과를 DTO 객체에 저장
                dto.setIdx(rs.getInt(1));
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString(3));
                dto.setCategory(rs.getInt(4));
                dto.setCreated_at(rs.getTimestamp(5));
                dto.setVisitcnt(rs.getInt(6));
                dto.setMembers_id(rs.getString(7));
                dto.setThumnails_idx(rs.getInt(8));
            }
        } catch (Exception e) {
            System.out.println("게시물 상세보기 중 예외 발생");
            e.printStackTrace();
        }
        return dto; // 결과 반환
    }

    // 주어진 일련번호에 해당하는 게시물의 조회수를 1 증가시킵니다.
    public void updateVisitCount(String idx) {
        String query = "UPDATE articles SET "
                + " visitcnt=visitcnt+1 "
                + " WHERE idx=?";
        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, idx);
            psmt.executeQuery();
        } catch (Exception e) {
            System.out.println("게시물 조회수 증가 중 예외 발생");
            e.printStackTrace();
        }
    }

    // 다운로드 횟수를 1 증가시킵니다.
    // public void downCountPlus(String idx) {
    // String sql = "UPDATE articles SET "
    // + " downcount=downcount+1 "
    // + " WHERE idx=? ";
    // try {
    // psmt = con.prepareStatement(sql);
    // psmt.setString(1, idx);
    // psmt.executeUpdate();
    // } catch (Exception e) {
    // }
    // }

    // 입력한 비밀번호가 지정한 일련번호의 게시물의 비밀번호와 일치하는지 확인합니다.
    // public boolean confirmPassword(String pass, String idx) {
    // boolean isCorr = true;
    // try {
    // String sql = "SELECT COUNT(*) FROM articles WHERE pass=? AND idx=?";
    // psmt = con.prepareStatement(sql);
    // psmt.setString(1, pass);
    // psmt.setString(2, idx);
    // rs = psmt.executeQuery();
    // rs.next();
    // if (rs.getInt(1) == 0) {
    // isCorr = false;
    // }
    // } catch (Exception e) {
    // isCorr = false;
    // e.printStackTrace();
    // }
    // return isCorr;
    // }

    // 지정한 일련번호의 게시물을 삭제합니다.
    public int deletePost(String idx) {
        int result = 0;
        try {
            String query = "DELETE FROM articles WHERE idx=?";
            psmt = con.prepareStatement(query);
            psmt.setString(1, idx);
            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 삭제 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    // 게시글 데이터를 받아 DB에 저장되어 있던 내용을 갱신합니다(파일 업로드 지원).
    public int updatePost(ArticlesDTO dto) {
        int result = 0;
        try {
            // 쿼리문 템플릿 준비
            String query = "UPDATE articles"
                    + " SET title=?, name=?, content=?, ofile=?, sfile=? "
                    + " WHERE idx=? and pass=?";

            // 쿼리문 준비
            psmt = con.prepareStatement(query);
            psmt.setInt(1, dto.getIdx());
            psmt.setString(2, dto.getTitle());
            psmt.setString(3, dto.getContent());
            psmt.setInt(4, dto.getCategory());
            psmt.setTimestamp(5, dto.getCreated_at());
            psmt.setInt(6, dto.getVisitcnt());
            psmt.setString(7, dto.getMembers_id());
            psmt.setInt(8, dto.getThumnails_idx());

            // 쿼리문 실행
            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 수정 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

}
