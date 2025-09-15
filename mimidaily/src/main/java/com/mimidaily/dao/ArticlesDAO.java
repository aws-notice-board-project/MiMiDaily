package com.mimidaily.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.ArticlesDTO;

public class ArticlesDAO extends DBConnPool {
    public ArticlesDAO() {
        super();
    }
    
    // 실시간 관심기사
 	public List<ArticlesDTO> viewestList(){
 		List<ArticlesDTO> viewest=new ArrayList<ArticlesDTO>();
 		String query="SELECT * FROM articles ORDER BY visitcnt DESC LIMIT 4";
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
                loadThumbnail(dto);
                viewest.add(dto);
 			}
 		}catch(Exception e) {e.printStackTrace();}
 		return viewest;
 	}

 	// 기사 갯수
    public int selectCount(Map<String, Object> map) {
        int totalCount = 0;
        String query = "SELECT COUNT(*) FROM articles";
        boolean whereAdded = false;
        if (map.get("category") != null) {
            query += " WHERE category = " + map.get("category");
            whereAdded = true;
        }
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
            if(rs.next())
                totalCount = rs.getInt(1);
        } catch (Exception e) {
            System.out.println("기사 카운트 중 예외 발생");
            e.printStackTrace();
        }
        return totalCount;
    }
    

    // 목록 반환(List)
    public List<ArticlesDTO> selectListPage(Map<String,Object> map){
    	List<ArticlesDTO> article=new ArrayList<ArticlesDTO>();
    	String query="SELECT * FROM articles";
		List<String> conds = new ArrayList<>();
		if (map.get("category") != null) {
	        conds.add("category = " + map.get("category"));
	    }
	    if (map.get("searchWord") != null) {
	        conds.add(map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%'");
	    }
	    if (!conds.isEmpty()) {
	        query += " WHERE " + String.join(" AND ", conds);
	    }
	    query += " ORDER BY idx DESC LIMIT ?, ?";
		try {
			psmt=con.prepareStatement(query);
			psmt.setInt(1, (Integer) map.get("start") - 1); // MySQL은 0-base
			psmt.setInt(2, (Integer) map.get("end") - (Integer) map.get("start") + 1);
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
	            loadThumbnail(dto);
				article.add(dto);
			}
		}catch(Exception e) {
			System.out.println("기사 목록 조회 중 예외 발생");
			e.printStackTrace();
		}
		return article;
	}
    
 	// 게시글 작성
    public int insertWrite(ArticlesDTO dto) {
    	  int articleId = 0;
          try {
              if (dto.getOfile() == null || dto.getOfile().trim().equals("")) {
                  String query = "INSERT INTO articles (title, content, category, created_at, visitcnt, members_id, thumbnails_idx) VALUES (?, ?, ?, ?, 0, ?, NULL)";
                  psmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                  psmt.setString(1, dto.getTitle());
                  psmt.setString(2, dto.getContent());
                  psmt.setInt(3, dto.getCategory());
                  psmt.setTimestamp(4, dto.getCreated_at());
                  psmt.setString(5, dto.getMembers_id());
                  int result = psmt.executeUpdate();
                  if (result > 0) {
                      ResultSet rs = psmt.getGeneratedKeys();
                      if (rs.next()) {
                          articleId = rs.getInt(1);
                      }
                      rs.close();
                  }
              } else {
                  // 썸네일 먼저 추가
                  String thumbQuery = "INSERT INTO thumbnails (ofile, sfile, file_path, file_size, file_type, created_at) VALUES (?, ?, ?, ?, ?, ?)";
                  PreparedStatement thumbStmt = con.prepareStatement(thumbQuery, Statement.RETURN_GENERATED_KEYS);
                  thumbStmt.setString(1, dto.getOfile());
                  thumbStmt.setString(2, dto.getSfile());
                  thumbStmt.setString(3, dto.getFile_path());
                  thumbStmt.setLong(4, dto.getFile_size());
                  thumbStmt.setString(5, dto.getFile_type());
                  thumbStmt.setTimestamp(6, dto.getCreated_at());
                  thumbStmt.executeUpdate();
                  int thumbIdx = 0;
                  ResultSet thumbRs = thumbStmt.getGeneratedKeys();
                  if (thumbRs.next()) thumbIdx = thumbRs.getInt(1);
                  thumbRs.close();
                  thumbStmt.close();
                  String query = "INSERT INTO articles (title, content, category, created_at, visitcnt, members_id, thumbnails_idx) VALUES (?, ?, ?, ?, 0, ?, ?)";
                  psmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                  psmt.setString(1, dto.getTitle());
                  psmt.setString(2, dto.getContent());
                  psmt.setInt(3, dto.getCategory());
                  psmt.setTimestamp(4, dto.getCreated_at());
                  psmt.setString(5, dto.getMembers_id());
                  psmt.setInt(6, thumbIdx);
                  int result = psmt.executeUpdate();
                  if (result > 0) {
                      ResultSet rs = psmt.getGeneratedKeys();
                      if (rs.next()) {
                          articleId = rs.getInt(1);
                      }
                      rs.close();
                  }
              }
          } catch (Exception e) {
              System.out.println("기사 입력 중 예외 발생");
              e.printStackTrace();
          }
          return articleId;
    }
    

    // 주어진 일련번호에 해당하는 기사 반환
    public ArticlesDTO selectView(String idx, String memberId) {
        ArticlesDTO dto = new ArticlesDTO();
        String query = "SELECT idx, title, content, category, created_at, visitcnt, members_id, thumbnails_idx FROM articles WHERE idx = ?";
        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, idx);
            rs = psmt.executeQuery();
            if (rs.next()) {
                dto.setIdx(rs.getInt(1));
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString(3));
                dto.setCategory(rs.getInt(4));
                dto.setCreated_at(rs.getTimestamp(5));
                dto.setVisitcnt(rs.getInt(6));
                dto.setMembers_id(rs.getString(7));
                dto.setThumbnails_idx(rs.getInt(8));
                dto.setHashtags(hashtagsByArticle(rs.getInt(1)));
                loadThumbnail(dto);
	            dto.setLikes(getLikeCount(rs.getInt(1)));
	            dto.setIs_liked(isLiked(memberId, rs.getInt(1)));
            }
        } catch (Exception e) {
            System.out.println("기사 상세보기 중 예외 발생");
            e.printStackTrace();
        }
        return dto;
    }

    // 조회수 증가
    public void updateVisitCount(String idx) {
        String query = "UPDATE articles SET visitcnt=visitcnt+1 WHERE idx=?";
        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, idx);
            psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("기사 조회수 증가 중 예외 발생");
            e.printStackTrace();
        }
    }

    public int deletePost(String articleIdx) {
        int result = 0;
        Integer thumbnailIdx = null;
        try {
            String selectThumbQuery = "SELECT thumbnails_idx FROM articles WHERE idx = ?";
            psmt = con.prepareStatement(selectThumbQuery);
            psmt.setString(1, articleIdx);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                thumbnailIdx = rs.getInt("thumbnails_idx");
            }
            rs.close();
            psmt.close();
            String deleteHashtagsArticlesQuery = "DELETE FROM hashtags_articles WHERE articles_idx = ?";
            psmt = con.prepareStatement(deleteHashtagsArticlesQuery);
            psmt.setString(1, articleIdx);
            psmt.executeUpdate();
            psmt.close();
            String deleteLikesQuery = "DELETE FROM likes WHERE articles_idx = ?";
            psmt = con.prepareStatement(deleteLikesQuery);
            psmt.setString(1, articleIdx);
            psmt.executeUpdate();
            psmt.close();
            String deleteCommentsQuery = "DELETE FROM comments WHERE articles_idx = ?";
            psmt = con.prepareStatement(deleteCommentsQuery);
            psmt.setString(1, articleIdx);
            psmt.executeUpdate();
            psmt.close();
            String deleteArticleQuery = "DELETE FROM articles WHERE idx = ?";
            psmt = con.prepareStatement(deleteArticleQuery);
            psmt.setString(1, articleIdx);
            result = psmt.executeUpdate();
            psmt.close();
            if (thumbnailIdx != null && thumbnailIdx > 0) {
                String deleteThumbnailQuery = "DELETE FROM thumbnails WHERE idx = ?";
                psmt = con.prepareStatement(deleteThumbnailQuery);
                psmt.setInt(1, thumbnailIdx);
                psmt.executeUpdate();
                psmt.close();
            }
        } catch (Exception e) {
            System.out.println("기사 삭제 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    // 게시글 데이터 갱신(파일 업로드 지원)
    public int updatePost(ArticlesDTO dto, String idx, String thumb_idx) {
        int updatedArticleId = 0;
        try {
            if (dto.getOfile() == null || dto.getOfile().trim().equals("")) {
                String query = "UPDATE articles SET title = ?, content = ?, category = ?, created_at = ? WHERE idx = ?";
                psmt = con.prepareStatement(query);
                psmt.setString(1, dto.getTitle());
                psmt.setString(2, dto.getContent());
                psmt.setInt(3, dto.getCategory());
                psmt.setTimestamp(4, dto.getCreated_at());
                psmt.setString(5, idx);
                int result = psmt.executeUpdate();
                if (result > 0) {
                    updatedArticleId = Integer.parseInt(idx);
                }
            } else {
                if (thumb_idx != null && thumb_idx.trim().equals("0")) {
                    String insertThumbQuery =
                        "INSERT INTO thumbnails (ofile, sfile, file_path, file_size, file_type, created_at) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmtThumb = con.prepareStatement(insertThumbQuery, Statement.RETURN_GENERATED_KEYS);
                    pstmtThumb.setString(1, dto.getOfile());
                    pstmtThumb.setString(2, dto.getSfile());
                    pstmtThumb.setString(3, dto.getFile_path());
                    pstmtThumb.setLong(4, dto.getFile_size());
                    pstmtThumb.setString(5, dto.getFile_type());
                    pstmtThumb.setTimestamp(6, dto.getCreated_at());
                    int thumbResult = pstmtThumb.executeUpdate();
                    int newThumbId = 0;
                    if (thumbResult > 0) {
                        ResultSet rs = pstmtThumb.getGeneratedKeys();
                        if (rs.next()) {
                            newThumbId = rs.getInt(1);
                        }
                        rs.close();
                    }
                    pstmtThumb.close();
                    String updateArticleQuery =
                        "UPDATE articles SET title = ?, content = ?, category = ?, created_at = ?, thumbnails_idx = ? WHERE idx = ?";
                    PreparedStatement pstmtArticle = con.prepareStatement(updateArticleQuery);
                    pstmtArticle.setString(1, dto.getTitle());
                    pstmtArticle.setString(2, dto.getContent());
                    pstmtArticle.setInt(3, dto.getCategory());
                    pstmtArticle.setTimestamp(4, dto.getCreated_at());
                    pstmtArticle.setInt(5, newThumbId);
                    pstmtArticle.setString(6, idx);
                    int artResult = pstmtArticle.executeUpdate();
                    if (artResult > 0) {
                        updatedArticleId = Integer.parseInt(idx);
                    }
                    pstmtArticle.close();
                } else {
                   String updateThumbQuery =
                    "UPDATE thumbnails SET ofile = ?, sfile = ?, file_path = ?, file_size = ?, file_type = ?, created_at = ? WHERE idx = ?";
                    PreparedStatement pstmtThumb = con.prepareStatement(updateThumbQuery);
                    pstmtThumb.setString(1, dto.getOfile());
                    pstmtThumb.setString(2, dto.getSfile());
                    pstmtThumb.setString(3, dto.getFile_path());
                    pstmtThumb.setLong(4, dto.getFile_size());
                    pstmtThumb.setString(5, dto.getFile_type());
                    pstmtThumb.setTimestamp(6, dto.getCreated_at());
                    pstmtThumb.setString(7, thumb_idx);
                    pstmtThumb.executeUpdate();
                    pstmtThumb.close();
                    String updateArticleQuery =
                    "UPDATE articles SET title = ?, content = ?, category = ?, created_at = ?, thumbnails_idx = ? WHERE idx = ?";
                    PreparedStatement pstmtArticle = con.prepareStatement(updateArticleQuery);
                    pstmtArticle.setString(1, dto.getTitle());
                    pstmtArticle.setString(2, dto.getContent());
                    pstmtArticle.setInt(3, dto.getCategory());
                    pstmtArticle.setTimestamp(4, dto.getCreated_at());
                    pstmtArticle.setString(5, thumb_idx);
                    pstmtArticle.setString(6, idx);
                    int artResult = pstmtArticle.executeUpdate();
                    if (artResult > 0) {
                        updatedArticleId = Integer.parseInt(idx);
                    }
                    pstmtArticle.close();
                }
            }
        } catch (Exception e) {
            System.out.println("기사 수정 중 예외 발생");
            e.printStackTrace();
        }
        return updatedArticleId;
    }
    
    // 해시태그 삽입
    public void processHashtags(int articleId, String hashtagStr) {
        if (hashtagStr == null || hashtagStr.trim().isEmpty()) return;
        String[] tags = hashtagStr.split("\\s");
        for (String tag : tags) {
            tag = tag.trim();
            if (tag.startsWith("#")) {
                tag = tag.substring(1);
            }
            if (tag.isEmpty()) continue;
            int hashtagId = getHashtagId(tag);
            if (hashtagId == 0) {
                hashtagId = insertHashtag(tag);
            }
            insertHashtagArticleRelation(hashtagId, articleId);
        }
    }
    
    // 해시태그 존재 확인
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
    
    // 새로운 해시태그 삽입
    private int insertHashtag(String tag) {
        int id = 0;
        String sql = "INSERT INTO hashtags(name) VALUES(?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
    
    // 해시태그와 게시글 관계 삽입
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

    // 현재 기사 해시태그 목록 조회
    private Set<String> getCurrentHashtags(int articleId) {
        Set<String> currentTags = new HashSet<>();
        String sql = "SELECT h.name FROM hashtags h JOIN hashtags_articles hr ON h.idx = hr.hashtags_idx WHERE hr.articles_idx = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, articleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                currentTags.add(rs.getString("name"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentTags;
    }
    
    // 기존 해시태그 관계 삭제
    private void deleteHashtagArticleRelation(int hashtagId, int articleId) {
        String sql = "DELETE FROM hashtags_articles WHERE hashtags_idx = ? AND articles_idx = ?";
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

    // 해시태그 목록 파싱
    private Set<String> parseHashtags(String hashtagStr) {
        Set<String> tags = new HashSet<>();
        if (hashtagStr == null || hashtagStr.trim().isEmpty()) return tags;
        String[] tokens = hashtagStr.split("\\s");
        for (String tag : tokens) {
            tag = tag.trim();
            if (tag.startsWith("#")) {
                tag = tag.substring(1);
            }
            if (!tag.isEmpty()) {
                tags.add(tag);
            }
        }
        return tags;
    }

    // 기사 해시태그 업데이트
    public void updateArticleHashtagsSelective(int articleId, String hashtagStr) {
        Set<String> newTags = parseHashtags(hashtagStr);
        Set<String> currentTags = getCurrentHashtags(articleId);
        Set<String> tagsToDelete = new HashSet<>(currentTags);
        tagsToDelete.removeAll(newTags);
        Set<String> tagsToAdd = new HashSet<>(newTags);
        tagsToAdd.removeAll(currentTags);
        for (String tag : tagsToDelete) {
            int hashtagId = getHashtagId(tag);
            if (hashtagId != 0) {
            	deleteHashtagArticleRelation(hashtagId, articleId);
            }
        }
        for (String tag : tagsToAdd) {
            int hashtagId = getHashtagId(tag);
            if (hashtagId == 0) {
                hashtagId = insertHashtag(tag);
            }
            insertHashtagArticleRelation(hashtagId, articleId);
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
    
	// 상위 10개 기사 반환(top10)
	public List<ArticlesDTO> selectTopArticles() {
		List<ArticlesDTO> article=new ArrayList<ArticlesDTO>();
		String query=
				"SELECT a.*, l.likecnt " +
				"  FROM articles a " +
				"  LEFT JOIN (SELECT articles_idx, COUNT(members_id) AS likecnt FROM likes GROUP BY articles_idx) l " +
				"  ON a.idx = l.articles_idx " +
				"  ORDER BY likecnt DESC, visitcnt DESC LIMIT 10";
		try {
			psmt=con.prepareStatement(query);
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
                dto.setLikes(rs.getInt("likecnt"));
                dto.setHashtags(hashtagsByArticle(rs.getInt(1)));
                loadThumbnail(dto);
				article.add(dto);
			}
		}catch(Exception e) {
			System.out.println("기사 조회 중 예외 발생");
			e.printStackTrace();
		}
		return article;
	}
	
	// 기사 번호로 해당되는 해시태그들 반환
	public List<String> hashtagsByArticle(int articleIdx) {
		List<String> hashtags=new ArrayList<String>();
		String hashtagQuery = "SELECT h.name FROM hashtags_articles ha JOIN hashtags h ON ha.hashtags_idx = h.idx WHERE ha.articles_idx = ?";
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
	
	// 좋아요 갯수
	public int getLikeCount(int idx) {
	    String query = "SELECT COUNT(*) FROM likes WHERE articles_idx = ?";
	    try {
	    	psmt=con.prepareStatement(query);
	        psmt.setInt(1, idx);
	        ResultSet rs = psmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	// 좋아요 추가
	public boolean addLike(String members_id, int articleIdx) {
	    String query = "INSERT INTO likes (members_id, articles_idx) VALUES (?, ?)";
	    try {
	    	psmt=con.prepareStatement(query);
	        psmt.setString(1, members_id);
	        psmt.setInt(2, articleIdx);
	        return psmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// 좋아요 제거
	public boolean removeLike(String members_id, int articleIdx) {
	    String query = "DELETE FROM likes WHERE members_id = ? AND articles_idx = ?";
	    try {
	    	psmt=con.prepareStatement(query);
	        psmt.setString(1, members_id);
	        psmt.setInt(2, articleIdx);
	        return psmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// 좋아요 여부
	public boolean isLiked(String members_id, int idx) {
	    String query = "SELECT COUNT(*) FROM likes WHERE members_id = ? AND articles_idx = ?";
	    try {
	    	psmt=con.prepareStatement(query);
	        psmt.setString(1, members_id);
	        psmt.setInt(2, idx);
	        ResultSet rs = psmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
}