<%@ page  isErrorPage="true" language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>에러 페이지</title>
	<link rel="stylesheet" type="text/css" href="/css/main.css">
</head>
<body id="error_page">
	<div class="error_cont">
		<h1>서버 오류</h1>
		<p>서버에서 처리 중 오류가 발생했습니다.</p>
		<a href="${pageContext.request.contextPath}/main.do">홈으로</a>
	</div>
</body>
</html>