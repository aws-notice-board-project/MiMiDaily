<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>미미일보</title>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- tailwindCSS -->
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
<!-- fontawsome -->
<script src="https://kit.fontawesome.com/e7c9242ec2.js" crossorigin="anonymous"></script>
 
<link rel="stylesheet" type="text/css" href="/css/main.css">
<link rel="stylesheet" type="text/css" href="/css/index.css">
<script type="module" src="/script/index.js"></script>

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
				<p><b>미미일보</b>가 당신의 여정에 동행합니다.</p>
				<a class="login" href="login.do">로그인</a>
				<a class="register" href="#">회원가입</a>
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