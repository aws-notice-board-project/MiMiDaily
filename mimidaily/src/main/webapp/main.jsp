<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>미미일보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/index.css">
<script type="module" src="${pageContext.request.contextPath}/script/index.js"></script>
</head> 
<body>
<jsp:include page="/components/navigation.jsp"></jsp:include>
<jsp:include page="/components/searchbar.jsp"></jsp:include>
	<div id="main">
		<aside class="xs">
			<div class="login_box cont">
			<c:if test="${empty sessionScope.loginUser}">
				<p><b>미미일보</b>가 당신의 여정에 동행합니다.</p>
				<a class="login btn" href="login.do">로그인</a>
				<a class="register" href="join.do">회원가입</a>
			</c:if>
			<c:if test="${not empty sessionScope.loginUser}">
				<p><b>${sessionScope.loginUser != null ? sessionScope.loginUser : "게스트"}님</b> 환영합니다.</p>
				<div class="logining"> 
					<c:if test="${ userRole==0 || userRole==2 }">
						<a class="write btn" href="/articles/write.do">기사 작성</a>
					</c:if>
					<a class="profile btn" href="update.do">나의 정보</a>
				</div>
				<div class="logout_box">
					<a class="logout" href="logout.do">로그아웃</a>
				</div>
			</c:if>
			</div>
			<div class="index_viewest">
				<jsp:include page="/components/viewest.jsp"></jsp:include>
			</div>
		</aside>
		<section class="most_liked_news cont">
			<h2>주요 뉴스</h2>
			<div class="news_list">
			    <div class="fst_news">
					<c:forEach var="news" items="${bestnews}" begin="0" end="0">
						<div onclick="location.href='/articles/view.do?idx=${ news.idx }'" style="cursor:pointer;">
				        <c:choose>
				            <c:when test="${news.thumbnails_idx == 0}"> <!-- 여기에서도 bestnews를 news로 수정 -->
				                <div class="news_img">
				                    <img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="No Image">
				                </div>
				            </c:when>
				            <c:otherwise>
				                <div class="news_img">
				                    <img src="${pageContext.request.contextPath}${news.file_path}${news.sfile}" alt="${news.title} 썸네일"> <!-- 여기에서도 news 사용 -->
				                </div>
				            </c:otherwise>
				        </c:choose>
				        <div class="news_content">
				            <h3>${news.title}</h3> <!-- 여기에서도 news.title 사용 -->
				            <p class="context">${news.content}</p>
				            <p class="date">${news.formattedDate}</p>
				        </div>
						</div>
					</c:forEach>
			    </div>
			    <div class="snd_news">
					<c:forEach var="news" items="${bestnews}" begin="1" end="2">
					    <div onclick="location.href='/articles/view.do?idx=${ news.idx }'" style="cursor:pointer;">
					        <c:choose>
					            <c:when test="${news.thumbnails_idx == 0}"> <!-- 여기에서도 bestnews를 news로 수정 -->
					                <div class="news_img">
					                    <img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="No Image">
					                </div>   
					            </c:when>
					            <c:otherwise>
					                <div class="news_img">
					                    <img src="${pageContext.request.contextPath}${news.file_path}${news.sfile}" alt="${news.title} 썸네일"> <!-- 여기에서도 news 사용 -->
					                </div>
					            </c:otherwise>
					        </c:choose>
					        <div class="news_content">
					            <h3>${news.title}</h3> <!-- 여기에서도 news.title 사용 -->
					            <p class="context">${news.content}</p>
					            <p class="date">${news.formattedDate}</p>
					        </div>
					    </div>
					</c:forEach>
			    </div>
			</div>
		</section>
		<aside class="md">
			<div class="login_box cont">
			<c:if test="${empty sessionScope.loginUser}">
				<p><b>미미일보</b>가 당신의 여정에 동행합니다.</p>
				<a class="login btn" href="login.do">로그인</a>
				<a class="register" href="join.do">회원가입</a>
			</c:if>
			<c:if test="${not empty sessionScope.loginUser}">
				<p><b>${sessionScope.loginUser != null ? sessionScope.loginUser : "게스트"}님</b> 환영합니다.</p>
				<div class="logining">
					<c:if test="${ userRole==0 || userRole==2 }">
						<a class="write btn" href="/articles/write.do">기사 작성하기</a>
					</c:if>
					<a class="profile btn" href="update.do">나의 정보</a>
				</div>
				<div class="logout_box">
					<a class="logout" href="logout.do">로그아웃</a>
				</div>
			</c:if>
			</div>
			<div class="index_viewest">
				<jsp:include page="/components/viewest.jsp"></jsp:include>
			</div>
		</aside>
	</div>
	
<jsp:include page="/components/footer.jsp"></jsp:include>

</body>
</html>