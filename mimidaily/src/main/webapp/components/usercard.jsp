<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.userbox{width: 216px; margin: 1rem 0 1rem 1rem ; padding: 1rem;}
	.profile{display: flex; align-items: center; margin-bottom: 1rem;}
	.profile i{font-size: 3rem; margin-right: 1rem;}
	.profile .name{font-weight: bold; font-size: 1.2rem; color: #4A4A4A;}
	.profile .date{font-style: 12px; color: #8C7B7B;}
	.info div{display: flex; justify-content: space-between; margin-bottom: 0.5rem;}
	.info span{color: #8C7B7B;}
	.info button.myprofile {background-color: #594543;}
	.info button.write {background-color: #8C7B7B;}
	.info button.btn {border-radius: 4px; color: #FBF9F9;padding: 0.5rem;}
	.info .profile_btn{width: 100%; justify-content: space-around;margin-top: 1rem;}
</style>
</head>
<body> 
	<div class="userbox cont">
		<c:if test="${not empty sessionScope.loginUser}">
			<div class="profile">
				<!-- 프로필 임시 -->
				<i class="fa-solid fa-circle-user"></i>
				<div>
					<p class="name">${sessionScope.loginUser != null ? sessionScope.loginUser : "게스트"}</p>
					<p class="date">${ memberInfo.createdAt } 가입</p>
				</div>
			</div>
			<div class="info">
				<div><b>방문</b><span>${visitCnt}회</span></div>
				<div><b>게시글</b><span>${ memberInfo.articleCount }개</span></div>
				<div><b>댓글</b><span>${ memberInfo.commentCount }개</span></div>
				<div class="profile_btn">
					<c:if test="${ userRole==0 || userRole==2 }">
						<button class="write btn">글쓰기</button>
					</c:if>
					<button class="myprofile btn">나의 정보</button>
				</div>
			</div>
		</c:if>
	</div>
</body>
</html>