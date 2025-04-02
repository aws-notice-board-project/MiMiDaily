<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>맛집 기사</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/musteat.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/musteat.js"></script>

</head>

<body id="musteat">
	<jsp:include page="/components/navigation.jsp"></jsp:include>
	<jsp:include page="/components/searchBar.jsp"></jsp:include>

	<div id="wrap">
		<div class="top_title">
			<span>맛집</span>
			<div class="under_line"></div>
		</div>
		<div class="cont">
			<div class="news_list">
				<c:choose>
					<c:when test="${ empty boardLists }">
						등록된 게시물이 없을 때
					</c:when>
					<c:otherwise>
						<c:forEach items="${ boardLists }" var="row" varStatus="loop">
							<%-- ${ row.idx } <!-- 번호 --> ${ map.totalCount - (((map.pageNum-1) *
								map.pageSize) + loop.index)} --%>
								<div class="news"
									onclick="location.href='/articles/view.do?idx=${ row.idx }'">
									<div class="news_info">
										<div class="news_title">${ row.title }</div>
										<div class="news_text">${ row.content }</div>
										<div class="news_date">${fn:substring(row.created_at, 0, 10)}</div>
										<div class="news_writer">${ row.members_id }</div>
									</div>
									<div class="news_thumnails">
										<c:if test="${row.thumnails_idx == 0}">
											<img src="${pageContext.request.contextPath}/media/images/no_image.png" alt="no image">
										</c:if>
										<c:if test="${row.thumnails_idx != 0}">
											<img src="${pageContext.request.contextPath}${row.file_path}${row.sfile}" alt="썸네일">
										</c:if>	
									</div>
								</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="right_cont">
				<div class="login_box">
					<c:if test="${empty sessionScope.loginUser}">
						<p><b>미미일보</b>가 당신의 여정에 동행합니다.</p>
						<a class="login btn" href="/login.do">로그인</a>
						<a class="register" href="/join.do">회원가입</a>
					</c:if>
					<c:if test="${not empty sessionScope.loginUser}">
						<p><b>${sessionScope.loginUser != null ? sessionScope.loginUser : "게스트"}님</b> 환영합니다.
						</p>
						<div class="logining">
							<a class="write btn" href="/articles/write.do">기사 작성하기</a>
							<a class="logout btn" href="/logout.do">로그아웃</a>
						</div>
					</c:if>
				</div>
				<div class="most_viewed_news">
					<h3>실시간 관심 기사</h3>
					<ul class="news_list">
						<li>
							<img src="" alt="뉴스 이미지">
							<div>
								<h4>기사 제목</h4>
								<p>기사 내용</p>
								<p>2025.00.00</p>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="page_nav">
			${ map.pagingImg }
		</div>
	</div>
	<jsp:include page="/components/footer.jsp"></jsp:include>
</body>

</html>