export function loginAlert() {
  alert('로그인 후 이용 가능합니다.');
  window.location.href = '/login.do';
}
export function toggleLike(articleIdx) {
  $.ajax({
      url: '/articles/like.do',
      method: 'post',
      data: { id: articleIdx },
      success: function(res) {
		console.log(res);
        if (res) {
          const likesCnt = $('.likes.cont p span');

          // 현재 좋아요 수
          let currentLikes = likesCnt.text() ? parseInt(likesCnt.text()) : 0;
		  
          // 좋아요 상태에 따라 증가 또는 감소
          if (res.liked) {
              likesCnt.text(currentLikes + 1);
              $('.view_container .likes i').addClass('fa-solid');
              $('.view_container .likes i').removeClass('fa-regular');
              $('.view_container .likes i').css('color', 'red');
            } else {
              likesCnt.text(currentLikes - 1);
              $('.view_container .likes i').addClass('fa-regular');
              $('.view_container .likes i').removeClass('fa-solid');
              $('.view_container .likes i').css('color', '#594543');
          }
      } else {
          console.warn('좋아요 업데이트에 실패했습니다.');
      }
      },
      error: function(e) {
          console.error('Error:', e);
      }
  });
}

$(document).ready(function() {
});