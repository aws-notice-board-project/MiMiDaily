<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="/components/error.jsp" %>
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
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.css">
<script type="module" src="/script/view.js" defer></script>
</head>
<body>
	<div id="deleteModal" class="modal">
		<div class="modal_content cont">
			<span id="close_btn">&times;</span>
			<p>게시글을 삭제하시겠습니까?</p>
			<div class="btn_box">
				<button id="yes_btn">예</button>
				<button id="no_btn">아니오</button>
			</div>
			<form id="deleteForm" action="${pageContext.request.contextPath}/articles/delete.do" method="post">
				<input type="hidden" name="idx" value="${article.idx}" />
				<input type="hidden" name="redirectURL" value="${redirectURL}" />
			</form>
		</div>
	</div>

<jsp:include page="/components/navigation.jsp"></jsp:include>

	<div class="view_container">
		<c:if test="${ sessionScope.loginUser==article.members_id || sessionScope.userRole==0 }">
			<div class="ud_btn">
	            <button class="btn" type="button" onclick="location.href='../articles/edit.do?mode=edit&idx=${ param.idx }&thumb_idx=${ article.thumbnails_idx }&redirectURL=${ redirectURL }';">
	                수정하기
	            </button>
	            <button class="btn" type="button" onclick="deleteArticle()">
	                삭제하기
	            </button>
	        </div>
		</c:if>
		<div class="view_detail">
			<div class="view_box cont">
		        <div class="view_top">
		            <h2><span id="article_idx">${ article.idx }</span> ${ article.title }</h2>
		            <div class="view_top_info">
		                <span><b>작성일</b> ${ article.formattedDate }</span>
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
				            <c:when test="${writer.profile_idx==0}">
					              <i class="fa-solid fa-circle-user none_profile"></i>
							</c:when>
				            <c:otherwise>
				            <div class="profile_img">
								<img src="${pageContext.request.contextPath}${writer.file_path}/${writer.sfile}" alt="${article.members_id}의 프로필">
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
					<!-- 댓글 -->
					<div class="comments">
						<h3>댓글 <span class="comment_cnt">${ commentCnt }</span></h3>
						<div class="comments_form">
				    		<c:choose>
				        		<c:when test="${empty sessionScope.loginUser}">
				        			로그인 후 댓글을 작성할 수 있습니다.
				        		</c:when>
				        		<c:otherwise>
				     			<!-- 댓글 작성 -->
				      		<div class="input_comment">
				       		<c:choose>
					            <c:when test="${member.profile_idx == 0}">
						             <i class="fa-solid fa-circle-user none_profile"></i>
								</c:when>
					            <c:otherwise>
									<div class="profile_img">
										<img src="${pageContext.request.contextPath}${member.file_path}/${member.sfile}" alt="${sessionScope.loginUser}의 썸네일">
						            </div>
					            </c:otherwise>
					        </c:choose>
				       		<textarea rows="4" cols="50" id="comment" autocomplete="off"></textarea>
				      		</div>
				      		<div class="comt_cnt_box">
				      			<span class="comt_cnt">0</span><span>/500</span>
				       		<button class="comment_btn btn" type="button" onclick="insertComment('${sessionScope.loginUser}',${article.idx})">댓글 작성</button>
				      		</div>
				        		</c:otherwise>
				        	</c:choose>
				   		</div>
					</div>
					<div class="comments_container">
						<jsp:include page="/components/comments.jsp"></jsp:include>
					</div>
					<div class="more_btn">
		     			<button class="btn" onclick="loadMoreComments(${article.idx}, ${commentCnt})">더보기</button>
		     		</div>
		        </div>
			</div>
			<div class="comts_likes">
				<aside class="news_right">
					<div class="aside_box">
						<c:if test="${empty sessionScope.loginUser}">
							<div class="login_box cont" onclick="location.href='/login.do'">
								<i class="fa-solid fa-right-to-bracket"></i>
								<p>로그인</p>
							</div>
						</c:if>
						<c:choose>
							<c:when test="${not empty sessionScope.loginUser}">
								<div class="likes cont" onclick="toggleLike(${article.idx});">
									<i class="fa-heart" style="color:red;"></i>
									<p><span class="like_txt">좋아요</span> <span class="like_cnt">${article.likes}</span></p>
								</div>
							</c:when>
							<c:otherwise>
								<div class="likes cont" onclick="loginAlert()">
									<i class="fa-regular fa-heart unlike"></i>
									<p><span class="like_txt">좋아요</span> <span class="like_cnt">${article.likes}</span></p>
								</div>
							</c:otherwise>
						</c:choose>
						<div class="comments cont">
							<i class="fa-solid fa-comment-dots"></i>
							<p>댓글</p>
						</div>
					</div>
				</aside>
			</div>
		</div>
		
		<!-- 사이드 -->
		<aside class="news_right md">
			<div class="aside_box">
				<jsp:include page="/components/usercard.jsp"></jsp:include>
				<div class="likes_comments">
					<c:choose>
						<c:when test="${not empty sessionScope.loginUser}">
							<div class="likes cont" onclick="toggleLike(${article.idx});">
									<i class="fa-heart" style="color:red;"></i>
								<p><span class="like_cnt">${article.likes}</span><span class="like_txt">좋아요</span></p>
							</div>
						</c:when>
						<c:otherwise>
							<div class="likes cont">
								<i class="fa-regular fa-heart unlike" onclick="loginAlert()"></i>
								<p><span class="like_cnt">${article.likes}</span><span class="like_txt">좋아요</span></p>
							</div>
						</c:otherwise>
					</c:choose>
					<div class="comments cont">
						<i class="fa-solid fa-comment-dots"></i>
						<p>댓글</p>
					</div>
				</div>
				<jsp:include page="/components/viewest.jsp"></jsp:include>
			</div>
		</aside>
	</div>

<jsp:include page="/components/footer.jsp"></jsp:include>

<script type="module" defer>
    import { insertComment, loginAlert, toggleLike , deleteArticle, loadMoreComments } from '/script/view.js';
	window.insertComment = insertComment;
    window.loginAlert = loginAlert;
    window.toggleLike = toggleLike;
	window.deleteArticle = deleteArticle;
	window.loadMoreComments = loadMoreComments;
</script>
<script>
	const isLiked = ${article.is_liked};
	const isLogin = ${not empty sessionScope.loginUser};
	if(isLogin){ // 로그인일때만 상태 확인
		if(isLiked){
			$('.view_container .likes i').addClass('fa-solid');
			$('.view_container .likes i').removeClass('fa-regular');
			$('.view_container .likes i').css('color', 'red');
		}else{
			$('.view_container .likes i').addClass('fa-regular');
			$('.view_container .likes i').removeClass('fa-solid');
			$('.view_container .likes i').css('color', '#594543');
		}
	}
	
	// 페이지 로드 시, 더보기 버튼
	const cnt = ${commentCnt}
	if(cnt<11){$('.view_bottom .more_btn').css('display','none');}
</script>
</body>
</html>