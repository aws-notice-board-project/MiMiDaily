document.addEventListener('DOMContentLoaded', function() {
  // from에서 입력값 받아오기 모음
  const form = document.forms["join_form"];
  if (!form) {
      console.error('Form not found');
      return;
  }

  // 입력 필드 모음
  const idInput = form.querySelector('[name="id"]');
  const pwInput = form.querySelector('[name="pw"]');
  const rpwInput = form.querySelector('[name="rpw"]');
  const nameInput = form.querySelector('[name="name"]');
  const emailInput = form.querySelector('[name="email"]');
  const telInput = form.querySelector('[name="tel"]');
  const birthInput = form.querySelector('[name="birth"]');
  const genderInput = form.querySelector('[name="gender"]');
  const codeInput = form.querySelector('[name="code"]');

  // 에러 메시지 영역 모음
  const error = {
      id: document.querySelector('#id .error'), 
      pw: document.querySelector('#pw .error'), 
      rpw: document.querySelector('#rpw .error'), 
      name: document.querySelector('#name .error'), 
      email: document.querySelector('#email .error'), 
      tel: document.querySelector('#tel .error'), 
      birth_gender: document.querySelector('#birth_gender .error'), 
      code: document.querySelector('#code .error')
  };

  // 정규 표현식 모음
  const regex = {
      id: /^[a-zA-Z0-9_]{5,20}$/, 
      pw: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/, 
      email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, 
      tel: /^010-?\d{4}-?\d{4}$/, 
      birth: /^\d{6}$/, 
      gender: /^[1-4]$/
  };

  // 정규 표현식으로 검증
  idInput.addEventListener('input', function() {
    const id=idInput.value;
    if (!regex.id.test(id)) {
      error.id.classList.remove('hidden');
      error.id.textContent = '아이디 형식이 올바르지 않습니다.';
    } else {
      error.id.classList.add('hidden');
    }
  });
  pwInput.addEventListener('input', function() {
    const pw=pwInput.value;
    if (!regex.pw.test(pw)) {
      error.pw.classList.remove('hidden');
      error.pw.textContent = '비밀번호는 8~20자이며 숫자, 특수문자를 포함해야 합니다.';
    } else {
      error.pw.classList.add('hidden');
    }
  });
  rpwInput.addEventListener('input', function() {
    const rpw=rpwInput.value;
    if (rpw!=pw) {
      error.rpw.classList.remove('hidden');
      error.rpw.textContent = '비밀번호가 일치하지 않습니다.';
    } else {
      error.rpw.classList.add('hidden');
    }
  });
  // nameInput.addEventListener('input', function() {
  //   const name=nameInput.value;
  //   if (!regex.name.test(name)) {
  //     error.name.classList.remove('hidden');
  //     error.name.textContent = '이름은 한글 2~6자여야 합니다.';
  //   } else {
  //     // 비밀번호 오류 메시지 숨기기
  //     error.name.classList.add('hidden');
  //   }
  // });
  emailInput.addEventListener('input', function() {
    const email=emailInput.value;
    if (!regex.email.test(email)) {
      error.email.classList.remove('hidden');
      error.email.textContent = '이메일 형식이 올바르지 않습니다.';
    } else {
      error.email.classList.add('hidden');
    }
  });
  telInput.addEventListener('input', function() {
    const tel=telInput.value;
    if (!regex.tel.test(tel)) {
      error.tel.classList.remove('hidden');
      error.tel.textContent = '연락처 형식이 올바르지 않습니다.';
    } else {
      error.tel.classList.add('hidden');
    }
  });
  birthInput.addEventListener('input', function() {
    const birth=birthInput.value;
    if (!regex.birth.test(birth)) {
      error.birth_gender.classList.remove('hidden');
      error.birth_gender.textContent = '주민등록번호가 올바르지 않습니다.';
    } else {
      error.birth_gender.classList.add('hidden');
    }
  });
  genderInput.addEventListener('input', function() {
    const gender=birthInput.value;
    if(!regex.gender.test(gender)) {
      error.birth_gender.classList.remove('hidden');
      error.birth_gender.textContent = '주민등록번호가 올바르지 않습니다.';
    }  else {
      error.birth_gender.classList.add('hidden');
    }
  });

  // 각 체크박스에 이벤트 리스너 추가
  document.getElementById('agree1').addEventListener('click', toggleSubmitButton);
  document.getElementById('agree2').addEventListener('click', toggleSubmitButton);
  document.getElementById('agree3').addEventListener('click', toggleSubmitButton);

  // 전체 선택 체크박스 클릭 시
  document.getElementById('agree_all').addEventListener('click', AllCheck);

  // 각 버튼에 이벤트 리스너 추가
  document.getElementById('agree_modal_btn1').addEventListener('click', function() {
      openModal('agree_modal1', 'agree_content1.html'); // 모달1 내용 불러오기
  });
  document.getElementById('agree_modal_btn2').addEventListener('click', function() {
      openModal('agree_modal2', 'agree_content2.html'); // 모달2 내용 불러오기
  });
  document.getElementById('agree_modal_btn3').addEventListener('click', function() {
      openModal('agree_modal3', 'agree_content3.html'); // 모달3 내용 불러오기
  });

  // 각 모달 닫기 버튼에 이벤트 리스너 추가
  document.getElementById('close1').addEventListener('click', function() {
      closeModal('agree_modal1');
  });
  document.getElementById('close2').addEventListener('click', function() {
      closeModal('agree_modal2');
  });
  document.getElementById('close3').addEventListener('click', function() {
      closeModal('agree_modal3');
  });
});

// 상단 두 체크박스가 선택된 경우에만 submit 버튼을 활성화
function toggleSubmitButton() {
  document.querySelector('#join_btn input').disabled = !(document.getElementById('agree1').checked && document.getElementById('agree2').checked); 
}

// 전체 선택/해제 체크박스를 클릭하면 나머지 체크박스를 선택하거나 해제하는 함수
function AllCheck() {
  // 모든 체크박스를 강제로 체크 상태로 설정
  document.getElementById('agree1').checked = true;
  document.getElementById('agree2').checked = true;
  document.getElementById('agree3').checked = true;
  toggleSubmitButton();
}

// 모달 열기 함수
function openModal(modalId, contentUrl) {
  const modal = document.getElementById(modalId);
  modal.style.display = 'block';
  // AJAX로 모달 내용 불러오기
  const xhr = new XMLHttpRequest();
  xhr.open('GET', contentUrl, true); // contentUrl을 불러옴
  xhr.onload = function() {
      if (xhr.status === 200) {
          document.getElementById(modalId + 'Content').innerHTML = xhr.responseText;
      }
  };
  xhr.send();
}

// 모달 닫기 함수
function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  modal.style.display = 'none';
}