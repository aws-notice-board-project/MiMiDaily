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
	<h2>여행지 뉴스</h2>
	<div class="news_container">    
		<section class="news_list">
			<c:choose>
				<c:when test="${ empty articleLists }">
					<div class="news_cont">
						등록된 게시물이 없습니다.
					</div>
				</c:when>
				<c:otherwise>
					<c:forEach items="${ articleLists }" var="i" varStatus="loop">
						<div class="news_cont">
							<div class="title">${ i.idx }</div>
							<div class="content">${ i.title }</div>
							<p class="date">${ i.formattedDate }</p>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			${ map.paging }
		</section>
		
		<aside>
			<div class="userbox">
				<div class="profile">
					<!-- 프로필 임시 -->
					<i class="fa-solid fa-circle-user"></i>
					<div>
						<p>이름</p>
						<p>2025.00.00 가입</p>
					</div>
				</div>
				<div class="info">
					<p>내가 쓴 게시글</p>
					<p>내가 쓴 댓글</p>
					<div>
						<button>글쓰기(기자만)</button>
						<button>나의 정보</button>
					</div>
				</div>
			</div>

			<div class="most_viewed_news cont">
				<h3>실시간 관심 기사</h3>
				<ul class="news_list">
					<li>
						<img src="" alt="뉴스 이미지">
						<div>
							<h4>기사 제목</h4>
							<p>기사 내용</p>
							<p>2025.00.00</p>
						</div>
					</li>
				</ul>
			</div>
		</aside>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>