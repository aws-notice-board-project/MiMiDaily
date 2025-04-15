<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회원가입</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">
<script src="${pageContext.request.contextPath}/script/update.js"></script>
</head>
<body id="update">
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<div id="wrap">
		<div id="mimilogo"></div>
		<h1 class="hide">미미일보</h1>
		<h2>정보수정</h2>
		<form action="update.do" method="get" name="precheck" class="hidden">
			<div id="prepw">
				<input type="password" name="prepw" placeholder="비밀번호 입력">
				<p class="error hidden"></p>
			</div>
			<div class="member_btn">
				<input type="submit" value="확인">
			</div>
		</form>
		<form action="update.do" method="post" enctype="multipart/form-data" onsubmit="return validateForm(this);" name="member_form">
			<div class="member_box hide">
				<input type="hidden" name="prevprofile_idx" value="${ member.profile_idx }" />
				<input type="hidden" name="prevOfile" value="${ member.ofile }" />
				<input type="hidden" name="prevSfile" value="${ member.sfile }" />
				<input type="hidden" name="prevfile_path" value="${ member.file_path }" />
				<input type="hidden" name="prevfile_size" value="${ member.file_size }" />
				<input type="hidden" name="prevfile_type" value="${ member.file_type }" />
			</div>
			<div class="member_box" id="profile">
			<c:choose>
            <c:when test="${member.profile_idx == 0}">
          		<i class="fa-solid fa-circle-user none_profile"></i>
			</c:when>
            <c:otherwise>
				<img src="${pageContext.request.contextPath}${member.file_path}/${member.sfile}" alt="${member.id}의 프로필">
            </c:otherwise>
        	</c:choose>
			</div>
	        <div class="member_box">
	        	<label id="profile_upload">
	        		<span id="upload_label_text">프로필 선택</span>
					<input type="file" name="ofile" accept="image/*" class="file_upload_input">
				</label>
				<div class="readonly">
					<input type="text" name="id" value="${sessionScope.loginUser}" readonly>
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
					<input type="text" name="name" placeholder="이름" value="${member.name}">
				</div>
				<div id="email">
					<input type="email" name="email" placeholder="메일 주소" value="${member.email}">
					<p class="error hidden"></p>
				</div>
				<div id="tel">
					<input type="text" name="tel" placeholder="연락처" value="${member.tel}">
					<p class="error hidden"></p>
		        </div>
		        <div id="birth_gender">
					<input type="text" name="birth" placeholder="주민등록번호" maxlength="6" value="${member.birth}">
					-
					<input type="text" name="gender_code" maxlength="1">
					<div class="xnumber"></div>
					<div class="xnumber"></div>
					<div class="xnumber"></div>
					<div class="xnumber"></div>
					<div class="xnumber"></div>
					<div class="xnumber"></div>
					<p class="error hidden"></p>
					<input type="hidden" name="gender" value="${member.gender}">
		        </div>
				<label>
					<input type="checkbox" id="marketing_agree" checked>
					마케팅 활용 및 프로모션 이용 동의
					<input type="button" id="marketing_agree_modal_btn" value="내용 확인 >">
					<input type="hidden" name="marketing">
	        	</label>
	        </div>
			<div class="member_btn">
				<input type="submit" value="저장하기" data-success="${fail_msg}">
			</div>
		</form>
		<div id="marketing_agree_modal" class="modal">
			<span class="close" id="close">&times;</span>
	        <div class="modal_content">
	        	로딩 중...
			</div>
		</div>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>