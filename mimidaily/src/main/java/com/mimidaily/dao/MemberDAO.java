package com.mimidaily.dao;

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
	
	//id 중복 확인
	public int confirmID(String userid) {
		int result = -1;
		String sql = "select userid from member where userid=?";
		try {
//			con = source.getConnection();
			psmt = con.prepareStatement(sql);
			psmt.setString(1, userid);
			rs = psmt.executeQuery();
			if (rs.next()) {
				result = 1;
			} else {
				result = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
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
	
	// 회원 등록
		public int insertMember(MemberDTO mDto) {
			int result = -1;
			String sql = "insert into member values(?, ?, ?, ?, ?, ?)";
			try {
//				con = getConnection();
				psmt = con.prepareStatement(sql);
				psmt.setString(1, mDto.getId());
				psmt.setString(2, mDto.getPwd());
				psmt.setString(3, mDto.getName());
				psmt.setString(4, mDto.getEmail());
				psmt.setString(5, mDto.getTel());
				psmt.setString(6, mDto.getBirth());
				psmt.setString(7, mDto.getGender());
				psmt.setBoolean(8, mDto.isMarketing());
				psmt.setString(9, mDto.getRole());
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
