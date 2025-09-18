<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="/components/error.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="${pageContext.request.contextPath}/media/images/favicon.ico" type="image/x-icon" />
<title>Top 10</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/topten.css">
<script src="${pageContext.request.contextPath}/script/topten.js"></script>
</head>
<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<h2 id="title">Top 10</h2>
	<div id="news_container">
      <c:choose>
      <c:when test="${not empty topTenArticles}">
        <c:forEach var="article" varStatus="status" items="${topTenArticles}">
       	<div class="news ${status.index < 3 ? 'row1' : status.index < 6 ? 'row2' : 'row3'} cont" onclick="location.href='/articles/view.do?idx=${ article.idx }'">
       		<div class="news_thumbnails">
       			<%-- <div class="news_rank">${status.index + 1 < 10 ? '0' : ''}${status.index + 1}</div> --%>
       			<div class="news_rank">${status.index + 1}</div>
                        <c:choose>
                                <c:when test="${article.thumbnails_idx == 0}">
                                        <img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="no image">
                                </c:when>
                                <c:otherwise>
                                        <c:set var="thumbUrl" value="${article.imageUrl}" />
                                        <c:choose>
                                                <c:when test="${empty thumbUrl}">
                                                        <img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="no image">
                                                </c:when>
                                                <c:when test="${fn:startsWith(thumbUrl, 'http://') || fn:startsWith(thumbUrl, 'https://')}">
                                                        <img src="${thumbUrl}" alt="${article.idx}_thumbnail">
                                                </c:when>
                                                <c:when test="${fn:startsWith(thumbUrl, '/')}">
                                                        <img src="${pageContext.request.contextPath}${thumbUrl}" alt="${article.idx}_thumbnail">
                                                </c:when>
                                                <c:otherwise>
                                                        <img src="${pageContext.request.contextPath}/${thumbUrl}" alt="${article.idx}_thumbnail">
                                                </c:otherwise>
                                        </c:choose>
                                </c:otherwise>
                        </c:choose>
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