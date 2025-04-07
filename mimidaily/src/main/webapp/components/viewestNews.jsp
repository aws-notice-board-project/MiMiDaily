<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.most_viewed_news{width: 216px;margin: 1rem 0.5rem 1rem 0.5rem ; padding: 1rem;}
</style>
</head>
<body>
	<div class="most_viewed_news cont">
		<h3>실시간 관심 기사</h3>
		<ul class="news_list">
			<c:forEach items="${ viewestList }" var="i" varStatus="loop">
				<li>
					<c:choose>
			            <c:when test="${i.thumnails_idx != null}">
			            <div class="news_img">
							<img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="No Image">
			            </div>
						</c:when>
			            <c:otherwise>
			            <div class="news_img">
							<img src="" alt="${i.title} 썸네일">
			            </div>
			            </c:otherwise>
			        </c:choose>
					<div>
						<h4>${ i.title }</h4>
						<p class="content">${ i.content }</p>
						<p class="date">${ i.formattedDate }</p>
					</div>  
				</li>
			</c:forEach>
		</ul>
	</div>
</body>
</html>