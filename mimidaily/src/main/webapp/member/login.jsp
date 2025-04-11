<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="/components/error.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그인 - 미미일보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body id="login">
	<jsp:include page="/components/navigation.jsp" />
	<main id="wrap">
		<section class="content">
			<div id="login_logo">
				<img src="${pageContext.request.contextPath}/media/images/logo.svg" alt="로고 이미지">
			</div>
			<h1 class="hide">미미일보</h1>
			<h2>로그인</h2>
			<form action="login.do" method="post">
				<div class="login_box cont">
					<div class="id_box">
						<label for="userid">아이디</label>
						<input type="text" id="userid" name="userid" value="${userid}" required>
					</div>
					<div class="pw_box">
						<label for="pwd">비밀번호</label>
						<input type="password" id="pwd" name="pwd" required>
					</div>
				</div>
				<div class="join_btn">
					<span>아직 회원이 아니신가요?</span>
					<button type="button" onclick="location.href='join.do'">회원가입</button>
				</div>
				<div class="message_box">
					${message}
				</div>
				<div class="login_btn">
					<button type="submit">로그인</button>
				</div>
			</form>
		</section>
	</main>
	<jsp:include page="/components/footer.jsp" />
</body>
</html>