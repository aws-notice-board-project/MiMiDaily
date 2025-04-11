<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"% errorPage="error.jsp">
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>searchBar</title>

<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body>
	<form id="search_box" method="get" action="${ actionUrl }">
        <select class="searchfield" name="searchField">
            <option value="title">제목</option>
            <option value="content">내용</option>
        </select>
        <input type="text" id="search" name="searchWord" autocomplete="off" placeholder="검색어를 입력하세요" />
        <button class="search_btn" type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
    </form>
    <script>
        // enter : 검색
        document.getElementById("search").addEventListener("keypress", function(e) {
            if (e.key === "Enter") {
                e.preventDefault(); // 기본 동작 방지
                document.querySelector(".search_btn").click(); // 버튼 클릭 이벤트 발생
            }
        });
    </script>
</body>
</html>