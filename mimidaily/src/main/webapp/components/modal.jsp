<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<style>
	.comfirm_modal{
	width:100%;
	height:100%;
	background-color:rgba(0, 0, 0, 0.5);
	position:fixed;
	top:0;
	left:0;
	display:none;
	justify-content:center;
	align-items:center;
	z-index:99;
	}
	.comfirm_modal .modal_content{width:320px;padding:2rem 1rem;}
	.comfirm_modal .modal_content p{text-align:center;margin:1rem 0 0 0;}
	.modal_content .btn_box{text-align:center;margin-top:1rem;}
	.modal_content .btn_box button{margin:4px;color: #FBF9F9;padding: 0.5rem;border-radius:4px;width:100px;}
	.modal_content .btn_box button:first-child{background-color:#594543;}
	.modal_content .btn_box button:nth-child(2){background-color:#8C7B7B}
</style> 
<div class="comfirm_modal" id="confirmModal">
  <div class="modal_content cont">
    <p>댓글을 삭제하시겠습니까?</p>
    <div class="btn_box">
      <button id="confirmYes" class="btn">예</button>
      <button id="confirmNo" class="btn">아니오</button>
    </div>
  </div>
</div>
<script type="module" defer>
	import { deleteComment } from '/script/view.js';
	window.deleteComment=deleteComment;
	$('#confirmNo').on('click', function(){$('#confirmModal').css('display', 'none')});
</script>