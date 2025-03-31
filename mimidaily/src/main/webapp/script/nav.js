$(document).ready(function() {
  let isOpen = false;
  $('#nav .menu_icon').on('click', function() {
    if(isOpen) {
      $('.nav_toggle').removeClass('open');
      $('#nav').css('border-bottom', '1px solid #8C7B7B');
      isOpen = false;
    }else{
      $('.nav_toggle').addClass('open');
      $('#nav').css('border-bottom', '1px solid rgb(217, 211, 211)');
      isOpen = true;
    }

  });
  // 브라우저를 리사이즈할 때 메뉴가 닫히도록 설정
  // (768px 이상일 때)
  $(window).on('resize', function() {
    if($(window).width() > 768) {
      $('.nav_toggle').removeClass('open');
      $('#nav').css('border-bottom', '1px solid #8C7B7B');
      isOpen = false;
    }
  });

  // #nav 또는 .nav_toggle 외부 클릭 시 메뉴 닫기
  $('#nav, .nav_toggle').on('click', function(event) {
    event.stopPropagation(); // 클릭 이벤트가 부모 요소로 전파되지 않도록 막음
  });
  $(document).on('click', function() {
    if (isOpen) {
      $('.nav_toggle').removeClass('open');
      $('#nav').css('border-bottom', '1px solid #8C7B7B');
      isOpen = false;
    }
  });
});