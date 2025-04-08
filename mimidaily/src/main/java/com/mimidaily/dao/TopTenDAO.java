package com.mimidaily.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
				+"		  order by likecnt desc visitcnt) "
				+" where rownum <= 17";
//		System.out.println(query);
		
		try {
			psmt=con.prepareStatement(query); // 동적쿼리
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
                dto.setLikes(rs.getInt(10));
                
				article.add(dto);
			}
//			System.out.println("DB에서 데이터 가져옴...?");
		}catch(Exception e) {
			System.out.println("기사 조회 중 예외 발생");
			e.printStackTrace();
		}
		return article;
	}
}