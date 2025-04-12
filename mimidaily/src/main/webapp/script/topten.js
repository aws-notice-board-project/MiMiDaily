function applyHashtagOverflowLogic(p) {
  const hashtags = p.querySelectorAll("span.hashtag");
  const parentRight = p.getBoundingClientRect().right;

  hashtags.forEach(tag => {
    tag.style.visibility = "visible";

    const tagRect = tag.getBoundingClientRect();
    const tagRight = tagRect.right;

    if (tagRight > parentRight) {
      tag.style.visibility = "hidden";
    }
  });
}



window.addEventListener("DOMContentLoaded", function () {
  const paragraphs = document.querySelectorAll("#news_container .news_title p");

  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        applyHashtagOverflowLogic(entry.target);
        observer.unobserve(entry.target); // 한 번만 실행
      }
    });
  }, { threshold: 0.1 });

  paragraphs.forEach(p => {
    observer.observe(p);
  });

  // ✅ 브라우저 리사이즈 시 다시 체크
  window.addEventListener("resize", () => {
    document.querySelectorAll("#news_container .news_title p").forEach(p => {
      applyHashtagOverflowLogic(p);
    });
  });
});