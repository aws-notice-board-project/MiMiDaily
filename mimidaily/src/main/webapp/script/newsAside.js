$(document).ready(function() {
  console.log("newsAside.js loaded");
  
  // 스크롤시 나타나기
  $(window).scroll(function() {
    let scrollTop = $(this).scrollTop();
    let contentTop = $('.news_container').offset().top;
    
    if(scrollTop > contentTop-160) {
      $('aside.news_right').addClass('show');
    }
    else {
      $('aside.news_right').removeClass('show');
    }

  });

});