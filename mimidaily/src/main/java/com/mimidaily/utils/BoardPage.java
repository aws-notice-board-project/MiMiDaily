package com.mimidaily.utils;

public class BoardPage {
    public static String pagingStr(int totalCount, int pageSize, int blockPage, int pageNum, String pageURL) {
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int startPage = ((pageNum - 1) / blockPage) * blockPage + 1;
        int endPage = startPage + blockPage - 1;
        if (endPage > totalPages) {
            endPage = totalPages;
        }
        
        StringBuilder pagingStr = new StringBuilder();
        // 시맨틱 내비게이션과 리스트 구조 사용
        pagingStr.append("<nav class='page_nav' aria-label='페이지 네비게이션'>");
        pagingStr.append("<ul>");
        
        // 이전 버튼: 항상 표시, 만약 클릭 가능한 이전 페이지가 없으면 disabled 클래스 부여
        if (startPage > 1) {
            pagingStr.append("<li><a class='page-link previous' href='")
                     .append(pageURL).append("?pageNum=")
                     .append(startPage - 1)
                     .append("'> &lt; </a></li>");
        } else {
            pagingStr.append("<li><a class='page-link previous disabled' href='javascript:void(0);'> &lt; </a></li>");
        }
        
        // 현재 블럭의 페이지 번호 링크 생성
        for (int i = startPage; i <= endPage; i++) {
            if (i == pageNum) {
                pagingStr.append("<li class='current'><b>").append(i).append("</b></li>");
            } else {
                pagingStr.append("<li><a class='page-link' href='")
                         .append(pageURL).append("?pageNum=")
                         .append(i)
                         .append("'>").append(i).append("</a></li>");
            }
        }
        
        // 다음 버튼: 항상 표시, 클릭 가능한 다음 페이지가 없으면 disabled 클래스 부여
        if (endPage < totalPages) {
            pagingStr.append("<li><a class='page-link next' href='")
                     .append(pageURL).append("?pageNum=")
                     .append(endPage + 1)
                     .append("'> &gt; </a></li>");
        } else {
            pagingStr.append("<li><a class='page-link next disabled' href='javascript:void(0);'> &gt; </a></li>");
        }
        
        pagingStr.append("</ul>");
        pagingStr.append("</nav>");
        
        return pagingStr.toString();
    }
}
