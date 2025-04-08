<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>게시글 작성</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/write.css">

	<script>
		document.addEventListener("DOMContentLoaded", function () {
			// 작성시간 자동 입력 (작성시간 필드는 필요에 따라 hidden 처리)
			const now = new Date();
			const formatted = now.getFullYear() + "-"
				+ String(now.getMonth() + 1).padStart(2, '0') + "-"
				+ String(now.getDate()).padStart(2, '0') + " "
				+ String(now.getHours()).padStart(2, '0') + ":"
				+ String(now.getMinutes()).padStart(2, '0') + ":"
				+ String(now.getSeconds()).padStart(2, '0');
			document.getElementById('timeInput').value = formatted;
		});
	</script>
</head>

<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>

	<div class="form-container">
		<h1 class="title">기사 쓰기</h1>
		<form name="writeFrm" method="post" enctype="multipart/form-data" action="/articles/write.do"
			onsubmit="return validateForm(this);">

			<!-- 작성자 -->
			<div class="form-group hide">
				<label for="members_id">작성자</label>
				<input type="text" id="members_id" name="members_id" readonly value="${sessionScope.loginUser}">
			</div>


			<!-- 카테고리 선택 -->
			<div class="form-group">
				<label for="category">카테고리</label>
				<select name="category" id="category">
					<option value="1">여행지</option>
					<option value="2">맛집</option>
				</select>
			</div>

			<!-- 제목 -->
			<div class="form-group">
				<label for="title">제목</label>
				<input type="text" id="title" name="title" maxlength="30" placeholder="제목을 입력하세요.">
			</div>

			<!-- 첨부 파일 -->
			<div class="form-group">
				<label for="ofile">이미지 첨부</label>
				<input type="file" id="ofile" name="ofile">
			</div>

			<!-- 내용 -->
			<div class="form-group">
				<label for="content">내용</label>
				<textarea id="content" name="content" rows="6" placeholder="내용을 입력하세요."></textarea>
			</div>
			<!-- 작성시간 (숨김 처리) -->
			<div class="form-group hide">
				<label for="timeInput">작성시간</label>
				<input id="timeInput" type="text" name="created_at" readonly>
			</div>

			<!-- 해시태그 -->
			<div class="form-group">
				<label for="hashtags">해시태그 <small>(예: #여행지 #맛집 #서울_맛집)</small></label>
				<input type="text" id="hashtags" name="hashtags" placeholder="#해시태그를 입력하세요.">
			</div>

			<!-- 버튼 그룹 -->
			<div class="form-actions">
				<button type="submit">작성 완료</button>
				<!-- <button type="reset">RESET</button> -->
				<!-- <button type="button" onclick="location.href='../mvcboard/list.do';">목록 바로가기</button> -->
			</div>

		</form>
	</div>

	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>

</html>