<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/topten.css">
</head>
<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<h2 id="title">Top 10</h2>
	<div id="news_container">
      <c:choose>
      <c:when test="${not empty topTenArticles}">
        <c:forEach var="article" items="${topTenArticles}">
       	<div class="news">
       		<div class="news_thumnails">
			<c:if test="${article.thumnails_idx == 0}">
				<img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="no image">
			</c:if>
			<c:if test="${article.thumnails_idx != 0}">
				<img src="${pageContext.request.contextPath}${row.file_path}${row.sfile}" alt="${article.idx}_thumbnail">
			</c:if>	
			</div>
       		<div class="news_title">
       			<p>
       				<span>#ㅁㅇㄴㄹ</span>
       				<span>#ㅁㄹㅇ</span>
       				<span>#ㅁㄴㄹㄴㅁ</span>
       			</p>
	       		<p>${article.title}</p>
       		</div>
       	</div>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <p>인기 게시글이 없습니다.<p>
      </c:otherwise>
      </c:choose>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>