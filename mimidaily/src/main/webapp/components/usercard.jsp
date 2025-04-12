<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	.userbox{width: 216px; margin: 1rem 0.5rem 1rem 0.5rem ; padding: 1rem; justify-content: center;display: flex;flex-direction: column;}
	/* 로그인 O */
	.profile{display: flex; align-items: center; margin-bottom: 1rem;}
	.profile i{font-size: 38px; margin-right: 1rem;}
	.profile .name{font-weight: bold; font-size: 1.2rem; color: #4A4A4A;}
	.profile .date{font-size: 12px; color: #8C7B7B;}
	.info div{display: flex; justify-content: space-between; margin-bottom: 2px;}
	.info span{color: #8C7B7B;}
	.info .btn.myprofile {background-color: #594543;}
	.info .btn.write {background-color: #8C7B7B;}
	.info .btn {border-radius: 4px; color: #FBF9F9;padding: 0.5rem 0.7rem;font-size:16px;}
	.info .profile_btn{width: 100%; justify-content: space-around;}
	.info .logout_box{display:flex;justify-content:center;margin:0;}
	.info .logout {display:inline-block;margin:auto; color:#8C7B7B; font-size:16px; border-bottom: 1px solid;}
	/* 로그인X */
	.login_box {display:flex; flex-direction:column; justify-content:center;align-items:center;height:100%;}
	.login_box p{text-align:center; font-size: 14px;margin: 0;}
	.login_box p b{font-size:14px;}
	.login_box .login{color: #FBF9F9; font-size: 24px; background-color: #8C7B7B; border-radius: 4px;width: 190px;text-align: center;padding: 0.3rem;margin: 1rem auto;}
	.login_box .register{font-weight: 500;color: #8C7B7B;padding-bottom: 1px;border-bottom: 1px solid;}
</style>
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
			
			<c:if test="${ userRole==0 || userRole==2 }">
				<div><b>게시글</b><span>${ memberInfo.articleCount }<c:if test="${ empty memberInfo.articleCount }">0</c:if>개</span></div>
			</c:if>
			<div><b>댓글</b><span>${ memberInfo.commentCount }<c:if test="${ empty memberInfo.articleCount }">0</c:if>개</span></div>
			<div class="profile_btn">
				<c:if test="${ userRole==0 || userRole==2 }">
					<a class="write btn" href="/articles/write.do">기사 작성</a>
				</c:if>
				<a class="myprofile btn" href="/update.do">나의 정보</a>
			</div>
			<div class="logout_box"><a class="logout" href="/logout.do">로그아웃</a></div>
		</div>
	</c:if>
	<c:if test="${ empty sessionScope.loginUser }">
		<div class="login_box">
			<p><b>미미일보</b>가<br/>당신의 여정에 동행합니다.</p>
			<a class="login btn" href="${pageContext.request.contextPath}/login.do">로그인</a>
			<a class="register" href="${pageContext.request.contextPath}/join.do">회원가입</a>
		</div>
	</c:if>
</div>