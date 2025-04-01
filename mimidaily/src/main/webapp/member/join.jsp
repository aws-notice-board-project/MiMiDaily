<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/join.css">
<script src="${pageContext.request.contextPath}/script/join.js"></script>
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
			            <input type="button" id="agree_all" value="전체 동의">
			            전체 동의에는 필수 및 선택 정보에 대한 동의가 포함되어 있으며, 개별적으로 동의를 선택 하실 수 있습니다. 선택 항목에 대한 동의를 거부하시는 경우에도 서비스 이용이 가능합니다.
					</label>
					<label>
						<input type="checkbox" id="agree1">
						동의 1
					</label>
					<input type="button" id="agree_modal_btn1" value="확인 >">
					<label>
						<input type="checkbox" id="agree2">
						동의 2
					</label>
					<input type="button" id="agree_modal_btn2" value="확인 >">
					<label>
						<input type="checkbox" id="agree3">
						동의 3
					</label>
					<input type="button" id="agree_modal_btn3" value="확인 >">
				</div>
				<div class="join_btn">
					<input type="submit" value="가입하기">
				</div>
			</form>
			<!-- 모달 창들 -->
			<div id="agree_modal1" class="modal">
				<div class="modal_content">
					<span class="close" id="close1">&times;</span>
					<div id="agree_content1">로딩 중...</div>
				</div>
			</div>
			<div id="agree_modal2" class="modal">
		        <div class="modal_content">
					<span class="close" id="close2">&times;</span>
					<div id="agree_content2">로딩 중...</div>
		        </div>
			</div>
			<div id="agree_modal3" class="modal">
		        <div class="modal_content">
					<span class="close" id="close3">&times;</span>
					<div id="agree_content3">로딩 중...</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>