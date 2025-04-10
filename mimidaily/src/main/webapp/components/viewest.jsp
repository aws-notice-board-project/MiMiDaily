<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>viewest news</title>
<style>
	.most_viewed_news{width: 216px;margin: 1rem 0.5rem 1rem 0.5rem ; padding: 1rem;}
	.most_viewed_news .news_img {margin-right: 1rem;}
	.most_viewed_news h3{font-size: 20px;font-weight: bold;margin-bottom: 1rem;}
	.most_viewed_news .news_list .news_img{width: 80px;margin-right: 0.5rem;}
	.most_viewed_news .news_list img{width: 100%;}
	.most_viewed_news .news_list li{display: flex;align-items: center;margin-bottom: 1rem; width: 100%;}
	.most_viewed_news .news_list h4{white-space: nowrap;overflow: hidden;text-overflow: ellipsis;font-weight: 600;width: 100px;}
	.most_viewed_news .news_list .content{white-space: nowrap;overflow: hidden;text-overflow: ellipsis;font-size: 14px;width: 100px;}
	.most_viewed_news .news_list .date{color: #8C7B7B;font-size: 12px;text-align: end;}
	.most_viewed_news .news_list li:hover{cursor:pointer;}
</style>
</head>
<body>
	<div class="most_viewed_news cont">
		<h3>실시간 관심 기사</h3>
		<ul class="news_list">
			<c:forEach items="${ viewestList }" var="i" varStatus="loop">
				<li  onclick="location.href='/articles/view.do?idx=${ i.idx }'">
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
					<div class="news_content">
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