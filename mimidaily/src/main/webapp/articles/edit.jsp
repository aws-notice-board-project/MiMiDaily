<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>기사 수정</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/write.css">
	<script src="${pageContext.request.contextPath}/script/write.js"></script>
</head>

<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>

	<h1 class="title">기사 수정</h1>
	<div class="form_cont cont">
		<form name="editFrm" method="post" enctype="multipart/form-data" action="/articles/edit.do"
			onsubmit="return validateForm(this);">

			<!-- 데이터 전달용 -->
			<div class="form_box hide">
				<input type="hidden" name="idx" value="${ dto.idx }"/>
				<input type="hidden" id="members_id" name="members_id" readonly value="${dto.members_id}">
				<input type="hidden" name="prevthumbnails_idx" value="${ dto.thumbnails_idx }" />
				<input type="hidden" name="prevOfile" value="${ dto.ofile }" />
				<input type="hidden" name="prevSfile" value="${ dto.sfile }" />
				<input type="hidden" name="prevfile_path" value="${ dto.file_path }" />
				<input type="hidden" name="prevfile_size" value="${ dto.file_size }" />
				<input type="hidden" name="prevfile_type" value="${ dto.file_type }" />
			</div>

			<div class="form_box">
				<label for="category">카테고리</label>
				<select name="category" id="category">
					<option value="1">여행지</option>
					<option value="2">맛집</option>
				</select>
			</div>

			<div class="form_box">
				<label for="hashtags">해시태그 <small>(예: #여행지 #맛집 #서울_맛집)</small></label>
				<input type="text" id="hashtags" name="hashtags" placeholder="#해시태그를 입력하세요." value="${dto.hashtagString}">
			</div>

			<div class="form_box">
				<label for="title">제목</label>
				<input type="text" id="title" name="title" maxlength="30" value="${ dto.title }" >
			</div>

			<div class="form_box">
				<label for="ofile">이미지 첨부</label>
				<input type="file" id="ofile" name="ofile">
			</div>

			<div class="form_box">
				<label for="content">내용</label>
				<textarea id="content" name="content" rows="8" >${ dto.content }</textarea>
			</div>

			<div class="form_box hide">
				<label for="time_input">작성시간</label>
				<input id="time_input" type="text" name="created_at" readonly>
			</div>

			<div class="form_button">
				<button type="submit">수정 완료</button>
			</div>
		</form>
	</div>

	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>

</html>