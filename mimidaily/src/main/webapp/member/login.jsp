<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그인</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">

</head>
<body id="login">
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<div id="wrap">
		<div class="cont">
			<div id="logo"></div>
			<h1 class="hide">미미일보</h1>
			<h2>로그인</h2>
			<form action="login.do" method="post">
				<div class="login_box">
					<div class="id_box">
						<span>아이디</span>
						<input type="text" name="userid" value="${userid}">
					</div>
					<div class="pw_box">
						<span>비밀번호</span>
						<input type="password" name="pwd">
					</div>
				</div>
				<div class="join_btn">
					<span>아직 회원이 아니신가요?</span>
					<input type="button" value="회원가입" onclick="location.href='join.do'">
				</div>
				<div class="message_box">
					${message}
				</div>
				<div class="login_btn">
					<input type="submit" value="로그인">
				</div>
			</form>
		</div>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>