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
<jsp:include page="/components/searchBar.jsp"></jsp:include>
	<div id="main">
		<section class="most_liked_news cont">
			<h2>주요 뉴스</h2>
			<div>
				<ul class="news_list">
					<li>
						<img src="" alt="뉴스 이미지">
						<div>
							<h3>기사 제목</h3>
							<p>기사 내용</p>
							<p>2025.00.00</p>
						</div>
					</li>
				</ul>
			</div>
			<div></div>
		</section>
		<aside>
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
						<a class="profile btn" href="update.do">나의 정보</a>
					</c:if>
				</div>
				<div class="logout_box">
					<a class="logout" href="logout.do">로그아웃</a>
				</div>
			</c:if>
			</div>
			<div class="index_viewest">
				<jsp:include page="/components/viewestNews.jsp"></jsp:include>
			</div>
		</aside>
	</div>
	
<jsp:include page="/components/footer.jsp"></jsp:include>

</body>
</html>