<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/join.css">
<script type="module" src="../script/join.js"></script>
</head>
<body id="join">
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<div id="wrap">
		<div class="cont">
			<div id="logo"></div>
			<h1 class="hide">미미일보</h1>
			<h2>회원가입</h2>
			<form action="join.do" method="post" name="join_form">
		        <div class="join_box">
					<div id="id">
						<input type="text" name="id" placeholder="아이디">
						<input type="button" value="중복 확인">
						<p class="error hidden"></p>
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
						<!-- <p class="error hidden"></p> -->
					</div>
					<div id="email">
						<input type="email" name="email" placeholder="메일 주소">
						<p class="error hidden"></p>
					</div>
		        </div>
		        <div class="join_box">
					<div id="tel">
						<input type="text" name="tel" placeholder="연락처">
						<p class="error hidden"></p>
			        </div>
			        <div id="birth_gender">
						<input type="text" name="birth" placeholder="주민번호 앞자리" maxlength="6">-<input type="text" name="gender" maxlength="1">■■■■■■
						<p class="error hidden"></p>
			        </div>
		        </div>
		        <div class="join_box">
					<div id="code">
			            <input type="text" name="code" placeholder="기자 인증 코드">
			            <input type="button" value="인증">
			            <p class="error hidden"></p>
					</div>
		        </div>
				<div class="join_box">
					<label>
		            <input type="checkbox" id="agree" onclick="toggleSubmitButton()">
		            동의합니다.
					</label>
					<label>
		            <input type="button" class="modal_btn" onclick="openModal()">
		            > 
					</label>
		        </div>
		        <!-- 모달 창 -->
				<div id="myModal" class="modal">
					<div class="modal-content">
						<span class="close" onclick="closeModal()">&times;</span>
		            	<div id="modalContent1">로딩 중...</div> <!-- AJAX로 불러올 내용 -->
					</div>
		        </div>
		        <div class="join_btn">
					<input type="submit" value="가입하기">
				</div>
			</form>
		</div>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>