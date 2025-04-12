package com.mimidaily.dao;

import java.util.ArrayList;
import java.util.List;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.CommentsDTO;

public class CommentsDAO extends DBConnPool {
	
	public int commentsCount(int article_idx) {
		int comntCnt=0;
		String query="select count(*) from comments where articles_idx=? order by idx desc";
		try {
			psmt=con.prepareStatement(query);
			psmt.setInt(1, article_idx);
			rs=psmt.executeQuery();
			rs.next();
			comntCnt=rs.getInt(1);
		}catch(Exception e) {
			System.out.println("댓글 목록 조회 실패");
			e.printStackTrace();
		}
		return comntCnt;
	}
	
	public List<CommentsDTO> selectComments(int article_idx) {
		List<CommentsDTO> comments=new ArrayList<CommentsDTO>();
		String query="select * from comments where articles_idx=? order by idx desc";
		try {
			psmt=con.prepareStatement(query);
			psmt.setInt(1, article_idx);
			rs=psmt.executeQuery();
			while(rs.next()) {
				CommentsDTO dto =new CommentsDTO();
				dto.setIdx(rs.getInt("idx"));
				dto.setContext(rs.getString("context"));
				dto.setCreated_at(rs.getTimestamp("created_at"));
				dto.setMembers_id(rs.getString("members_id"));
				dto.setArticles_idx(rs.getInt("articles_idx"));
				dto.setIs_sameday(dto.isSameDay());
				// 추후 프로필 연결
				
				comments.add(dto);
			}
		}catch(Exception e) {
			System.out.println("댓글 목록 조회 실패");
			e.printStackTrace();
		}
		return comments;
	}
	
	public CommentsDTO insertComments(CommentsDTO dto) {
		CommentsDTO insertComment=new CommentsDTO();
		int result=0;
		String query = "INSERT INTO comments (idx, context, created_at, members_id, articles_idx) "
	             + "VALUES (comments_seq.NEXTVAL, ?, CURRENT_TIMESTAMP, ?, ?)";		
		try {
			psmt=con.prepareStatement(query);
			psmt.setString(1, dto.getContext());
			psmt.setString(2, dto.getMembers_id());
			psmt.setInt(3, dto.getArticles_idx());
			result=psmt.executeUpdate();
			if (result > 0) {
				insertComment.setContext(dto.getContext());
				insertComment.setMembers_id(dto.getMembers_id());
				insertComment.setArticles_idx(dto.getArticles_idx());
	            System.out.println("댓글 생성 성공");
	        }

		}catch(Exception e) {
			System.out.println("댓글 생성 실패");
			e.printStackTrace();
		}
		return insertComment;
	}
	
}
