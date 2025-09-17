<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
   	<div class="comment">
   		<div class="comments_list">
   		<c:choose>
   			<c:when test="${ not empty commentsList }">
     		<c:forEach var="com" items="${ commentsList }">
   			<jsp:include page="/components/modal.jsp"></jsp:include>
   			<div class="comment_box" data-comment-idx="${ com.idx }">
     			<div class="coment_cont">
	        		<c:choose>
	        			<c:when test="${com.profiles.profile_idx == 0 || com.profiles.profile_idx == null}">
			        		<div class="profile_img">
			        			<i class="fa-solid fa-circle-user none_profile"></i>
			        		</div>
	        			</c:when>
                                        <c:otherwise>
                                                <div class="profile_img images">
                                                                <c:set var="profileUrl" value="${com.profiles.imageUrl}" />
                                                                <c:choose>
                                                                        <c:when test="${empty profileUrl}">
                                                                                <img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="${com.members_id}의 썸네일">
                                                                        </c:when>
                                                                        <c:when test="${fn:startsWith(profileUrl, 'http://') || fn:startsWith(profileUrl, 'https://')}">
                                                                                <img src="${profileUrl}" alt="${com.members_id}의 썸네일">
                                                                        </c:when>
                                                                        <c:when test="${fn:startsWith(profileUrl, '/')}">
                                                                                <img src="${pageContext.request.contextPath}${profileUrl}" alt="${com.members_id}의 썸네일">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                                <img src="${pageContext.request.contextPath}/${profileUrl}" alt="${com.members_id}의 썸네일">
                                                                        </c:otherwise>
                                                                </c:choose>
                                            </div>
                                        </c:otherwise>
	        		</c:choose>
	        		<div class="content_box">
		        		<div class="comt_context">
		        			<p><strong>${ com.members_id }</strong><c:if test="${ com.is_updated }"><span class="is_updated">(수정됨)</span></c:if></p>
		        			<p class="comt_date">${ com.is_sameday? com.timeAgo:com.formattedDate }</p>
		        		</div>
	        			<p class="comt_content">${ com.context }</p>
	        		</div>
      			</div>
      			<c:if test="${ sessionScope.loginUser==com.members_id || sessionScope.userRole==0 }">
       			<div class="comt_btn">
       				<button onclick="updateComment(${com.idx})">수정</button>
       				<button onclick=" deleteComment(${com.idx})">삭제</button>
       			</div>
      			</c:if>
     		</div>
     		</c:forEach>
   			</c:when>
   			<c:otherwise>
   				<div class="no_comt">
   					댓글이 없습니다.
   				</div>
   			</c:otherwise>
   		</c:choose>
   		</div>
   	</div>
<script type="module" defer>
    import { deleteComment, updateComment, confirmUpdate, cancelUpdate, loadMoreComments } from '/script/view.js';
	window.deleteComment = deleteComment;
	window.updateComment = updateComment;
	window.confirmUpdate = confirmUpdate;
	window.cancelUpdate = cancelUpdate;

	// 초기 높이 설정
    let contentHeight = $('.view_box').height();
    $('aside.news_right').css('height', contentHeight+200);
</script>