<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>기사 수정</title>
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

	<h1 class="title">기사 수정</h1>
	<div class="form_cont cont">
		<form name="editFrm" method="post" enctype="multipart/form-data" action="/articles/edit.do"
			onsubmit="return validateForm(this);">

			<!-- 작성자 -->
			<div class="form_box hide">
				<label for="members_id">작성자</label>
				<input type="hidden" name="idx" value="${ dto.idx }"/>
				<input type="hidden" id="members_id" name="members_id" readonly value="${dto.members_id}">
				<input type="hidden" name="prevthumbnails_idx" value="${ dto.thumbnails_idx }" />
				<input type="hidden" name="prevOfile" value="${ dto.ofile }" />
				<input type="hidden" name="prevSfile" value="${ dto.sfile }" />
				<input type="hidden" name="prevfile_path" value="${ dto.file_path }" />
				<input type="hidden" name="prevfile_size" value="${ dto.file_size }" />
				<input type="hidden" name="prevfile_type" value="${ dto.file_type }" />
			</div>


			<!-- 카테고리 선택 -->
			<div class="form_box">
				<label for="category">카테고리</label>
				<select name="category" id="category">
					<option value="1">여행지</option>
					<option value="2">맛집</option>
				</select>
			</div>

			<!-- 해시태그 -->
			<div class="form_box">
				<label for="hashtags">해시태그 <small>(예: #여행지 #맛집 #서울_맛집)</small></label>
				<input type="text" id="hashtags" name="hashtags" placeholder="#해시태그를 입력하세요.">
			</div>

			<!-- 제목 -->
			<div class="form_box">
				<label for="title">제목</label>
				<input type="text" id="title" name="title" maxlength="30" value="${ dto.title }" >
			</div>

			<!-- 첨부 파일 -->
			<div class="form_box">
				<label for="ofile">이미지 첨부</label>
				<input type="file" id="ofile" name="ofile">
			</div>

			<!-- 내용 -->
			<div class="form_box">
				<label for="content">내용</label>
				<textarea id="content" name="content" rows="8" >${ dto.content }</textarea>
			</div>

			<!-- 작성시간 (숨김 처리) -->
			<div class="form_box hide">
				<label for="timeInput">작성시간</label>
				<input id="timeInput" type="text" name="created_at" readonly>
			</div>

			<!-- 버튼 그룹 -->
			<div class="form_button">
				<button type="submit">수정 완료</button>
				<!-- <button type="reset">RESET</button> -->
				<!-- <button type="button" onclick="location.href='../mvcboard/list.do';">목록 바로가기</button> -->
			</div>

		</form>
	</div>

	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>

</html>