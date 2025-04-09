export function loginAlert() {
  alert('로그인 후 이용 가능합니다.');
  window.location.href = '/login.do';
}
export function toggleLike(articleId) {
  $.ajax({
      url: '/like.do',
      method: 'POST',
      data: { id: articleId },
      success: function(res) {
        if (re.success) {
          const likesCnt = $('.likes.cont p span');

          // 현재 좋아요 수
          let currentLikes = likesCnt.text() ? parseInt(likesCnt.text()) : 0;

          // 좋아요 상태에 따라 증가 또는 감소
          if (response.liked) {
              likesCnt.text(currentLikes + 1);
          } else {
              likesCnt.text(currentLikes - 1);
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