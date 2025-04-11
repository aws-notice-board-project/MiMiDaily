export function loginAlert() {
  alert('로그인 후 이용 가능합니다.');
  window.location.href = '/login.do';
}

// 좋아요 비동기 처리
export function toggleLike(articleIdx) {
  $.ajax({
    url: '/articles/like.do',
    method: 'post',
    data: {
      articleIdx: articleIdx,
    },
    success: function (res) {
      if (res) {
        // 같이 로직 처리
        const likesCnt0 = $('.likes.cont p span.like_cnt').eq(0); // xs사이즈 좋아요
        const likesCnt1 = $('.likes.cont p span.like_cnt').eq(1); // md사이즈 좋아요

        // 현재 좋아요 수
        let currentLikes0 = likesCnt0.text() ? parseInt(likesCnt0.text()) : 0;
        let currentLikes1 = likesCnt1.text() ? parseInt(likesCnt1.text()) : 0;
        
        // 좋아요 상태에 따라 증가 또는 감소
        if (res.liked) {
          likesCnt0.text(currentLikes0 + 1);
          likesCnt1.text(currentLikes1 + 1);
          $('.view_container .likes i').addClass('fa-solid');
          $('.view_container .likes i').removeClass('fa-regular');
          $('.view_container .likes i').css('color', 'red');
        } else {
          likesCnt0.text(currentLikes0 - 1);
          likesCnt1.text(currentLikes1 - 1);
          $('.view_container .likes i').addClass('fa-regular');
          $('.view_container .likes i').removeClass('fa-solid');
          $('.view_container .likes i').css('color', '#594543');
        }
      } else {
        console.warn('좋아요 업데이트에 실패했습니다.');
      }
    },
    error: function (e) {
      console.error('Error:', e);
    }
  });
}

	// 댓글 작성
export	function insertComment(memberId, articleIdx) {
	  $.ajax({
	    url: '/comments.do',
	    method: 'post',
	    data: {
	      comment: $('#comment').val(),
	      memberId: memberId,
	      articleIdx: articleIdx,
	    },
	    success: function (res) {
	      console.log('댓글 생성 성공');
	      
	      // 댓글 추가 성공 시 처리 로직
	      // const profileIdx = ${member.profile_idx}; // 아직 프로필 없음
	      const context = $('#comment').val();
	      const commentList = $('.comments_list');
	      
	      let profileHtml = '';

	      // if(parseInt(profileIdx) == 0 || profileIdx == null){
	      if(res){
	          profileHtml = `<i class="fa-solid fa-circle-user none_profile"></i>`;
	      }else{
	          profileHtml = `<div class="profile_img"><img src="" alt="내 프로필 이미지"></div>`;
	      }

	      let newComment = `
	        <div class="coment_cont">
	          <div class="profile_img">
	            ${profileHtml}
	          </div>
	          <div style="width: 100%;">
	            <div class="comt_context">
	              <p><strong>${memberId}</strong></p>
	              <p>방금 전</p>
	            </div>
	            <p>${context}</p>
	          </div>
	        </div>
	      `;	      
        console.log(newComment);
        
        if($('.comments_list').text() == '댓글이 없습니다.'){
          $('.comments_list').empty(); // 댓글이 없을 때 비우기
        }

	      commentList.prepend(newComment);
	      $('#comment').val(''); // 입력 필드 초기화
	    },
		error:function(e){
	    console.warn('댓글 추가에 실패');
			console.log('Error :', e);
		}
	  })
	};

export function deleteArticle() {
  var modal = document.getElementById('deleteModal');
  modal.style.display = 'block';
  
  // 각 버튼과 창 외부 클릭 시 처리할 이벤트 핸들러 정의
  function confirmHandler() {
    modal.style.display = 'none';
    removeModalListeners();

    document.getElementById("deleteForm").submit();
    removeModalListeners();
  }

  function cancelHandler() {
    modal.style.display = 'none';
    removeModalListeners();
  }

  function closeHandler() {
    modal.style.display = 'none';
    removeModalListeners();
  }

  function windowClickHandler(event) {
    if (event.target === modal) {
      modal.style.display = 'none';
      removeModalListeners();
    }
  }
  
  // 이벤트 리스너들을 제거하는 함수 (중복 등록 방지)
  function removeModalListeners() {
    document.getElementById('yes_btn').removeEventListener('click', confirmHandler);
    document.getElementById('no_btn').removeEventListener('click', cancelHandler);
    document.getElementById('close_btn').removeEventListener('click', closeHandler);
    window.removeEventListener('click', windowClickHandler);
  }
  
  // 이벤트 리스너들을 모달 열릴 때 등록 (매번 새로 등록)
  document.getElementById('yes_btn').addEventListener('click', confirmHandler);
  document.getElementById('no_btn').addEventListener('click', cancelHandler);
  document.getElementById('close_btn').addEventListener('click', closeHandler);
  window.addEventListener('click', windowClickHandler);
}

$(document).ready(function() {
  let contentHeight=$('.view_box').height();
  $('aside.news_right').css('height', contentHeight+200);
});