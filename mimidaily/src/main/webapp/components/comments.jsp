<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="comments">
   	<h3>댓글 <span class="comment_cnt">${ commentCnt }</span></h3>
   	<div class="comment">
   		<div class="comments_form">
    		<c:choose>
        		<c:when test="${empty sessionScope.loginUser}">
        			로그인 후 댓글을 작성할 수 있습니다.
        		</c:when>
        		<c:otherwise>
     			<!-- 댓글 작성 -->
      		<div class="input_comment">
       		<c:choose>
	            <c:when test="${member.profile_idx == 0||member.profile_idx == null}">
		             <i class="fa-solid fa-circle-user none_profile"></i>
				</c:when>
	            <c:otherwise>
	            	이미지 있음
		            <%-- <div class="profile_img">
						<img src="${pageContext.request.contextPath}${member.file_path}${member.sfile}" alt="${sessionScope.loginUser}의 썸네일">
		            </div> --%>
	            </c:otherwise>
	        </c:choose>
       		<textarea rows="4" cols="50" id="comment" autocomplete="off"></textarea>
      		</div>
      		<div class="comt_cnt_box">
      			<span class="comt_cnt">0</span><span>/500</span>
       		<button class="comment_btn btn" type="button" onclick="insertComment('${member.id}',${articleIdx})">댓글 작성</button>
      		</div>
        		</c:otherwise>
        	</c:choose>
   		</div>
   		<div class="comments_list">
   		<c:choose>
   			<c:when test="${ not empty commentsList }">
     		<c:forEach var="com" items="${ commentsList }">
     			<jsp:include page="/components/modal.jsp"></jsp:include>
     			<div class="comment_box" data-comment-idx="${ com.idx }">
     				<div class="coment_cont">
        		<div class="profile_img">
        		<c:choose>
        			<c:when test="${ not empty commentsList }">
        			<%-- <c:when test="${com.profile_idx == 0||com.profile_idx == null}"> --%>
	        			<i class="fa-solid fa-circle-user none_profile"></i>
        			</c:when>
        			<c:otherwise>
        				이미지 있음
        				<%-- <div class="profile_img">
							<img src="${pageContext.request.contextPath}${com.file_path}${com.sfile}" alt="${com.members_id}의 썸네일">
			            </div> --%>
        			</c:otherwise>
        		</c:choose>
        		</div>
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
   </div>
<script type="module" defer>
    import { insertComment, deleteComment, updateComment, confirmUpdate, cancelUpdate } from '/script/view.js';
	window.insertComment = insertComment;
	window.deleteComment = deleteComment;
	window.updateComment = updateComment;
	window.confirmUpdate = confirmUpdate;
	window.cancelUpdate = cancelUpdate;

	// 초기 높이 설정
    let contentHeight = $('.view_box').height();
    $('aside.news_right').css('height', contentHeight+200);
</script>