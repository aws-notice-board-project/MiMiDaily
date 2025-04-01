// from에서 입력값 받아오기 모음
const form = document.forms["join_form"];
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
  // name: /^[가-힣]{2,6}$/, 
  email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, 
  tel: /^010-?\d{4}-?\d{4}$/, 
  birth: /^\d{6}$/, 
  gender: /^[1-4]$/
};

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




// 체크박스를 클릭하면 submit 버튼을 활성화/비활성화
function toggleSubmitButton() {
  const checkbox = document.getElementById('agreeCheckbox');
  const submitButton = document.getElementById('submitButton');
  submitButton.disabled = !checkbox.checked;  // 체크박스 상태에 따라 submit 버튼 활성화/비활성화
}

// 모달 열기
function openModal() {
  const modal = document.getElementById('myModal');
  modal.style.display = 'block';
  // AJAX로 모달 내용 불러오기
  const xhr = new XMLHttpRequest();
  xhr.open('GET', 'terms_of_use.html', true); // 'modal-content.html'을 불러옴
  xhr.onload = function() {
      if (xhr.status === 200) {
          document.getElementById('modalContent1').innerHTML = xhr.responseText;
      }
  };
  xhr.send();
}

// 모달 닫기
function closeModal() {
  const modal = document.getElementById('myModal');
  modal.style.display = 'none';
}
// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
  const modal = document.getElementById('myModal');
  if (event.target === modal) {
      closeModal();
  }
}