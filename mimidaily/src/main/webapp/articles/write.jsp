<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/write.css">

	<script>
		const now = new Date();
		const formatted = now.getFullYear() + "-"
		  + String(now.getMonth() + 1).padStart(2, '0') + "-"
		  + String(now.getDate()).padStart(2, '0') + " "
		  + String(now.getHours()).padStart(2, '0') + ":"
		  + String(now.getMinutes()).padStart(2, '0') + ":"
		  + String(now.getSeconds()).padStart(2, '0');
	  
		document.getElementById('created_at').value = formatted;
	  </script>
</head>
<body>
	<jsp:include page="/components/navigation.jsp"></jsp:include>
<h2>파일 첨부형 게시판 - 글쓰기(Write)</h2>
<!-- <form name="writeFrm" method="post" enctype="multipart/form-data"
	action="/articles/write.do" onsubmit="return validateForm(this);"> -->
	<form action="/articles/write.do" method="post">
	<table border="1" width="90%">
		<tr>
			<td>작성자</td>
			<td><input type="text" name="members_id" style="width: 150px;" readonly value="${sessionScope.loginUser}"/></td>
		</tr>
		<tr>
			<td>제목</td>
			<td><input type="text" name="title"></td>
		</tr>
		<tr>
			<td>내용</td>
			<td><textarea name="content" style="width: 90%; height: 100px;"></textarea>
			</td>
		</tr>
		<tr>
			<td>카테고리</td>
			<td><input type="number" name="category"  /></td>
		</tr>
		<tr>
			<td>작성시간</td>
			<td><input id="timeInput" type="text" name="created_at" readonly /></td>
		</tr>
		<!-- <tr>
			<td>썸네일</td>
			<td><input type="text" name="thumnails_idx" readonly value=""/></td>
		</tr> -->
		<tr>
			<td colspan="2" align="center">
				<button type="submit">작성 완료</button>
				<button type="reset">RESET</button>
				<button type="button" onclick="location.href='../mvcboard/list.do';">
					목록 바로가기</button>
			</td>
		</tr>
	</table>
</form>
<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>