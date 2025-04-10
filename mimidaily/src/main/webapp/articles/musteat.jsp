<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>    
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>맛집 뉴스</title>
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
<link rel="stylesheet" type="text/css" href="/css/main.css">
<link rel="stylesheet" type="text/css" href="/css/articles.css">
<script type="module" src="/script/aside.js"></script>
</head>
<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<jsp:include page="/components/searchbar.jsp"></jsp:include>
	<h2 class="news_title">맛집</h2>
	<div class="news_top">
		<jsp:include page="/components/usercard.jsp"></jsp:include>
		<jsp:include page="/components/viewest.jsp"></jsp:include>
	</div>
	<div class="news_container">
		<section class="news_list cont">
			<c:choose>
				<c:when test="${ empty articleLists }">
					<div class="empty_article">
						<div class="news_cont"> 
							등록된 게시물이 없습니다.
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<c:forEach items="${ articleLists }" var="i" varStatus="loop">
						<div class="news_cont list" onclick="location.href='/articles/view.do?idx=${ i.idx }'">
							<c:choose>
					            <c:when test="${i.thumbnails_idx == 0}">
						            <div class="news_img">
										<img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="No Image">
						            </div>   
								</c:when>
					            <c:otherwise>
					            <div class="news_img">
									<img src="${pageContext.request.contextPath}${i.file_path}${i.sfile}" alt="${i.title} 썸네일">
					            </div>
					            </c:otherwise>
					        </c:choose>
							<div class="contents">
								<div class="title">${ i.title }</div>
								<div class="context">${ i.content }</div> 
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
			<div class="aside_box">
				<jsp:include page="/components/usercard.jsp"></jsp:include>
				<jsp:include page="/components/viewest.jsp"></jsp:include>
			</div>
		</aside>		
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>