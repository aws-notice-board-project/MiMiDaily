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
<link rel="stylesheet" type="text/css" href="/css/travel.css">
<script type="module" src="/script/newsAside.js"></script>
</head>
<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<jsp:include page="/components/searchBar.jsp"></jsp:include>
	<h2 class="news_title">여행지</h2>
	<div class="news_top">
		<jsp:include page="/components/usercard.jsp"></jsp:include>
		<jsp:include page="/components/viewestNews.jsp"></jsp:include>
	</div>
	<div class="news_container">
		<section class="news_list cont">
			<c:choose>
				<c:when test="${ empty articleLists }">
					<div class="news_cont"> 
						등록된 게시물이 없습니다.
					</div>
				</c:when>
				<c:otherwise>
					<c:forEach items="${ articleLists }" var="i" varStatus="loop">
						<div class="news_cont">
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
							<div class="contents">
								<div class="title">${ i.idx }</div>
								<div class="context">${ i.title }</div> 
								<p class="date">${ i.formattedDate }</p>
							</div>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>  
			<div class="pagination_bar">
				${ map.paging }
			</div> 
		</section>
		
		<aside class="news_right">
			<jsp:include page="/components/usercard.jsp"></jsp:include>
			<jsp:include page="/components/viewestNews.jsp"></jsp:include>
		</aside>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>