package utils;

public class ArticlesPagination {
	public static String pagingBox(int totalCnt, int pageSize, int blockPage, int pageNum, String reqUrl) {
		String pagingBox = "";
		
		// 전체 페이지수 계산(나머지가 있을경우, 페이지 올림)
		int totalPages=(int)(Math.ceil((double)totalCnt/pageSize));
		
		// 각 블록의 첫 페이지
		int pageTemp=(((pageNum-1)/blockPage)*blockPage)+1; // 1~5페이지는 1블럭, 6~10페이지는 2블럭 (1, 6, 11...)
		// prevBtn
		if(pageTemp!=1) {
			System.out.println("***이전버든 조건 충족***");
			pagingBox+="<a class='paging_btn' href='"+reqUrl+"?pageNum=1'><i class='fa-solid fa-angles-left'></i></a>";
			pagingBox+="<a class='paging_btn' href='"+reqUrl+"?pageNum="+(pageTemp-1)+"'><i class='fa-solid fa-angle-left'></i></a>";
		}
		
		// 페이징 번호 출력
		int blockCnt=1;
		while (blockCnt<=blockPage && pageTemp<=totalPages) {
			if(pageTemp==pageNum) {
				// 현재 페이지
				pagingBox+="<span class='current_page'>"+pageTemp+"</span>";
			}else {
				pagingBox+="<a href='" + reqUrl + "?pageNum=" + pageTemp + "'>" + pageTemp + "</a>";
			}
			pageTemp++;
			blockCnt++;
		}
		
		// nextBtn
		if(pageTemp<=totalPages) {
			pagingBox += "<a href='" + reqUrl + "?pageNum=" + pageTemp + "'><i class='fa-solid fa-angle-right'></i></a>";
		    pagingBox += "<a href='" + reqUrl + "?pageNum=" + totalPages + "'><i class='fa-solid fa-angle-double-right'></i></a>";
		}
		
		return pagingBox;
	}
}
