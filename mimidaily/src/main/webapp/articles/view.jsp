<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>
</head>
<body>
<h2>파일 첨부형 게시판 - 상세 보기(View)</h2>

<table border="1" width="90%">
    <colgroup>
        <col width="15%"/> <col width="35%"/>
        <col width="15%"/> <col width="*"/>
    </colgroup>

    <!-- 게시글 정보 -->
    <tr>
        <td>번호</td> <td>${ dto.idx }</td>
        <td>작성자</td> <td>${ dto.members_id }</td>
    </tr>
    <tr>
        <td>작성일</td> <td>${fn:substring(dto.created_at, 0, 10)}</td>
        <td>조회수</td> <td>${ dto.visitcnt }</td>
    </tr>
    <tr>
        <td>제목</td>
        <td colspan="3">${ dto.title }</td>
    </tr>
    <tr>
        <td>내용</td>
        <td colspan="3" height="100">
        	${ dto.content }
        </td>
    </tr>

    <!-- 하단 메뉴(버튼) -->
    <tr>
        <td colspan="4" align="center">
            <button type="button" onclick="location.href='../mvcboard/pass.do?mode=edit&idx=${ param.idx }';">
                수정하기
            </button>
            <button type="button" onclick="location.href='../mvcboard/pass.do?mode=delete&idx=${ param.idx }';">
                삭제하기
            </button>
            <button type="button" onclick="location.href='../mvcboard/list.do';">
                목록 바로가기
            </button>
        </td>
    </tr>
</table>
</body>
</html>