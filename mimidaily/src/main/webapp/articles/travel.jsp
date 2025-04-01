<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>여행 뉴스</title>
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
<link rel="stylesheet" type="text/css" href="/css/main.css">
<link rel="stylesheet" type="text/css" href="/css/list.css">
</head>
<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<jsp:include page="/components/searchBar.jsp"></jsp:include>
	<div style="height: 800px; ">
		article list입니다.
		
		<c:choose>
			<c:when test="${ empty articleLists }">
				등록된 게시물X
			</c:when>
			<c:otherwise>
				<c:forEach items="${ articleLists }" var="i" varStatus="loop">
					${ i.idx }
					${ i.title }<br>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>