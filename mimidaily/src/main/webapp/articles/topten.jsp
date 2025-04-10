<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Top 10</title>
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
       	<div class="news" onclick="location.href='/articles/view.do?idx=${ article.idx }'">
       		<div class="news_thumbnails">
			<c:if test="${article.thumbnails_idx == 0}">
				<img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="no image">
			</c:if>
			<c:if test="${article.thumbnails_idx != 0}">
				<img src="${pageContext.request.contextPath}${row.file_path}${row.sfile}" alt="${article.idx}_thumbnail">
			</c:if>
			</div>
       		<div class="news_title">
       			<p>
       				<c:if test="${article.category == 1}">
					<span>여행</span>
					</c:if>
					<c:if test="${article.category == 2}">
					<span>맛집</span>
					</c:if>
		            <c:if test="${not empty article.hashtags}">
	                <c:forEach var="tag" items="${article.hashtags}">
                    <span class="hashtag">#${tag}</span>
	                </c:forEach>
		            </c:if>
       			</p>
	       		<article>${article.title}</article>
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