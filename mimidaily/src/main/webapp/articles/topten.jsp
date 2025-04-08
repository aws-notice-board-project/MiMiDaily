<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/css/main.css">
<script type="module" src="/script/newsAside.js"></script>
</head>
<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<h2 class="news_title">Top 10</h2>
	<div class="news_container">
	    <c:choose>
	        <c:when test="${not empty topTenArticles}">
	            <c:forEach var="article" items="${topTenArticles}">
			        <p>${article.title}</p>
			        <p>${article.content}</p>
			        <p>${article.category}</p>
			        <p>${article.created_at}</p>
			        <p>${article.visitcnt}</p>
			        <p>${article.members_id}</p>
			        <p>${article.thumnails_idx}</p>
			        <p>${article.likes}</p>
	            </c:forEach>
	        </c:when>
	        <c:otherwise>
	            <tr><td colspan="5">인기 게시글이 없습니다.</td></tr>
	        </c:otherwise>
	    </c:choose>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>