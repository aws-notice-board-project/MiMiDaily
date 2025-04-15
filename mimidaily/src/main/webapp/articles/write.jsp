<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="/components/error.jsp" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>기사 작성</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/write.css">
	<script src="${pageContext.request.contextPath}/script/write.js"></script>
</head>

<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>

	<h1 class="title">기사 쓰기</h1>
	<div class="form_cont cont">
		<form name="writeFrm" method="post" enctype="multipart/form-data" action="/articles/write.do"
			onsubmit="return validateForm(this);">

			<div class="form_box hide">
				<label for="members_id">작성자</label>
				<input type="text" id="members_id" name="members_id" readonly value="${sessionScope.loginUser}">
			</div>

			<div class="form_box">
				<label for="category" class="category_label">카테고리</label>
				<select name="category" id="category">
					<option value="1">여행지</option>
					<option value="2">맛집</option>
				</select>
			</div>

			<div class="form_box">
				<label for="hashtags"><span class="hash_label">해시태그</span></label>
				<input type="text" id="hashtags" name="hashtags" placeholder="#해시태그를 입력하세요.">
				<p><small>(예: #여행지 #맛집 #서울_맛집)</small></p>
			</div>

			<div class="form_box">
				<label for="title">제목</label>
				<input type="text" id="title" name="title" maxlength="30" placeholder="제목을 입력하세요." required>
			</div>

			<div class="form_box">
				<label for="ofile">이미지 첨부</label>
				<input type="file" id="ofile" name="ofile" accept="image/*">
			</div>

			<div class="form_box">
				<label for="content">내용</label>
				<textarea id="content" name="content" rows="8" placeholder="내용을 입력하세요." required></textarea>
			</div>

			<div class="form_box hide">
				<label for="time_input">작성시간</label>
				<input id="time_input" type="text" name="created_at" readonly>
			</div>

			<div class="form_button">
				<button type="submit">작성 완료</button>
			</div>

		</form>
	</div>

	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>

</html>