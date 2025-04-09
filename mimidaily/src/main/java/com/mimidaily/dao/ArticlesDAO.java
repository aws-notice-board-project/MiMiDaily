package com.mimidaily.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.ArticlesDTO;

public class ArticlesDAO extends DBConnPool {
    public ArticlesDAO() {
        super();
    }
    
 // 실시간 관심기사 =============================================================
 	public List<ArticlesDTO> viewestList(){
 		List<ArticlesDTO> viewest=new ArrayList<ArticlesDTO>();

 		// 조회수 많은 게시물 순으로 내림차순
 		String query=""
 					+"select * from (select * from articles order by visitcnt desc)"
 					+" where rownum <= 4";
 		
 		try {
 			stmt=con.createStatement();
 			rs=stmt.executeQuery(query);
 			while(rs.next()) {
 				ArticlesDTO dto=new ArticlesDTO();
 				dto.setIdx(rs.getInt(1));
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString(3));
                dto.setCategory(rs.getInt(4));
                dto.setCreated_at(rs.getTimestamp(5));
                dto.setVisitcnt(rs.getInt(6));
                dto.setMembers_id(rs.getString(7));
                dto.setThumbnails_idx(rs.getInt(8));
                viewest.add(dto);
 			}
 		}catch(Exception e) {e.getStackTrace();}
 		return viewest;
 	}

 	// 게시물 갯수 =============================================================
    public int selectCount(Map<String, Object> map) {
        int totalCount = 0;
        String query = "SELECT COUNT(*) FROM articles";
        boolean whereAdded = false;
        
        // category 조건 추가 (예: MustEat 페이지에서 category가 2인 글만 보이도록)
        if (map.get("category") != null) {
            query += " WHERE category = " + map.get("category");
            whereAdded = true;
        }
        
        // 검색어 조건 추가
        if (map.get("searchWord") != null) {
            if (whereAdded) {
                query += " AND " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%'";
            } else {
                query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%'";
            }
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
    

    // 목록 반환(List) =============================================================
    public List<ArticlesDTO> selectListPage(Map<String,Object> map){
    	List<ArticlesDTO> article=new ArrayList<ArticlesDTO>();
    	String query=""
    	+"select * from ("
    	+"	select Tb.*, rownum rNum from (" // Tb의 모든 칼럼, rNum: rownum의 별칭
    	+"		select * from articles";


		// 카테고리 조건
	    if (map.get("category") != null) { // (1:여행 2:맛집)
	        query += " where category = " + map.get("category");
	    }
		
	    // 검색 조건
	    if (map.get("searchWord") != null) {
	        // 카테고리 조건이 있을 경우 AND 추가
	        if (map.get("category") != null) {
	            query += " and " + map.get("searchField") + " like '%" + map.get("searchWord") + "%'";
	        } else {
	            query += " where " + map.get("searchField") + " like '%" + map.get("searchWord") + "%'";
	        }
	    }
	    
		query+="		order by idx desc"
				+"	) Tb"
				+" )"
				+" where rNum between ? and ?";
		
		try {
			psmt=con.prepareStatement(query); // 동적쿼리
			psmt.setInt(1, (Integer) map.get("start"));
			psmt.setInt(2, (Integer) map.get("end"));
			rs=psmt.executeQuery();
			while(rs.next()) {
				ArticlesDTO dto=new ArticlesDTO();
				dto.setIdx(rs.getInt(1)); // rs.get...(rs의 column row) 또는 명확한 컬럼명 넣어도 됨
	            dto.setTitle(rs.getString(2));
	            dto.setContent(rs.getString(3));
	            dto.setCategory(rs.getInt(4));
	            dto.setCreated_at(rs.getTimestamp(5));
	            dto.setVisitcnt(rs.getInt(6));
	            dto.setMembers_id(rs.getString(7));
	            dto.setThumbnails_idx(rs.getInt(8));
	             
	            // 썸네일 정보 로드
	            loadThumbnail(dto);
	             
				article.add(dto);
			}
		}catch(Exception e) {
			System.out.println("게시물 목록 조회 중 예외 발생");
			e.getStackTrace();
		}
		return article;
	}
    
 	// 게시글 작성 =============================================================
    public int insertWrite(ArticlesDTO dto) {
    	  int articleId = 0;
          try {
              if (dto.getOfile() == null || dto.getOfile().trim().equals("")) {
                  // 파일 업로드가 없는 경우: articles 테이블에만 INSERT
                  String query = "INSERT INTO articles (idx, title, content, category, created_at, visitcnt, members_id, thumnails_idx) " +
                                 "VALUES (articles_seq.NEXTVAL, ?, ?, ?, ?, 0, ?, NULL)";
                  psmt = con.prepareStatement(query);
                  psmt.setString(1, dto.getTitle());
                  psmt.setString(2, dto.getContent());
                  psmt.setInt(3, dto.getCategory());
                  psmt.setTimestamp(4, dto.getCreated_at());
                  psmt.setString(5, dto.getMembers_id());
                  int result = psmt.executeUpdate();
                  if (result > 0) {
                      stmt = con.createStatement();
                      rs = stmt.executeQuery("SELECT articles_seq.CURRVAL FROM dual");
                      if (rs.next()) {
                          articleId = rs.getInt(1);
                      }
                  }
              } else {
                  // 파일 업로드가 있는 경우: PL/SQL 블록을 사용하여 thumbnails와 articles 모두 INSERT
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
                      "  ? := v_a_seq; " +
                      "END;";
                  CallableStatement cstmt = con.prepareCall(query);
                  // thumbnails INSERT 파라미터
                  cstmt.setString(1, dto.getOfile());
                  cstmt.setString(2, dto.getSfile());
                  cstmt.setString(3, dto.getFile_path());
                  cstmt.setLong(4, dto.getFile_size());
                  cstmt.setString(5, dto.getFile_type());
                  cstmt.setTimestamp(6, dto.getCreated_at());
                  // articles INSERT 파라미터
                  cstmt.setString(7, dto.getTitle());
                  cstmt.setString(8, dto.getContent());
                  cstmt.setInt(9, dto.getCategory());
                  cstmt.setTimestamp(10, dto.getCreated_at());
                  cstmt.setString(11, dto.getMembers_id());
                  cstmt.registerOutParameter(12, java.sql.Types.INTEGER);
                  cstmt.execute();
                  articleId = cstmt.getInt(12);
              }
          } catch (Exception e) {
              System.out.println("게시물 입력 중 예외 발생");
              e.printStackTrace();
          }
          return articleId;
    }
    

    // 주어진 일련번호에 해당하는 게시물을 DTO에 담아 반환합니다.
    public ArticlesDTO selectView(String idx) {
        ArticlesDTO dto = new ArticlesDTO(); // DTO 객체 생성
        String query = ""
                + "SELECT a.idx, a.title, a.content, a.category, a.created_at, a.visitcnt, a.members_id, a.thumnails_idx, "
                + "       (SELECT COUNT(*) FROM likes l WHERE l.articles_idx = a.idx) AS like_count, "
                + "       (SELECT COUNT(*) FROM likes l WHERE l.articles_idx = a.idx AND l.members_id = ?) AS is_liked "
                + "FROM articles a "
                + "WHERE a.idx = ?";
        
        try {
            psmt = con.prepareStatement(query); // 쿼리문 준비
            psmt.setString(1, idx); // 특정 멤버 ID 설정
            psmt.setString(2, idx); // 게시글 ID 설정
            rs = psmt.executeQuery(); // 쿼리문 실행
            
            // ResultSetMetaData를 사용하여 컬럼 정보 출력
			/*
			 * ResultSetMetaData metaData = rs.getMetaData(); int columnCount =
			 * metaData.getColumnCount(); for (int i = 1; i <= columnCount; i++) {
			 * System.out.println("Column " + i + ": " + metaData.getColumnName(i)); }
			 */

            if (rs.next()) { // 결과를 DTO 객체에 저장
                dto.setIdx(rs.getInt(1));
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString(3));
                dto.setCategory(rs.getInt(4));
                dto.setCreated_at(rs.getTimestamp(5));
                dto.setVisitcnt(rs.getInt(6));
                dto.setMembers_id(rs.getString(7));
                dto.setThumbnails_idx(rs.getInt(8));
                
                // 썸네일 정보 로드
	            loadThumbnail(dto);
                
                // 좋아요 수와 현재 사용자의 좋아요 여부 가져오기
                dto.setLikes(rs.getInt("like_count")); // 좋아요 수
                dto.setIs_liked(rs.getInt("is_liked") > 0); // 현재 사용자가 좋아요를 눌렀는지 여부 (boolean)
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
        int updatedArticleId = 0;
        try {
            if (dto.getOfile() == null || dto.getOfile().trim().equals("")) {
                // 파일 업로드 없이 단순히 게시글의 제목, 내용, 카테고리 등만 수정하는 경우:
                String query = "UPDATE articles " +
                               "SET title = ?, content = ?, category = ?, created_at = ? " +
                               "WHERE idx = ?";
                psmt = con.prepareStatement(query);
                psmt.setString(1, dto.getTitle());
                psmt.setString(2, dto.getContent());
                psmt.setInt(3, dto.getCategory());
                psmt.setTimestamp(4, dto.getCreated_at());
                psmt.setInt(5, dto.getIdx());  // 수정할 게시글의 번호
                int result = psmt.executeUpdate();
                if (result > 0) {
                    updatedArticleId = dto.getIdx();
                }
            } else {
                // 파일 업로드가 있는 경우: PL/SQL 블록을 사용해서 thumbnails와 articles 모두 업데이트
                String query = 
                    "BEGIN " +
                    "  UPDATE thumbnails " +
                    "  SET ofile = ?, " +          // 새 원본파일명
                    "      sfile = ?, " +          // 새 저장파일명
                    "      file_path = ?, " +      // 새 파일 경로
                    "      file_size = ?, " +      // 새 파일 크기
                    "      file_type = ?, " +      // 새 파일 타입
                    "      created_at = ? " +      // 수정 시간 (또는 업데이트 시간)
                    "  WHERE idx = ?; " +          // 수정할 썸네일 번호  
                    "  UPDATE articles " +
                    "  SET title = ?, " +          // 새 제목
                    "      content = ?, " +        // 새 내용
                    "      category = ?, " +       // 새 카테고리
                    "      created_at = ?, " +     // 수정 시간
                    "      thumnails_idx = ? " +   // 기존 또는 변경된 썸네일 번호
                    "  WHERE idx = ?; " +          // 수정할 게시글 번호
                    "END;";
                CallableStatement cstmt = con.prepareCall(query);
                
                // 1. thumbnails 업데이트 파라미터 (순서대로)
                cstmt.setString(1, dto.getOfile());
                cstmt.setString(2, dto.getSfile());
                cstmt.setString(3, dto.getFile_path());
                cstmt.setLong(4, dto.getFile_size());
                cstmt.setString(5, dto.getFile_type());
                cstmt.setTimestamp(6, dto.getCreated_at());
                cstmt.setInt(7, dto.getThumbnails_idx());  // 기존 썸네일 idx가 dto에 있어야 함
                
                // 2. articles 업데이트 파라미터 (순서대로)
                cstmt.setString(8, dto.getTitle());
                cstmt.setString(9, dto.getContent());
                cstmt.setInt(10, dto.getCategory());
                cstmt.setTimestamp(11, dto.getCreated_at());
                cstmt.setInt(12, dto.getThumbnails_idx());
                cstmt.setInt(13, dto.getIdx());  // 수정할 게시글의 번호
                
                cstmt.execute();
                updatedArticleId = dto.getIdx();
            }
        } catch (Exception e) {
            System.out.println("게시물 수정 중 예외 발생");
            e.printStackTrace();
        }
        return updatedArticleId;
    }
    
    // 해시태그 처리: 해시태그 문자열을 받아 파싱 후 해시태그 테이블과 교차테이블에 삽입
    public void processHashtags(int articleId, String hashtagStr) {
        if (hashtagStr == null || hashtagStr.trim().isEmpty()) return;
        String[] tags = hashtagStr.split("\\s");
        for (String tag : tags) {
            tag = tag.trim();
            if (tag.startsWith("#")) {
                tag = tag.substring(1);
            }
            // 불필요한 기호 및 공백 제거
            //tag = tag.replaceAll("[,\\s]+", "");
            if (tag.isEmpty()) continue;
            int hashtagId = getHashtagId(tag);
            if (hashtagId == 0) {
                hashtagId = insertHashtag(tag);
            }
            insertHashtagArticleRelation(hashtagId, articleId);
        }
    }
    
    // 해시태그 존재 확인. 존재하면 해당 idx 반환, 없으면 0 반환
    private int getHashtagId(String tag) {
        int id = 0;
        String sql = "SELECT idx FROM hashtags WHERE name = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, tag);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("idx");
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    
    // 새로운 해시태그 삽입 후 생성된 idx 반환 (시퀀스 hashtags_seq가 있어야 함)
    private int insertHashtag(String tag) {
        int id = 0;
        String sql = "INSERT INTO hashtags(idx, name) VALUES(hashtags_seq.nextval, ?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql, new String[] { "idx" });
            pstmt.setString(1, tag);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();
            }
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    
    // 해시태그와 게시글 관계를 교차 테이블에 삽입
    private void insertHashtagArticleRelation(int hashtagId, int articleId) {
        String sql = "INSERT INTO hashtags_articles(hashtags_idx, articles_idx) VALUES(?, ?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, hashtagId);
            pstmt.setInt(2, articleId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 썸네일 정보
    public void loadThumbnail(ArticlesDTO dto) {
        if (dto.getThumbnails_idx() != null) {
            String thumbnailQuery = "SELECT ofile, sfile, file_path, file_size, file_type FROM thumbnails WHERE idx = ?";
            try (PreparedStatement thumbnailPsmt = con.prepareStatement(thumbnailQuery)) {
                thumbnailPsmt.setInt(1, dto.getThumbnails_idx());
                ResultSet thumbnailRs = thumbnailPsmt.executeQuery();
                if (thumbnailRs.next()) {
                    dto.setOfile(thumbnailRs.getString("ofile"));
                    dto.setSfile(thumbnailRs.getString("sfile"));
                    dto.setFile_path(thumbnailRs.getString("file_path"));
                    dto.setFile_size(thumbnailRs.getLong("file_size"));
                    dto.setFile_type(thumbnailRs.getString("file_type"));
                }
            } catch (Exception e) {
                System.out.println("썸네일 조회 중 예외 발생");
                e.printStackTrace();
            }
        }
    }
    
	// 상위 10개 게시물 반환(top10)
	public List<ArticlesDTO> selectTopArticles() {
		List<ArticlesDTO> article=new ArrayList<ArticlesDTO>();
		String query=""
				+"select * "
				+"  from (select * "
				+"		  from articles a join (select articles_idx, count(members_id) likecnt "
				+"								from likes "
				+"								group by articles_idx) l "
				+"		  on a.idx=l.articles_idx "
				+"		  order by likecnt desc, visitcnt) "
				+" where rownum <= 10";
//		System.out.println(query);
		
		try {
			psmt=con.prepareStatement(query); // 동적쿼리
			rs=psmt.executeQuery();
			while(rs.next()) {
				ArticlesDTO dto=new ArticlesDTO();
				dto.setIdx(rs.getInt(1));
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString(3));
                dto.setCategory(rs.getInt(4));
                dto.setCreated_at(rs.getTimestamp(5));
                dto.setVisitcnt(rs.getInt(6));
                dto.setMembers_id(rs.getString(7));
                dto.setThumbnails_idx(rs.getInt(8));
                dto.setLikes(rs.getInt(10));
                dto.setHashtags(getHashtagsByArticle(rs.getInt(1)));  // 해시태그 목록 추가
				article.add(dto);
			}
		}catch(Exception e) {
			System.out.println("기사 조회 중 예외 발생");
			e.printStackTrace();
		}
		return article;
	}
	
	// 게시물 번호로 해당돠는 해시태그들 반환
	public List<String> getHashtagsByArticle(int articleIdx) {
		List<String> hashtags=new ArrayList<String>();
		String hashtagQuery = "SELECT h.name " +
							  "  FROM hashtags_articles ha JOIN hashtags h " +
							  "  ON ha.hashtags_idx = h.idx " +
							  " WHERE ha.articles_idx = ?";
	    try {
	    	PreparedStatement tagPstmt = con.prepareStatement(hashtagQuery);
            tagPstmt.setInt(1, articleIdx);
            ResultSet tagRs = tagPstmt.executeQuery();
	        while(tagRs.next()) {
                hashtags.add(tagRs.getString("name"));
	        }
	        tagRs.close();
	        tagPstmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return hashtags;
	}
}
