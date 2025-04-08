package com.mimidaily.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.ArticlesDTO;

public class TopTenDAO extends DBConnPool{
	public TopTenDAO() {
		super();
	}
	
	// 상위 10개 게시물 반환
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
				+" where rownum <= 17";
//		System.out.println(query);
		
		try {
			psmt=con.prepareStatement(query); // 동적쿼리
			rs=psmt.executeQuery();
			while(rs.next()) {
				ArticlesDTO dto=new ArticlesDTO();
				int articleIdx = rs.getInt(1);
				dto.setIdx(articleIdx);
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString(3));
                dto.setCategory(rs.getInt(4));
                dto.setCreated_at(rs.getTimestamp(5));
                dto.setVisitcnt(rs.getInt(6));
                dto.setMembers_id(rs.getString(7));
                dto.setThumbnails_idx(rs.getInt(8));
                dto.setLikes(rs.getInt(10));
                // 해시태그 조회
                List<String> hashtags=new ArrayList<String>();
                String hashtagQuery =
                    "SELECT h.name " +
                    "  FROM hashtags_articles ha JOIN hashtags h " +
                    "  ON ha.hashtags_idx = h.idx " +
                    " WHERE ha.articles_idx = ?";
                PreparedStatement tagPstmt = con.prepareStatement(hashtagQuery);
                tagPstmt.setInt(1, articleIdx);
                ResultSet tagRs = tagPstmt.executeQuery();
                while (tagRs.next()) {
                    hashtags.add(tagRs.getString("name"));
                }
                tagRs.close();
                tagPstmt.close();
                dto.setHashtags(hashtags);  // 해시태그 목록 추가
                
				article.add(dto);
			}
		}catch(Exception e) {
			System.out.println("기사 조회 중 예외 발생");
			e.printStackTrace();
		}
		return article;
	}
}