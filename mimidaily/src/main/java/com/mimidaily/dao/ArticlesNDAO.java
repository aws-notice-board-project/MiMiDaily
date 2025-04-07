package com.mimidaily.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mimidaily.common.DBConnPool;
import com.mimidaily.dto.ArticlesDTO;

public class ArticlesNDAO extends DBConnPool {
	public ArticlesNDAO() {
		super();
	}
	
	// 게시물 개수
	public int selectCnt(Map<String,Object> map) {
		int totalCnt=0;
		String query="select count(*) from articles";
		
		// 검색 조건이 있다면
		if(map.get("searchWord")!=null) {
			query+=" where "+map.get("searchField")+" like '%"+map.get("searchWord")+"%'";
		}
		
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
					+"		select * from articles"; // Tb, (1:여행 2:맛집)
		
		// 검색 조건이 있다면
		if(map.get("searchWord")!=null) {
			query+=" where "+map.get("searchField")+" like '%"+map.get("searchWord")+"%'";
		}
		
		query+="		order by idx desc"
				+"	) Tb"
				+" )"
				+" where rNum between ? and ?";
		
		try {
			psmt=con.prepareStatement(query); // 동적쿼리
			psmt.setInt(1, (Integer) map.get("start"));
			psmt.setInt(2, (Integer) map.get("end"));
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
		}catch(Exception e) {
			System.out.println("게시물 목록 조회 중 예외 발생");
			e.getStackTrace();
		}
		return article;
	}
	
	// 실시간 관심기사
	public List<ArticlesDTO> viewestList(){
		List<ArticlesDTO> viewest=new ArrayList<ArticlesDTO>();

		// 조회수 많은 게시물 순으로 내림차순
		String query=""
					+"select * from (select * from articles order by visitcnt desc)"
					+" where rownum <= 4";
		
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery(query);
			while(rs.next()) {
				ArticlesDTO dto=new ArticlesDTO();
				dto.setIdx(rs.getInt(1));
                dto.setTitle(rs.getString(2));
                dto.setContent(rs.getString(3));
                dto.setCategory(rs.getInt(4));
                dto.setVisitcnt(rs.getInt(6));
                dto.setMembers_id(rs.getString(7));
                dto.setThumnails_idx(rs.getInt(8));
                viewest.add(dto);
			}
		}catch(Exception e) {e.getStackTrace();}
		return viewest;
	}
	
}
