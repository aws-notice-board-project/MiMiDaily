package com.mimidaily.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.ArticlesDTO;

public class ArticlesTDAO extends DBConnPool {
	public ArticlesTDAO() {
		super();
	}
	
	// 게시물 개수
	public int selectCnt(Map<String,Object> map) {
		int totalCnt=0;
		String query="select count(*) from articles";
		
		try {
			stmt=con.createStatement(); // 정적쿼리
			rs=stmt.executeQuery(query);
			rs.next();
			totalCnt=rs.getInt(1);
		} catch (Exception e) {
			System.out.println("기사 카운트 중 예외 발생");
			e.printStackTrace();
		}
		return totalCnt;
	}
	
	// 목록 반환(List)
	public List<ArticlesDTO> selectListPage(Map<String,Object> map){
		List<ArticlesDTO> article=new ArrayList<ArticlesDTO>();
		String query=""
					+"select * from ("
					+"	select Tb.*, rownum rNum from (" // Tb의 모든 칼럼, rNum: rownum의 별칭
					+"		select * from articles"; // Tb
		query+="orderby idx desc"
				+") Tb"
				+")"
				+"where rNum between ? and ?";
		
		try {
			psmt=con.prepareStatement(query); // 동적쿼리
			psmt.setString(1, map.get("start").toString());
			psmt.setString(2, map.get("end").toString());
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
                dto.setThumnails_idx(rs.getInt(8));
				article.add(dto);
			}
		}catch(Exception e) {e.getStackTrace();}
		return article;
	}
	
}
