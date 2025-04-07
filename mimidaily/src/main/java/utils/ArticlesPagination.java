package utils;

import java.util.Map;

public class ArticlesPagination {
	public static String pagingBox(int totalCnt, int pageSize, int blockPage, int pageNum, String reqUrl, Map<String, Object> searchParams) {
		String pagingBox = "";
		 
		// 전체 페이지수 계산(나머지가 있을경우, 페이지 올림)
		int totalPages=(int)(Math.ceil((double)totalCnt/pageSize));
		
		// 각 블록의 첫 페이지
		int pageTemp=(((pageNum-1)/blockPage)*blockPage)+1; // 1~5페이지는 1블럭, 6~10페이지는 2블럭 (1, 6, 11...)
		// prevBtn
		if (pageTemp != 1) {
	        pagingBox += "<a class='paging_btn' href='" + buildUrl(reqUrl, 1, searchParams) + "'><i class='fa-solid fa-angles-left'></i></a>";
	        pagingBox += "<a class='paging_btn' href='" + buildUrl(reqUrl, pageTemp - 1, searchParams) + "'><i class='fa-solid fa-angle-left'></i></a>";
	    }
		
		// 페이징 번호 출력
		int blockCnt=1;
		while (blockCnt<=blockPage && pageTemp<=totalPages) {
			if(pageTemp==pageNum) {
				// 현재 페이지
				pagingBox+="<span class='current_page'>"+pageTemp+"</span>";
			}else {
				pagingBox += "<a href='" + buildUrl(reqUrl, pageTemp, searchParams) + "'>" + pageTemp + "</a>";
			}
			pageTemp++;
			blockCnt++;
		}
		
		// nextBtn
		if (pageTemp <= totalPages) {
	        pagingBox += "<a class='paging_btn' href='" + buildUrl(reqUrl, pageTemp, searchParams) + "'><i class='fa-solid fa-angle-right'></i></a>";
	        pagingBox += "<a class='paging_btn' href='" + buildUrl(reqUrl, totalPages, searchParams) + "'><i class='fa-solid fa-angle-double-right'></i></a>";
	    }
		
		return pagingBox;
	}
	
	// searchUrl 을 생성하는 메서드
	private static String buildUrl(String reqUrl, int pageNum, Map<String, Object> searchParams) {
    	// 기본 URL 생성
    	StringBuilder url = new StringBuilder(reqUrl + "?pageNum=" + pageNum);
    	
    	// 검색 파라미터 추가
    	for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
    		// 빈 문자열인 경우는 추가하지 않음
    		if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
    			url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
    		}
    	}
    	return url.toString();
    

	}
}
