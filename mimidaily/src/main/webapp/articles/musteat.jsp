<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>맛집 기사</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/musteat.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/musteat.js"></script>

</head>

<body id="musteat">
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<jsp:include page="/components/searchBar.jsp"></jsp:include>
	
	<div id="wrap">
		<div class="top_title">
			<span>맛집</span>
			<div class="under_line"></div>
		</div>
		<div class="cont">
			<div class="news_list">
				<c:choose>
					<c:when test="${ empty boardLists }">
						등록된 게시물이 없을 때
					</c:when>
					<c:otherwise>
						<c:forEach items="${ boardLists }" var="row" varStatus="loop">
							<%-- ${ row.idx } <!-- 번호 --> ${ map.totalCount - (((map.pageNum-1) * map.pageSize) + loop.index)} --%>
							<div class="news" onclick="location.href='/articles/view.do?idx=${ row.idx }'">
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
		<div class="page_nav">
			${ map.pagingImg }
		</div>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>

</html>