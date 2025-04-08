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
				+"		  order by likecnt desc) "
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
                dto.setThumnails_idx(rs.getInt(8));
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
	
	// 게시물 개수 반환(상위 10개 제외 나머지 게시물...?)
//	public int selectCnt(Map<String,Object> map) {
//		int totalCnt=0;
//		String query="select count(*) from articles";
//		
//		try {
//			stmt=con.createStatement(); // 정적쿼리..?
//			rs=stmt.executeQuery(query);
//			rs.next();
//			totalCnt=rs.getInt(1);
//		} catch (Exception e) {
//			System.out.println("기사 로딩 중 예외 발생");
//			e.printStackTrace();
//		}
//		totalCnt -= 10;
//		return totalCnt;
//	}
	
	// 게시물 목록 반환(페이징 기능 지원)
//		public List<ArticlesDTO> selectListPage(Map<String,Object> map){
//			List<ArticlesDTO> article=new ArrayList<ArticlesDTO>();
//			String query=""
//						+"select * from ("
//						+"	select Tb.*, rownum rNum from (" // Tb의 모든 칼럼, rNum: rownum의 별칭
//						+"		select * from articles where category=1"; // Tb, (1:여행 2:맛집)
//			
//			// 검색 조건이 있다면
//			if(map.get("searchWord")!=null) {
//				query+=" and "+map.get("searchField")+" like '%"+map.get("searchWord")+"%'";
//			}
//			
//			query+="		order by idx desc"
//					+"	) Tb"
//					+" )"
//					+" where rNum between ? and ?";
//			
//			try {
//				psmt=con.prepareStatement(query); // 동적쿼리
//				psmt.setInt(1, (Integer) map.get("start"));
//				psmt.setInt(2, (Integer) map.get("end"));
//				rs=psmt.executeQuery();
//				while(rs.next()) {
//					ArticlesDTO dto=new ArticlesDTO();
//					dto.setIdx(rs.getInt(1)); // rs.get...(rs의 column row) 또는 명확한 컬럼명 넣어도 됨
//	                dto.setTitle(rs.getString(2));
//	                dto.setContent(rs.getString(3));
//	                dto.setCategory(rs.getInt(4));
//	                dto.setCreated_at(rs.getTimestamp(5));
//	                dto.setVisitcnt(rs.getInt(6));
//	                dto.setMembers_id(rs.getString(7));
//	                dto.setThumnails_idx(rs.getInt(8));
//					article.add(dto);
//				}
//			}catch(Exception e) {
//				System.out.println("게시물 목록 조회 중 예외 발생");
//				e.getStackTrace();
//			}
//			return article;
//		}
}