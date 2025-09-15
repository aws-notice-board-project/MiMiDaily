package com.mimidaily.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.CommentsDTO;
import com.mimidaily.dto.MemberDTO;

public class CommentsDAO extends DBConnPool {
	// 댓글 수
	public int commentsCount(int article_idx) {
		int comntCnt=0;
		String query="SELECT COUNT(*) FROM comments WHERE articles_idx=?";
		try {
			psmt=con.prepareStatement(query);
			psmt.setInt(1, article_idx);
			rs=psmt.executeQuery();
			if(rs.next())
				comntCnt=rs.getInt(1);
		}catch(Exception e) {
			System.out.println("댓글 목록 조회 실패");
			e.printStackTrace();
		}
		return comntCnt;
	}
	
	// 댓글 목록 조회 
	public List<CommentsDTO> selectComments(int article_idx, int start, int end) {
		List<CommentsDTO> comments=new ArrayList<CommentsDTO>();
		String query = "SELECT * FROM comments WHERE articles_idx = ? ORDER BY idx DESC LIMIT ?, ?";
		try {
			psmt=con.prepareStatement(query);
			psmt.setInt(1, article_idx);
			psmt.setInt(2, start - 1); // MySQL: 0-base
			psmt.setInt(3, end - start + 1);
			rs=psmt.executeQuery();
			while(rs.next()) {
				CommentsDTO dto = new CommentsDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setContext(rs.getString("context"));
				dto.setCreated_at(rs.getTimestamp("created_at"));
				dto.setUpdated_at(rs.getTimestamp("updated_at"));
				dto.setMembers_id(rs.getString("members_id"));
				dto.setArticles_idx(rs.getInt("articles_idx"));
				MemberDTO mDto = getMember(dto.getMembers_id());
				if (mDto != null) {
					dto.setProfiles(mDto);
				}
				dto.setIs_sameday(dto.isSameDay());
				dto.setIs_updated(dto.isUpdated());
				comments.add(dto);
			}
		}catch(Exception e) {
			System.out.println("댓글 목록 조회 실패");
			e.printStackTrace();
		}
		return comments;
	}
	
	// 댓글 생성
	public CommentsDTO insertComments(CommentsDTO dto) {
		CommentsDTO insertComment=new CommentsDTO();
		int result=0;
		String query = "INSERT INTO comments (context, created_at, updated_at, members_id, articles_idx) VALUES (?, NOW(), NOW(), ?, ?)";
		try {
			psmt=con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			psmt.setString(1, dto.getContext());
			psmt.setString(2, dto.getMembers_id());
			psmt.setInt(3, dto.getArticles_idx());
			result=psmt.executeUpdate();
			if (result > 0) {
	            ResultSet rs = psmt.getGeneratedKeys();
	            if (rs.next()) {
	                int insertedIdx = rs.getInt(1);
	                insertComment.setIdx(insertedIdx);
	                insertComment.setContext(dto.getContext());
	                insertComment.setMembers_id(dto.getMembers_id());
	                insertComment.setArticles_idx(dto.getArticles_idx());
	            }
	            rs.close();
	            System.out.println("댓글 생성 성공");
	        }

		}catch(Exception e) {
			System.out.println("댓글 생성 실패");
			e.printStackTrace();
		}
		return insertComment;
	}
	
	// 댓글 삭제
	public int deleteComments(int comment_idx) {
		int result=0;
		String query="DELETE FROM comments WHERE idx=?";
		try {
			psmt=con.prepareStatement(query);
			psmt.setInt(1, comment_idx);
			result=psmt.executeUpdate();		
		}catch(Exception e) {
			System.out.println("댓글 삭제 실패");
			e.printStackTrace();
		}
		return result;
	}
	
	// 댓글 수정
	public CommentsDTO updateComment(int idx, String context) {
	    String query = "UPDATE comments SET context = ?, updated_at=NOW() WHERE idx = ?";
	    String selectQuery = "SELECT context FROM comments WHERE idx = ?";
	    try {
	        psmt = con.prepareStatement(query);
	        psmt.setString(1, context);
	        psmt.setInt(2, idx);
	        int result = psmt.executeUpdate();
            if (result>0) {
            	psmt = con.prepareStatement(selectQuery);
                psmt.setInt(1, idx);
                rs = psmt.executeQuery();
            	if (rs.next()) {
                    CommentsDTO updatedComment = new CommentsDTO();
                    updatedComment.setContext(rs.getString("context"));
                    return updatedComment;
                }
	        }
	    } catch (Exception e) {
	    	System.out.println("댓글 수정 실패");
	        e.printStackTrace();
	    }
	    return null;
	}
	
	// (댓글조회-프로필) 멤버 정보
	public MemberDTO getMember(String id) {
		MemberDTO mDto = null;
		String sql = "SELECT id, profiles_idx FROM members WHERE id=?";
		try(PreparedStatement localPsmt = con.prepareStatement(sql);) {
			localPsmt.setString(1, id);
	        ResultSet localRs = localPsmt.executeQuery();
	        if (localRs.next()) {
	            mDto = new MemberDTO();
	            mDto.setId(localRs.getString("id"));
	            mDto.setProfile_idx(localRs.getInt("profiles_idx"));
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
                System.out.println("프로필 조회 중 예외 발생");
                e.printStackTrace();
            }
        }
    }
}