<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>맛집 기사</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/musteat.css">

</head>

<body id="musteat">
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<jsp:include page="/components/searchBar.jsp"></jsp:include>
	
	
	
	<div id="wrap">
		<div class="top_title"></div>
		<div class="cont">
			<div class="news_list">
				<c:choose>
					<c:when test="${ empty boardLists }">
						등록된 게시물이 없을 때
					</c:when>
					<c:otherwise> <!-- 게시물이 있을 때 -->
						<c:forEach items="${ boardLists }" var="row" varStatus="loop">
							<%-- ${ row.idx } <!-- 번호 --> ${ map.totalCount - (((map.pageNum-1) * map.pageSize) + loop.index)} --%>
							<div class="news"><!-- <a href="../mvcboard/view.do?idx=${ row.idx }">${ row.title }</a> -->
								<div class="news_info">
									<div class="news_title">${ row.title }</div>
									<div class="news_text">${ row.content }</div>
									<div class="news_date">${fn:substring(row.created_at, 0, 10)}</div>
									<div class="news_writer">${ row.members_id }</div>
								</div>
								<div class="news_thumnails">
									${ row.thumnails_idx }
								</div>
							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="right_cont"></div>
		</div>
		<div class="page_nav"></div>
	</div>
	
	
	
	
	
	<!-- <h2>파일 첨부형 게시판 - 목록 보기(List)</h2> -->

	<!-- 검색 폼 -->
	<!-- <form method="get">
		<table border="1" width="90%">
			<tr>
				<td align="center">
					<select name="searchField">
						<option value="title">제목</option>
						<option value="content">내용</option>
					</select>
					<input type="text" name="searchWord" />
					<input type="submit" value="검색하기" />
				</td>
			</tr>
		</table>
	</form> -->

	<!-- 목록 테이블 -->

	<!-- 하단 메뉴(바로가기, 글쓰기) -->
	<!-- <table border="1" width="90%">
		<tr align="center">
			<td>
				${ map.pagingImg }
			</td>
			<td width="100"><button type="button" onclick="location.href='../mvcboard/write.do';">글쓰기</button></td>
		</tr>
	</table> -->
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>

</html>