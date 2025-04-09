<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>
    <c:choose>
        <c:when test="${article.category == 1}">
            여행 | ${article.title}
        </c:when>
        <c:when test="${article.category == 2}">
            맛집 | ${article.title}
        </c:when>
    </c:choose>
</title>
<script src="https://kit.fontawesome.com/e7c9242ec2.js" crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.css">
<script type="module">
    import { loginAlert, toggleLike } from '/script/view.js';

    window.loginAlert = loginAlert;
    window.toggleLike = toggleLike;
</script>
<script type="module" src="/script/view.js"></script>
</head>
<body>
<jsp:include page="/components/navigation.jsp"></jsp:include>

	<div class="view_container">
		<c:if test="${ sessionScope.loginUser==article.members_id }">
			<div class="ud_btn">
	            <button class="btn" type="button" onclick="location.href='../articles/edit.do?mode=edit&idx=${ param.idx }';">
	                수정하기
	            </button>
	            <button class="btn" type="button" onclick="location.href='../articles/delete.do?mode=delete&idx=${ param.idx }';">
	                삭제하기
	            </button>
	        </div>
		</c:if>
		<div class="view_box cont">
	        <div class="view_top">
	            <h2><span>${ article.idx }</span> ${ article.title }</h2>
	            <div class="view_top_info">
	                <span><b>작성일</b> ${fn:substring(article.created_at, 0, 10)}</span>
	                <span><b>조회수</b> ${ article.visitcnt }</span>
	            </div>
	        </div>
	        <div class="view_bottom">
	            <c:if test="${article.thumbnails_idx != 0}">
		            <div class="news_img">
						<img src="${pageContext.request.contextPath}${article.file_path}${article.sfile}" alt="${article.title} 사진 자료">
		            </div>
				</c:if>

		        <div class="news_context">${ article.content }</div>
				
				<c:if test="${not empty article.hashtags}">
				<h4 class="hide">해시태그</h4>
				<ul class="hashtags">
					<c:forEach items="${ article.hashtags }" var="tags" varStatus="loop">
						<li>#${ tags }</li>
					</c:forEach>
				</ul>
				</c:if>
				
	 	 	    <div class="journalist">
					<h3 class="hide">기자정보</h3>
					<c:choose>
			            <c:when test="${empty writer.profile_idx}">
				              <i class="fa-solid fa-circle-user none_profile"></i>
						</c:when>
			            <c:otherwise>
			            <div class="profile_img">
							<img src="${pageContext.request.contextPath}${writer.file_path}${writer.sfile}" alt="${article.members_id}의 프로필">
			            </div>
			            </c:otherwise>
			        </c:choose>
			        <div class="journalist_info">
						<p><b>${ writer.name }</b> <c:if test="${ writer.role==2 }">기자</c:if></p>
						<a class="writer_email" href="mailto:${writer.email}?subject=${writer.name} 기자님께 제보합니다.&body=제보 이메일 내용을 작성해주세요">
						    <i class="fa-regular fa-envelope"></i> ${writer.email}
						</a>
			        </div>
				</div>
		        	
	        </div>
		</div>
		
		<!-- 사이드 -->
		<aside class="news_right">
			<div class="aside_box">
				<jsp:include page="/components/usercard.jsp"></jsp:include>
				<div class="likes_comments">
					<c:choose>
						<c:when test="${not empty sessionScope.loginUser}">
							<div class="likes cont">
								<c:choose>
									<c:when test="${article.is_liked==true}"><i class="fa-solid fa-heart like" onclick="toggleLike(${article.idx});" style="color:red;"></i></c:when>
									<c:otherwise><i class="fa-regular fa-heart unlike" onclick="toggleLike(${article.idx});"></i></c:otherwise>
								</c:choose>
								<p>좋아요 <span>${article.likes}</span></p>
							</div>
						</c:when>
						<c:otherwise>
							<div class="likes cont">
								<i class="fa-regular fa-heart unlike" onclick="loginAlert()"></i>
								<p>좋아요 <span>${article.likes}</span></p>
							</div>
						</c:otherwise>
					</c:choose>
					<div class="comments cont">
						<i class="fa-solid fa-comment-dots"></i>
						<p>댓글</p>
					</div>
				</div>
				<jsp:include page="/components/viewestNews.jsp"></jsp:include>
			</div>
		</aside>
	</div>

<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>