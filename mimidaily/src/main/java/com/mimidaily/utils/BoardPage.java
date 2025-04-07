package com.mimidaily.utils;

public class BoardPage {
    public static String pagingStr(int totalCount, int pageSize, int blockPage, int pageNum, String pageURL) {
        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        // 현재 블럭의 시작 페이지와 종료 페이지 계산
        int startPage = ((pageNum - 1) / blockPage) * blockPage + 1;
        int endPage = startPage + blockPage - 1;
        if (endPage > totalPages) {
            endPage = totalPages;
        }
        
        StringBuilder pagingStr = new StringBuilder();
        
        // 이전 블럭 링크 (현재 블럭의 시작 페이지가 1보다 크면 표시)
        if (startPage > 1) {
            pagingStr.append("<a href='").append(pageURL).append("?pageNum=")
                     .append(startPage - 1)
                     .append("'>이전</a> ");
        }
        
        // 현재 블럭의 페이지 번호 링크 생성
        for (int i = startPage; i <= endPage; i++) {
            if (i == pageNum) {
                pagingStr.append("<b>").append(i).append("</b> ");
            } else {
                pagingStr.append("<a href='").append(pageURL).append("?pageNum=")
                         .append(i)
                         .append("'>").append(i).append("</a> ");
            }
        }
        
        // 다음 블럭 링크 (현재 블럭의 마지막 페이지가 전체 페이지 수보다 작으면 표시)
        if (endPage < totalPages) {
            pagingStr.append("<a href='").append(pageURL).append("?pageNum=")
                     .append(endPage + 1)
                     .append("'>다음</a>");
        }
        
        return pagingStr.toString();
    }
}
