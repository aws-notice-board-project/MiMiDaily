<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${ dto.title }</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/view.css">
</head>
<body>
<jsp:include page="/components/navigation.jsp"></jsp:include>

	<div class="view_container">
		<div class="view_box cont">
	        <div class="view_top">
	            <h2>${ dto.idx }. ${ dto.title }</h2>
	            <div class="view_top_info">
	                <span>작성자: ${ dto.members_id }</span>
	                <span>작성일: ${fn:substring(dto.created_at, 0, 10)}</span>
	                <span>조회수: ${ dto.visitcnt }</span>
	            </div>
	        </div>
	        <div class="view_bottom">
		        <div>${ dto.content }</div>
	 	 	    <div>
					<h3 class="hide">기자정보</h3>
					${ dto.members_id }
				</div>
		        	
	        </div>
	        <div>
	            <button class="btn" type="button" onclick="location.href='../mvcboard/pass.do?mode=edit&idx=${ param.idx }';">
	                수정하기
	            </button>
	            <button class="btn" type="button" onclick="location.href='../mvcboard/pass.do?mode=delete&idx=${ param.idx }';">
	                삭제하기
	            </button>
	            <button class="btn" type="button" onclick="location.href='../mvcboard/list.do';">
	                목록 바로가기
	            </button>
	        </div>
		</div>
		
		<!-- 사이드 -->
		<aside class="news_right">
			<div class="aside_box">
				<jsp:include page="/components/usercard.jsp"></jsp:include>
				<jsp:include page="/components/viewestNews.jsp"></jsp:include>
			</div>
		</aside>
	</div>

<jsp:include page="/components/footer.jsp"></jsp:include>
</body>
</html>