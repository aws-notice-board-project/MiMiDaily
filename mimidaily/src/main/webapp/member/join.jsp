<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"% errorPage="error.jsp">
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
<body id="join">
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<div id="wrap">
		<div id="mimilogo"></div>
		<h1 class="hide">미미일보</h1>
		<h2>회원가입</h2>
		<form action="join.do" method="get" name="precheck" class="hidden">
			<label>
				<input type="checkbox" value="reporter" name="role" class="hidden">
				<img src="${pageContext.request.contextPath}/media/images/user.png" id="unchecked" alt="일반회원">
				<img src="${pageContext.request.contextPath}/media/images/reporter.png" id="checked" alt="기자" class="hidden">
			</label>
			<div class="member_btn">
				<input type="submit" value="일반 회원 가입">
			</div>
		</form>
		<form action="join.do" method="post" autocomplete="off" name="member_form">
	        <div class="member_box">
				<div id="id">
					<input type="text" name="id" placeholder="아이디">
					<input type="button" value="중복 확인" id="id_check">
					<input type="button" value="사  용" id="id_use" class="hidden">
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
				</div>
				<div id="email">
					<input type="email" name="email" placeholder="메일 주소">
					<p class="error hidden"></p>
				</div>
	        </div>
	        <div class="member_box">
				<div id="tel">
					<input type="text" name="tel" placeholder="연락처">
					<p class="error hidden"></p>
		        </div>
		        <div id="birth_gender">
					<input type="text" name="birth" placeholder="주민등록번호 앞자리" maxlength="6">-<input type="text" name="gender_code" maxlength="1"> ■ ■ ■ ■ ■ ■
					<p class="error hidden"></p>
					<input type="hidden" name="gender">
		        </div>
	        </div>
	        <div class="member_box">
				<div id="code" data-job="${job}">
					<input type="text" name="code" placeholder="기자 인증 코드">
					<input type="button" value="인  증" id="code_check">
					<input type="hidden" name="role">
				</div>
			</div>
			<div class="member_box">
				<label>
		            <input type="button" name="agree_all" value="전체 동의"><br>
		            전체 동의에는 필수 및 선택 정보에 대한 동의가 포함되어 있으며, 개별적으로 동의를 선택 하실 수 있습니다. 선택 항목에 대한 동의를 거부하시는 경우에도 서비스 이용이 가능합니다.
				</label>
				<label>
					<input type="checkbox" name="agree1">
					이용약관 동의
					<input type="button" id="agree_modal_btn1" value="내용 확인 >">
				</label>
				<label>
					<input type="checkbox" name="agree2">
					개인정보 수집 및 이용 동의
					<input type="button" id="agree_modal_btn2" value="내용 확인 >">
				</label>
				<label>
					<input type="checkbox" name="agree3" checked>
					마케팅 활용 및 프로모션 이용 동의
					<input type="button" id="agree_modal_btn3" value="내용 확인 >">
					<input type="hidden" name="marketing">
				</label>
			</div>
			<div class="member_btn">
				<input type="submit" value="가입하기" data-success="${success_msg}">
			</div>
		</form>
		<!-- 모달 창들 -->
		<div id="agree_modal1" class="modal">
			<span class="close" id="close1">&times;</span>
			<div class="modal_content">
	        	로딩 중...
			</div>
		</div>
		<div id="agree_modal2" class="modal">
			<span class="close" id="close2">&times;</span>
	        <div class="modal_content">
	        	로딩 중...
			</div>
		</div>
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