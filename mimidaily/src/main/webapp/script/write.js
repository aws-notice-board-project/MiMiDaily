document.addEventListener("DOMContentLoaded", function () {
  //기사 작성시 현재 시간 삽입
  const now = new Date();
  const formatted = now.getFullYear() + "-"
    + String(now.getMonth() + 1).padStart(2, '0') + "-"
    + String(now.getDate()).padStart(2, '0') + " "
    + String(now.getHours()).padStart(2, '0') + ":"
    + String(now.getMinutes()).padStart(2, '0') + ":"
    + String(now.getSeconds()).padStart(2, '0');
  document.getElementById('time_input').value = formatted;

  //이미지 파일인지 체크와 허용 파일크기 체크
  const MAX_FILE_SIZE = 3 * 1024 * 1024; // 허용 최대 크기: 2MB

  document.getElementById('ofile').addEventListener('change', function(e) {
    const file = this.files[0];
    if (!file) return;

    // 1) 타입 검사
    if (!file.type.startsWith('image/')) {
      alert('이미지 파일만 업로드 가능합니다.');
      this.value = ''; // 선택 초기화
      return;
    }

    // 2) 용량 검사
    if (file.size > MAX_FILE_SIZE) {
      alert('파일 크기는 3MB 이하만 가능합니다.');
      this.value = '';
      return;
    }
  });

});