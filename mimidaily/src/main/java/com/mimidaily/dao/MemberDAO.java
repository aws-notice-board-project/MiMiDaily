package com.mimidaily.dao;

import java.sql.SQLException;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.MemberDTO;

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
        } finally {
            // 자원 반납
            try {
                if (rs != null) rs.close();
                if (psmt != null) psmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return visitcnt; // 증가된 방문 횟수 반환
    }
    
    // 멤버정보 가져오기(필요한 정보만)
    public MemberInfo getMemberInfo(String memberId) {
        MemberInfo memberInfo = null;
        String sql = "SELECT m.id, m.name, COUNT(a.idx) AS article_count, "
                   + "COUNT(c.idx) AS comment_count, m.created_at "
                   + "FROM members m "
                   + "LEFT JOIN articles a ON m.id = a.members_id "
                   + "LEFT JOIN comments c ON a.idx = c.articles_idx "
                   + "WHERE m.id = ? "
                   + "GROUP BY m.id, m.name, m.created_at";
        try {
        	psmt = con.prepareStatement(sql);
            psmt.setString(1, memberId);
            rs = psmt.executeQuery();

            if (rs.next()) {
                memberInfo = new MemberInfo();
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
			System.out.println("예외? 오류? 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	// 회원 등록
		public int insertMember(MemberDTO mDto) {
			int result = -1;
			String sql = "insert into members values(?, ?, ?, ?, ?)"; // 필수 입력 값 5개
			try {
				psmt = con.prepareStatement(sql);
				psmt.setString(1, mDto.getId());
				psmt.setString(2, mDto.getPwd());
				psmt.setString(3, mDto.getName());
				psmt.setString(4, mDto.getEmail());
				psmt.setString(5, mDto.getTel());
				psmt.setString(6, mDto.getBirth());
				psmt.setString(7, mDto.getGender());
				psmt.setBoolean(8, mDto.isMarketing());
				psmt.setInt(9, mDto.getRole());
				psmt.setObject(10, mDto.getProfile_idx());
				psmt.setString(11, mDto.getCreated_at());
				result = psmt.executeUpdate();// 영향을 받은 행의 수 리턴. insert하면 1행이 추가되므로 1이 리턴됨.
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (psmt != null)
						psmt.close();
					if (con != null)
						con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}
}