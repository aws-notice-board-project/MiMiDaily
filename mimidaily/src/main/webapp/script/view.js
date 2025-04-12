export function loginAlert() {
  alert('로그인 후 이용 가능합니다.');
  window.location.href = '/login.do';
}

// 댓글 위치로 이동
function commentTop(){
  $('.comments.cont').on('click', function(){
    const commentTop = $('.view_bottom .comments').offset().top;	
	$('html, body').animate({ scrollTop: commentTop-110 }, 500);		
  });	
}

// 댓글 500자 제한
function commentWord(){
  const comment = $('textarea#comment');
  const comcnt =  $('.comt_cnt');
  comment.on('input', function(){
    const val = comment.val();
    comcnt.text(val.length);
	  if(val.length > 499){
  	    alert('댓글은 500자 이내로 작성해주세요.');
        comment.val(val.substring(0, 499));
	  }
  });	
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

let alreadyExecuted = false; // 글자 갯수에 따라 옵저버 발생량 증가하므로 최적화 
// 댓글 작성 비동기 처리
export	function insertComment(memberId, articleIdx) {
  alreadyExecuted=false;
  // 유효성 검사 
  const cnt = $('textarea#comment').val().length;
  if(!cnt){
	alert('입력된 값이 없습니다.');
	return;
  }
  if(cnt<500){
	  $.ajax({
	    url: '/comments/insert.do',
	    method: 'post',
	    data: {
	      comment: $('#comment').val(),
	      memberId: memberId,
	      articleIdx: articleIdx,
	    },
	    success: function (res) {
	      // 댓글 추가 성공 시 처리 로직
	      // const profileIdx = ${member.profile_idx}; // 아직 프로필 없음
	      const context = $('#comment').val();
	      const commentList = $('.comments_list');
          const commentIdx = res.idx; // 서버에서 받은 댓글 인덱스
	      let profileHtml = '';
	
	      // if(parseInt(profileIdx) == 0 || profileIdx == null){
	      if(res){
	          profileHtml = `<i class="fa-solid fa-circle-user none_profile"></i>`;
	      }else{
	          profileHtml = `<div class="profile_img"><img src="" alt="내 프로필 이미지"></div>`;
	      }
	
	      let newComment = `
		  <div class="comment_box" data-comment-idx="${commentIdx}">
        <div class="coment_cont">
          <div class="profile_img">
            ${profileHtml}
          </div>
          <div class="content_box">
            <div class="comt_context">
              <p><strong>${memberId}</strong><span class="is_updated"></span></p>
              <p class="comt_date">방금 전</p>
            </div>
            <p class="comt_content">${context}</p>
          </div>
        </div>
        <div class="comt_btn">
          <button onclick="updateComment(${commentIdx})">수정</button>
          <button onclick="deleteComment(${commentIdx})">삭제</button>
        </div>
		  </div>
	      `;
	    
	    if($('.no_comt').text().includes('댓글이 없습니다.')){
	      $('.comments_list').empty(); // 댓글이 없을 때 비우기
	    }
	
	      commentList.prepend(newComment);
	      $('#comment').val(''); // 입력 필드 초기화
	    },
		error:function(e){
	      console.warn('댓글 추가 실패');
		  console.log('Error :', e);
		}
	  })
  }else{
	console.warn("댓글 500자 이상 작성 불가");
  }
};

// 댓글 수정 비동기 처리
let isEditing = false; // 중복 수정 불가 
export function updateComment(commentIdx) {
  if (isEditing) {
    alert("다른 댓글을 수정 중입니다.");
    return;
  }

  isEditing = true; // 수정 시작
	
  const commentBox = $(`.comment_box[data-comment-idx="${commentIdx}"]`);
  const commentText = commentBox.find('.comt_content'); // 댓글 내용 부분
  const originalText = commentText.text().trim(); // 원래 댓글 내용

  // textarea로 변경
  commentText.html(`<textarea id="update_comment" rows="4">${originalText}</textarea>`);

  // 버튼 변경
  const buttonHtml = `
    <span class="updated_cnt"><span class="cnt">0</span>/500</span>
    <button onclick="confirmUpdate(${commentIdx})">확인</button>
    <button onclick="cancelUpdate(${commentIdx}, '${originalText}')">취소</button>
  `;
  commentBox.find('.comt_btn').html(buttonHtml);
  
  const updated = $('textarea#update_comment');
  const updatedComcnt = $('.updated_cnt>.cnt ');
  updatedComcnt.text(originalText.length);
  updated.on('input', function(){
  	const val = updated.val();
      updatedComcnt.text(val.length);
  	  if(val.length > 499){
    	  alert('댓글은 500자 이내로 작성해주세요.');
          updated.val(val.substring(0, 499));
  	  }
    });
}
// 댓글 수정 확인 비동기 처리
export function confirmUpdate(commentIdx) {
  const commentBox = $(`.comment_box[data-comment-idx="${commentIdx}"]`);
  const updatedText = commentBox.find('#update_comment').val();
  if(!updatedText){
	alert('입력된 값이 없습니다.');
	return;
  }
  $.ajax({
    url: '/comments/update.do',
    method: 'post',
    data: {
      commentIdx: commentIdx,
      comment: updatedText,
    },
    success: function (res) {
      if (res) {
        reloadComments(); // 댓글 전체 다시 불러오기
        isEditing = false;
      } else {
        console.warn('댓글 수정에 실패했습니다.');
      }
    },
    error: function (e) {
      console.error('Error:', e);
    }
  });	
  
}

// 댓글 조회 리랜더링
function reloadComments() {
  const articleIdx = $('#article_idx').text().trim();
  $.ajax({
    url: '/comments/list.do',
    method: 'get',
    data: { articleIdx: articleIdx },
    success: function (html) {
      $('.comments_container').html(html); // JSP에서 렌더링된 댓글 HTML 조각
    },
    error: function (e) {
      console.error('댓글 목록 불러오기 실패:', e);
    }
  });
}
// 댓글 수정 취소 비동기 처리
export function cancelUpdate(commentIdx, originalText) {
  isEditing = false; // 수정 상태 해제
  const commentBox = $(`.comment_box[data-comment-idx="${commentIdx}"]`);
  const commentText = commentBox.find('.comt_content');

  // 원래 댓글 내용으로 되돌리기
  commentText.html(originalText);

  // 버튼 변경
  const buttonHtml = `
    <button onclick="updateComment(${commentIdx})">수정</button>
    <button onclick="deleteComment(${commentIdx})">삭제</button>
  `;
  commentBox.find('.comt_btn').html(buttonHtml);
}

// 댓글 삭제 비동기 처리
export function deleteComment(commentIdx){
  alreadyExecuted=false;
  if(confirm('댓글을 삭제하시겠습니까?')){
    $.ajax({
      url: '/comments/delete.do',
      method: 'post',
      data: {
        commentIdx: commentIdx,
      },
      success: function (res) {
        $(`.comment_box[data-comment-idx="${commentIdx}"]`).remove();
      },
      error: function (e) {
        console.warn('댓글 삭제 실패');
        console.error('Error:', e);
      }
    });
  }else return;
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
  // MutationObserver: 요소 추가 제거반응 
  let observer = new MutationObserver(function (mutations) {
	if (alreadyExecuted) return; // 한 번만 실행
	console.log('옵저버 발생');
	alreadyExecuted = true;
    
	contentHeight = $('.view_box').height();
    $('aside.news_right').css('height', contentHeight + 200);
  });

  // 대상 요소 지정
  observer.observe(document.querySelector('.comments_list'), {
    childList: true, // 자식 노드 추가/삭제 감지
    // subtree: true // 자식의 자식도 감지
  });

  // 댓글 불러오기
  reloadComments();

  // 필요 함수 로드
  commentTop();
  commentWord();
});