package com.mimidaily.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.ArticlesDTO;
import com.mimidaily.dto.MemberDTO;
import com.mimidaily.dto.MemberInfoDTO;

//public class MemberDAO {
public class MemberDAO extends DBConnPool {
	public MemberDAO() {
		super();
	}

	// 로그인시 사용하는 메서드
	public int userCheck(String userid, String pwd) {
		int result = -1; // 기본값
		String query = "select pwd from members where id = ?"; // 쿼리문 템플릿 준비
		try {
			psmt = con.prepareStatement(query); // 쿼리문 준비
			psmt.setString(1, userid); // 인파라미터 설정
			rs = psmt.executeQuery(); // 쿼리문 실행

			if (rs.next()) {
				if (rs.getString("pwd") != null &&
						rs.getString("pwd").equals(pwd)) {
					result = 1; // 비밀번호일치
				} else {
					result = 0; // 비밀번호 불일치
				}
			} else {
				result = -1; // i id 없음
			}
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	// 역할을 가져오는 메서드
    public int getUserRole(String userid) {
        int role = 0;
        String query = "SELECT role FROM members WHERE id = ?";
        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, userid);
            rs = psmt.executeQuery();

            if (rs.next()) {
                role = rs.getInt("role"); // 역할 가져오기
            }
        } catch (Exception e) {
            System.out.println("역할 조회 중 예외 발생");
            e.printStackTrace();
        }
        return role; // 역할 반환
    }
    
    // 방문횟수 조회 및 증가
    public int incrementUserVisitCnt(String userid) {
        int visitcnt = 0;
        String selectSql = "SELECT visitcnt FROM members WHERE id = ?"; // 방문 횟수 조회 SQL
        String updateSql = "UPDATE members SET visitcnt = ? WHERE id = ?"; // 방문 횟수 증가 SQL

        try {
            // 방문 횟수 조회
            psmt = con.prepareStatement(selectSql);
            psmt.setString(1, userid);
            rs = psmt.executeQuery();

            if (rs.next()) {
                visitcnt = rs.getInt("visitcnt"); // 현재 방문 횟수 가져오기
            }

            // 방문 횟수 증가
            visitcnt++; // 1 증가

            // 업데이트 실행
            psmt = con.prepareStatement(updateSql);
            psmt.setInt(1, visitcnt);
            psmt.setString(2, userid);
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return visitcnt; // 증가된 방문 횟수 반환
    }
    
    // 멤버정보 가져오기(필요한 정보만)
    public MemberInfoDTO getMemberInfo(String memberId) {
        MemberInfoDTO memberInfo = null;
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM articles WHERE members_id = ?) AS article_count, "
                + "(SELECT COUNT(*) FROM comments WHERE members_id = ?) AS comment_count, "
                + "id, name, created_at "
                + "FROM members "
                + "WHERE id = ?";

        try {
        	psmt = con.prepareStatement(sql);
            psmt.setString(1, memberId);
            psmt.setString(2, memberId);
            psmt.setString(3, memberId);
            rs = psmt.executeQuery();

            if (rs.next()) {
                memberInfo = new MemberInfoDTO();
                memberInfo.setId(rs.getString("id"));
                memberInfo.setName(rs.getString("name"));
                memberInfo.setArticleCount(rs.getInt("article_count"));
                memberInfo.setCommentCount(rs.getInt("comment_count"));
                memberInfo.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return memberInfo;
    }
  
	//id 중복 확인
	public int confirmID(String userid) {
		int result = -1; // 중복아님?
		String sql = "select id from members where id=?";
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, userid);
			rs = psmt.executeQuery();
			if (rs.next()) {
				result = 1;// 사용 불가능
			} else {
				result = -1;// 사용 가능
			}
		} catch (Exception e) {
			System.out.println("id 중복 확인 중 오류 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	// 회원 등록
		public int insertMember(MemberDTO mDto) {
			int result = -1;
			String sql = "insert into members(id, pwd, name, email, tel, birth, gender, role, marketing, created_at) VALUES \r\n"
					+ "    (?, ?, ?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP)";
			try {
				psmt = con.prepareStatement(sql);
				psmt.setString(1, mDto.getId());
				psmt.setString(2, mDto.getPwd());
				psmt.setString(3, mDto.getName());
				psmt.setString(4, mDto.getEmail());
				psmt.setString(5, mDto.getTel());
				psmt.setString(6, mDto.getBirth());
				psmt.setString(7, mDto.getGender());
				psmt.setInt(8, mDto.getRole());
				psmt.setBoolean(9, mDto.isMarketing());
				result = psmt.executeUpdate();// 영향을 받은 행의 수 리턴. insert하면 1행이 추가되므로 1이 리턴됨.
			} catch (Exception e) {
				System.out.println("회원 등록 중 오류 발생");
				e.printStackTrace();
			}
			return result;
		}
		
		// 회원 정보 검색
		public MemberDTO getMember(String id) {
			MemberDTO mDto = null;
			String sql = "select * from members where id=?";
			try {
				psmt = con.prepareStatement(sql);
				psmt.setString(1, id);
				rs = psmt.executeQuery();
				if (rs.next()) {
					//Model 객체에 입력한 값을 저장
					mDto = new MemberDTO();
					mDto.setId(rs.getString("id"));
					mDto.setPwd(rs.getString("pwd"));
					mDto.setName(rs.getString("name"));
					mDto.setEmail(rs.getString("email"));
					mDto.setTel(rs.getString("tel"));
					mDto.setBirth(rs.getString("birth"));
					mDto.setGender(rs.getString("gender"));
					mDto.setMarketing(rs.getBoolean("Marketing"));
					loadProfile(mDto);
				}
			} catch (Exception e) {
				System.out.println("회원 정보 검색 중 오류 발생");
				e.printStackTrace();
			}
			return mDto;
		}
		
		// 프로필 정보
	    public void loadProfile(MemberDTO dto) {
	        if (dto.getProfile_idx() != null) {
	            String profileQuery = "SELECT ofile, sfile, file_path, file_size, file_type FROM profiles WHERE idx = ?";
	            try (PreparedStatement profilePsmt = con.prepareStatement(profileQuery)) {
	                profilePsmt.setInt(1, dto.getProfile_idx());
	                ResultSet profileRs = profilePsmt.executeQuery();
	                if (profileRs.next()) {
	                    dto.setOfile(profileRs.getString("ofile"));
	                    dto.setSfile(profileRs.getString("sfile"));
	                    dto.setFile_path(profileRs.getString("file_path"));
	                    dto.setFile_size(profileRs.getLong("file_size"));
	                    dto.setFile_type(profileRs.getString("file_type"));
	                }
	            } catch (Exception e) {
	                System.out.println("썸네일 조회 중 예외 발생");
	                e.printStackTrace();
	            }
	        }
	    }
		
		//회원 정보 수정
		public int updateMember(MemberDTO mDto) {
			int result = -1;
			String sql = "update members set pwd=?, name=?, email=?, "
					+ " tel=?, birth=?, gender=?, marketing=? where id=?";
			try {
				psmt = con.prepareStatement(sql);
				psmt.setString(8, mDto.getId());
				psmt.setString(1, mDto.getPwd());
				psmt.setString(2, mDto.getName());
				psmt.setString(3, mDto.getEmail());
				psmt.setString(4, mDto.getTel());
				psmt.setString(5, mDto.getBirth());
				psmt.setString(6, mDto.getGender());
				psmt.setBoolean(7, mDto.isMarketing());
				result = psmt.executeUpdate();
			} catch (Exception e) {
				System.out.println("회원 정보 수정 중 오류 발생");
				e.printStackTrace();
			} finally {
				close(); }
			return result;
		}
		
		// 게시글 데이터를 받아 DB에 저장되어 있던 내용을 갱신(파일 업로드 지원).
//	    public int updatePost(ArticlesDTO dto, String idx, String thumb_idx) {
//	        int updatedArticleId = 0;
//	        try {
//	            // 파일 업로드 없이 단순 업데이트인 경우 (파일 변경 없음)
//	            if (dto.getOfile() == null || dto.getOfile().trim().equals("")) {
//	                String query = "UPDATE articles " +
//	                               "SET title = ?, content = ?, category = ?, created_at = ? " +
//	                               "WHERE idx = ?";
//	                psmt = con.prepareStatement(query);
//	                psmt.setString(1, dto.getTitle());
//	                psmt.setString(2, dto.getContent());
//	                psmt.setInt(3, dto.getCategory());
//	                psmt.setTimestamp(4, dto.getCreated_at());
//	                psmt.setString(5, idx);
//	                int result = psmt.executeUpdate();
//	                if (result > 0) {
//	                    updatedArticleId = Integer.parseInt(idx);
//	                }
//	            } else {
//	                // 파일 업로드가 있는 경우
//	                // thumb_idx 값에 따라 기존 썸네일 레코드가 있는지 판단
//	                if (thumb_idx != null && thumb_idx.trim().equals("0")) {
//	                    // thumb_idx가 "0"이면 부모 썸네일 레코드가 없으므로 INSERT
//	                    String insertThumbQuery = 
//	                        "INSERT INTO thumbnails (idx, ofile, sfile, file_path, file_size, file_type, created_at) " +
//	                        "VALUES (thumbnails_seq.nextval, ?, ?, ?, ?, ?, ?)";
//	                    PreparedStatement pstmtThumb = con.prepareStatement(insertThumbQuery, new String[]{"idx"});
//	                    pstmtThumb.setString(1, dto.getOfile());
//	                    pstmtThumb.setString(2, dto.getSfile());
//	                    pstmtThumb.setString(3, dto.getFile_path());
//	                    pstmtThumb.setLong(4, dto.getFile_size());
//	                    pstmtThumb.setString(5, dto.getFile_type());
//	                    pstmtThumb.setTimestamp(6, dto.getCreated_at());
//	                    int thumbResult = pstmtThumb.executeUpdate();
//	                    int newThumbId = 0;
//	                    if (thumbResult > 0) {
//	                        ResultSet rs = pstmtThumb.getGeneratedKeys();
//	                        if (rs.next()) {
//	                            newThumbId = rs.getInt(1);
//	                        }
//	                        rs.close();
//	                    }
//	                    pstmtThumb.close();
//	                    
//	                    // articles 테이블 업데이트 (새 썸네일 키 반영)
//	                    String updateArticleQuery = 
//	                        "UPDATE articles " +
//	                        "SET title = ?, content = ?, category = ?, created_at = ?, thumbnails_idx = ? " +
//	                        "WHERE idx = ?";
//	                    PreparedStatement pstmtArticle = con.prepareStatement(updateArticleQuery);
//	                    pstmtArticle.setString(1, dto.getTitle());
//	                    pstmtArticle.setString(2, dto.getContent());
//	                    pstmtArticle.setInt(3, dto.getCategory());
//	                    pstmtArticle.setTimestamp(4, dto.getCreated_at());
//	                    pstmtArticle.setInt(5, newThumbId);
//	                    pstmtArticle.setString(6, idx);
//	                    int artResult = pstmtArticle.executeUpdate();
//	                    if (artResult > 0) {
//	                        updatedArticleId = Integer.parseInt(idx);
//	                    }
//	                    pstmtArticle.close();
//	                } else {
//	                    // thumb_idx가 "0"이 아니면 기존 썸네일 레코드가 존재하므로, 
//	                    // PL/SQL 블록을 사용해 thumbnails 테이블과 articles 테이블 모두 업데이트
//	                    String query =
//	                        "BEGIN " +
//	                        "  UPDATE thumbnails " +
//	                        "  SET ofile = ?, " +
//	                        "      sfile = ?, " +
//	                        "      file_path = ?, " +
//	                        "      file_size = ?, " +
//	                        "      file_type = ?, " +
//	                        "      created_at = ? " +
//	                        "  WHERE idx = ?; " +
//	                        "  UPDATE articles " +
//	                        "  SET title = ?, " +
//	                        "      content = ?, " +
//	                        "      category = ?, " +
//	                        "      created_at = ?, " +
//	                        "      thumbnails_idx = ? " +
//	                        "  WHERE idx = ?; " +
//	                        "END;";
//	                    CallableStatement cstmt = con.prepareCall(query);
//	                    
//	                    // thumbnails 업데이트 파라미터 (순서대로)
//	                    cstmt.setString(1, dto.getOfile());
//	                    cstmt.setString(2, dto.getSfile());
//	                    cstmt.setString(3, dto.getFile_path());
//	                    cstmt.setLong(4, dto.getFile_size());
//	                    cstmt.setString(5, dto.getFile_type());
//	                    cstmt.setTimestamp(6, dto.getCreated_at());
//	                    cstmt.setString(7, thumb_idx);
//	                    
//	                    // articles 업데이트 파라미터 (순서대로)
//	                    cstmt.setString(8, dto.getTitle());
//	                    cstmt.setString(9, dto.getContent());
//	                    cstmt.setInt(10, dto.getCategory());
//	                    cstmt.setTimestamp(11, dto.getCreated_at());
//	                    cstmt.setString(12, thumb_idx);
//	                    cstmt.setString(13, idx);
//	                    
//	                    cstmt.execute();
//	                    updatedArticleId = Integer.parseInt(idx);
//	                    cstmt.close();
//	                }
//	            }
//	        } catch (Exception e) {
//	            System.out.println("게시물 수정 중 예외 발생");
//	            e.printStackTrace();
//	        }
//	        return updatedArticleId;
//	    }
		
		//특정 유저 정보 가져오기
		public MemberDTO userInfo(String member_id) {
			MemberDTO mDto = null;
		    String query = "SELECT id, name, email, role FROM members WHERE id = ?"; // 필요한 컬럼 선택

		    try {
		        psmt = con.prepareStatement(query);
		        psmt.setString(1, member_id); // memberId를 쿼리에 설정
		        rs = psmt.executeQuery();

		        if (rs.next()) {
		            mDto = new MemberDTO(); // MemberDTO 객체 생성
		            mDto.setId(rs.getString("id"));
		            mDto.setName(rs.getString("name"));
		            mDto.setEmail(rs.getString("email"));
		            mDto.setRole(rs.getInt("role"));
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			return mDto;
		}
}