$(document).ready(function() {
  
  let windowHeight
  // resize
  $(window).resize(function() {
    // 브라우저의 bottom 위치
    windowHeight = $(window).height();
  });

  // 스크롤시 나타나기
  $(window).scroll(function() {
    let scrollTop = $(this).scrollTop();
    let contentTop = $('.news_container').offset().top;
    
    // news_container의 높이
    let contentHeight = $('.news_container').outerHeight();
    // 브라우저의 bottom 위치
    windowHeight = $(window).height();
    let windowBottom = windowHeight+scrollTop;

    if(scrollTop > contentTop-180 && windowBottom < contentTop+contentHeight+100) {
      $('aside.news_right').addClass('show');
    }
    else {
      $('aside.news_right').removeClass('show');
    }

  });

});