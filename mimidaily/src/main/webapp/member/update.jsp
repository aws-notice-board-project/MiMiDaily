<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회원가입</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">
<script src="${pageContext.request.contextPath}/script/join.js"></script>
</head>
<body id="member">
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<div id="wrap">
		<div id="mimilogo"></div>
		<h1 class="hide">미미일보</h1>
		<h2>정보수정</h2>
		<form action="#" method="post" name="update_form">
	        <div class="member_box">
				<div id="id">
					<input type="text" name="id" placeholder="아이디" disabled>
				</div>
				<div id="pw">
					<input type="password" name="pw" placeholder="비밀번호">
					<p class="error hidden"></p>
				</div>
				<div id="rpw">
					<input type="password" name="rpw" placeholder="비밀번호 확인">
					<p class="error hidden"></p>
				</div>
				<div id="name">
					<input type="text" name="name" placeholder="이름">
				</div>
				<div id="email">
					<input type="email" name="email" placeholder="메일 주소">
					<p class="error hidden"></p>
				</div>
				<div id="tel">
					<input type="text" name="tel" placeholder="연락처">
					<p class="error hidden"></p>
		        </div>
		        <!-- <div id="birth_gender">
					<input type="text" name="birth" placeholder="주민번호 앞자리" maxlength="6">-<input type="text" name="gender" maxlength="1"> ■ ■ ■ ■ ■ ■
					<p class="error hidden"></p>
		        </div> -->
				<!-- <label>
					<input type="checkbox" id="agree3">
					마케팅 활용 및 프로모션 이용 동의
					<input type="button" id="agree_modal_btn3" value="내용 확인 >">
	        	</label> -->
	        </div>
			<div class="member_btn">
				<input type="submit" value="저장하기" data-success="${success_msg}">
			</div>
		</form>
		<div id="agree_modal3" class="modal">
			<span class="close" id="close3">&times;</span>
	        <div class="modal_content">
	        	로딩 중...
			</div>
		</div>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>