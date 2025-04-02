package com.mimidaily.dao;

import java.sql.SQLException;

import com.mimidaily.common.DBConnPool;

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
		// 결과 반환
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


	/*
	 * public static MemberDAO getInstance() { // TODO Auto-generated method stub
	 * return null; }
	 */

	// id 중복체크
	// public int confirmID(String userid) {
	// int result = -1;
	// String sql = "select userid from member where userid=?";
	// Connection conn = null;
	// PreparedStatement pstmt = null;
	// ResultSet rs = null;
	// try {
	// conn = getConnection();
	// pstmt = conn.prepareStatement(sql);
	// pstmt.setString(1, userid);
	// rs = pstmt.executeQuery();
	// if (rs.next()) {
	// result = 1;
	// } else {
	// result = -1;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (rs != null)
	// rs.close();
	// if (pstmt != null)
	// pstmt.close();
	// if (conn != null)
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return result;
	// }

	// 회원 등록
	// public int insertMember(MemberDTO mVo) {
	// int result = -1;
	// String sql = "insert into member values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	// Connection conn = null;
	// PreparedStatement pstmt = null;
	// try {
	// conn = getConnection();
	// pstmt = conn.prepareStatement(sql);
	// pstmt.setString(1, mVo.getId());
	// pstmt.setString(2, mVo.getPwd());
	// pstmt.setString(3, mVo.getName());
	// pstmt.setString(4, mVo.getBirth());
	// pstmt.setString(5, mVo.getGender());
	// pstmt.setString(6, mVo.getEmail());
	// pstmt.setString(7, mVo.getTel());
	// pstmt.setString(8, mVo.getMarketing());
	// pstmt.setInt(9, mVo.getProfile_idx());
	// pstmt.setString(10, mVo.getIsreporter());
	// pstmt.setString(11, mVo.getCreated_at());
	// result = pstmt.executeUpdate();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (pstmt != null)
	// pstmt.close();
	// if (conn != null)
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return result;
	// }

	// 아이디로 회원 정보 가져오는 메소드
	// public MemberDTO getMember(String userid) {
	// MemberDTO mVo = null;
	// String sql = "select * from member where userid=?";
	// Connection conn = null;
	// PreparedStatement pstmt = null;
	// ResultSet rs = null;
	// try {
	// conn = getConnection();
	// pstmt = conn.prepareStatement(sql);
	// pstmt.setString(1, userid);
	// rs = pstmt.executeQuery();
	// if (rs.next()) {
	// mVo = new MemberDTO();
	// mVo.setId(rs.getString("id"));
	// mVo.setPwd(rs.getString("pwd"));
	// mVo.setName(rs.getString("name"));
	// mVo.setBirth(rs.getString("birth"));
	// mVo.setGender(rs.getString("gender"));
	// mVo.setEmail(rs.getString("email"));
	// mVo.setTel(rs.getString("tel"));
	// mVo.setMarketing(rs.getString("marketing"));
	// mVo.setProfile_idx(rs.getInt("profile_idx"));
	// mVo.setIsreporter(rs.getString("isreporter"));
	// mVo.setCreated_at(rs.getString("created_at"));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (rs != null)
	// rs.close();
	// if (pstmt != null)
	// pstmt.close();
	// if (conn != null)
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return mVo;
	// }

	// 회원정보 수정
	// public int updateMember(MemberDTO mVo) {
	// int result = -1;
	// String sql = "insert into member values(?, ?, ?, ?, ?, ?)";
	// Connection conn = null;
	// PreparedStatement pstmt = null;
	// try {
	// conn = getConnection();
	// pstmt = conn.prepareStatement(sql);
	// pstmt.setString(1, mVo.getId());
	// pstmt.setString(2, mVo.getPwd());
	// pstmt.setString(3, mVo.getName());
	// pstmt.setString(4, mVo.getBirth());
	// pstmt.setString(5, mVo.getGender());
	// pstmt.setString(6, mVo.getEmail());
	// pstmt.setString(7, mVo.getTel());
	// pstmt.setString(8, mVo.getMarketing());
	// pstmt.setInt(9, mVo.getProfile_idx());
	// pstmt.setString(10, mVo.getIsreporter());
	// pstmt.setString(11, mVo.getCreated_at());
	// result = pstmt.executeUpdate();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (pstmt != null)
	// pstmt.close();
	// if (conn != null)
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return result;
	// }
}
