package com.mimidaily.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.mimidaily.dto.MemberDTO;

import com.mimidaily.common.DBConnPool;

public class MemberDAO {
	// public class MemberDAO extends DBConnPool {
	// public MemberDAO() {
	// super();
	// }

	// singleton pattern
	// 생성자를 private로 설정. 직접 instance를 private static으로 생성해서 공유. 메모리절약
	// private 생성자
	private MemberDAO() {
	}

	// instance 생성
	private static MemberDAO instance = new MemberDAO();

	// instance 리턴
	public static MemberDAO getInstance() {
		return instance;
	}

	// DBCP 연결 connection 객체 생성
	public Connection getConnection() throws Exception {
		Connection conn = null;
		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");
		conn = ds.getConnection();
		System.out.println(conn);
		return conn;
	}

	// 로그인시 사용하는 메서드
	public int userCheck(String userid, String pwd) {
		/*
		 * int result = -1; // 기본값 String query =
		 * "select pwd from member where userid = ?"; // 쿼리문 템플릿 준비 try { psmt =
		 * con.prepareStatement(query); // 쿼리문 준비 psmt.setString(1, userid); // 인파라미터 설정
		 * rs = psmt.executeQuery(); // 쿼리문 실행
		 * 
		 * if (rs.next()) { if (rs.getString("pwd") != null &&
		 * rs.getString("pwd").equals(pwd)) { result = 1; // 비밀번호일치 } else { result = 0;
		 * // 비밀번호 불일치 } } else { result = -1; // i id 없음 } } catch (Exception e) {
		 * System.out.println("게시물 상세보기 중 예외 발생"); e.printStackTrace(); } return result;
		 * // 결과 반환
		 */
		int result = -1; // 기본값

		String sql = "select pwd from members where id = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection(); // connection 객체 생성
			pstmt = conn.prepareStatement(sql); // preparedStatement 객체 생성
			pstmt.setString(1, userid); // ? 위치에 들어가는 값 세팅
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("pwd") != null && rs.getString("pwd").equals(pwd)) {
					result = 1; // 비밀번호일치
				} else {
					result = 0; // 비밀번호 불일치
				}
			} else {
				result = -1; // i id 없음
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
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
