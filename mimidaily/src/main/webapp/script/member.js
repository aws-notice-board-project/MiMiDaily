document.addEventListener('DOMContentLoaded', function() {  
  const pre = document.forms["precheck"];
    if (!pre) {
      console.error('Form(precheck) not found');
      return;
  }
  const form = document.forms["member_form"];
    if (!form) {
      console.error('Form(member_form) not found');
      return;
  }
  const register = form.querySelector('[type="submit"]');
  // form으로 (기자/일반회원)checkbox 선택
//  const roleType = pre.querySelector('[name="role"]');
//  const join = pre.querySelector('[type="submit"]');
//  let unchecked = document.getElementById("unchecked");
//  let checked = document.getElementById("checked");
  
//  unchecked.addEventListener("click", function() {
//    roleType.checked = false;
//    unchecked.classList.add("hidden");
//    checked.classList.remove("hidden");
//	join.value = "기자 회원 가입";
//  });
//  checked.addEventListener("click", function() {
//    roleType.checked = true;
//    checked.classList.add("hidden");
//    unchecked.classList.remove("hidden");
//	join.value = "일반 회원 가입";
//  });*/
  
  // 현재 URL에서 쿼리 문자열 가져와서 보여줄 form을 변경
//  let params = new URLSearchParams(window.location.search);
//  let role = params.get("role");
//  join.addEventListener("click", function() {
	//event.preventDefault();
//    console.log(role);
//    if(role == "reporter" || role == null) {
//	  form.classList.remove("hidden");
//	  pre.classList.add("hidden");
//    }else {
//	  pre.classList.remove("hidden");
//  	  form.classList.add("hidden");
//    }
//  });
//  let reporterElement = document.getElementById("code");
//  	let job = reporterElement.dataset.job;
//      let savedJob = localStorage.getItem(job);
//  	console.log(savedJob);
//  		console.log(job);
//  window.onload = function () {
//	let reporterElement = document.getElementById("code");
//	let job = reporterElement.dataset.job;
//    let savedJob = localStorage.getItem(job);
//	console.log(savedJob);
//		console.log(job);
//    if (savedJob) {
//      job = savedJob;
//    }
//	console.log(savedJob);
//	console.log(job);
//	  if(job == "reporter") {
//		reporterElement.parentElement.classList.remove("hidden");
//		form.classList.remove("hidden");
//		pre.classList.add("hidden");
//	  }else if(job == "user") {
//	    reporterElement.parentElement.classList.add("hidden");
//		form.classList.remove("hidden");
//		pre.classList.add("hidden");
//	  } else {
//		pre.classList.remove("hidden");
//		form.classList.add("hidden");
//	  }
// };
  
  // 기자인지 아닌지에 따라 인증 코드 사라지고 나타남
//  let reporterElement = document.getElementById("code");
//  let jobName = reporterElement.dataset.job;
//  console.log(jobName);
//  if(jobName == "reporter") {
//	reporterElement.parentElement.classList.remove("hidden");
//	form.classList.remove("hidden");
//	pre.classList.add("hidden");
//  }else if(jobName == "user") {
//    reporterElement.parentElement.classList.add("hidden");
//	form.classList.remove("hidden");
//	pre.classList.add("hidden");
//  } else {
//	pre.classList.remove("hidden");
//	form.classList.add("hidden");
//  }

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
  // id 버튼 모음
  const idCheckBtn = document.getElementById("id_check");
  const idUseBtn = document.getElementById("id_use");
  
  // 에러 메시지 영역 모음
  const error = {
      id: document.querySelector('#id .error'), 
      pw: document.querySelector('#pw .error'), 
      rpw: document.querySelector('#rpw .error'), 
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
      gender: /^[1-4]$/,
	  code: /^(NEWS24X7|PRESS911|JOURN88|MEDIA2025|REP0RT10)$/
  };

  // 정규 표현식으로 검증
  idInput.addEventListener('input', function() {
    const id=idInput.value;
    if (!regex.id.test(id)) {
      error.id.classList.remove('hidden');
      error.id.textContent = '아이디 형식이 올바르지 않습니다.';
	  idInput.focus();
    } else {
      error.id.classList.add('hidden');
    }
  });
  pwInput.addEventListener('input', function() {
    const pw=pwInput.value;
    if (!regex.pw.test(pw)) {
      error.pw.classList.remove('hidden');
      error.pw.textContent = '비밀번호는 8~20자이며 숫자, 특수문자를 포함해야 합니다.';
	  pwInput.focus();
    } else {
      error.pw.classList.add('hidden');
    }
  });
  rpwInput.addEventListener('input', function() {
	const pw=pwInput.value;
    const rpw=rpwInput.value;
    if (rpw!=pw) {
      error.rpw.classList.remove('hidden');
      error.rpw.textContent = '비밀번호가 일치하지 않습니다.';
	  rpwInput.focus();
    } else {
      error.rpw.classList.add('hidden');
    }
  });
  emailInput.addEventListener('input', function() {
    const email=emailInput.value;
    if (!regex.email.test(email)) {
      error.email.classList.remove('hidden');
      error.email.textContent = '이메일 형식이 올바르지 않습니다.';
	  emailInput.focus();
    } else {
      error.email.classList.add('hidden');
    }
  });
  telInput.addEventListener('input', function() {
    const tel=telInput.value;
    if (!regex.tel.test(tel)) {
      error.tel.classList.remove('hidden');
      error.tel.textContent = '연락처 형식이 올바르지 않습니다.';
	  telInput.focus();
    } else {
      error.tel.classList.add('hidden');
    }
  });
  birthInput.addEventListener('input', function() {
    const birth=birthInput.value;
    if (!regex.birth.test(birth)) {
      error.birth_gender.classList.remove('hidden');
      error.birth_gender.textContent = '주민등록번호가 올바르지 않습니다.';
	  birthInput.focus();
    } else {
      error.birth_gender.classList.add('hidden');
    }
  });
  genderInput.addEventListener('input', function() {
    const gender=birthInput.value;
    if(!regex.gender.test(gender)) {
      error.birth_gender.classList.remove('hidden');
      error.birth_gender.textContent = '주민등록번호가 올바르지 않습니다.';
	  genderInput.focus();
    }  else {
      error.birth_gender.classList.add('hidden');
    }
  });
  codeInput.addEventListener('input', function() {
    const code=birthInput.value;
    if(!regex.code.test(code)) {
      error.code.classList.remove('hidden');
      error.code.textContent = '인증코드가 올바르지 않습니다.';
	  codeInput.focus();
    }  else {
      error.code.classList.add('hidden');
    }
  });

  // 정규 표현식 검증 함수
  function checkValidInputs() {
  	return regex.id.test(idInput.value) &&
             regex.pw.test(pwInput.value) &&
             pwInput.value === rpwInput.value &&
             regex.email.test(emailInput.value) &&
             regex.tel.test(telInput.value) &&
             regex.birth.test(birthInput.value) &&
             regex.gender.test(genderInput.value);
  }
  
  // id input 클릭 또는 입력 시작 시 버튼(중복확인)
  idInput.addEventListener("focus", function() {
    if (idInput.disabled) return; // 비활성화 상태에서는 무시
    idCheckBtn.classList.remove('hidden');
    idUseBtn.classList.add('hidden');
  });
  
  // 사용하기 버튼 클릭 시 input 비활성화
  idUseBtn.addEventListener("click", function() {
    idInput.disabled = true;
  });
  
  // 버튼 변경(중복확인 결과로 처리)
    if (idInput.dataset.id_error === "사용 가능한 아이디 입니다.") {
      idCheckBtn.classList.add('hidden');  // 중복확인 버튼 숨기기
      idUseBtn.classList.remove('hidden'); // 사용하기 버튼 보이기
    } else {
      idCheckBtn.classList.remove('hidden');
      idUseBtn.classList.add('hidden');
    }
  
  // id 중복 체크를 위해 servlet으로 값 넘기기
  idCheckBtn.addEventListener("click", function() {
  const id=idInput.value;
  	var url = "join.do?id=" + id;
  	window.location.replace(url);
  	alert(idInput.dataset.id_error);
  });

  // 필수 입력값이 모두 작성되었는지 확인하는 함수
  function checkRequiredFields() {
    return idInput.value && pwInput.value && rpwInput.value && nameInput.value && emailInput.value;
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
      const modal = document.querySelector(modalId);
      const contentBox = modal.querySelector('.modal_content');
      modal.style.display = 'block';
      // AJAX로 모달 내용 불러오기
      const xhr = new XMLHttpRequest();
      xhr.open('GET', contentUrl, true); // contentUrl을 불러옴
      xhr.onload = function() {
    	if (xhr.status === 200) {
    	        contentBox.innerHTML = xhr.responseText;
    	}
      };
      xhr.send();
  }
  // 모달 닫기 함수
  function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.style.display = 'none';
  }

  // 각 체크박스에 이벤트 리스너 추가
  document.getElementById('agree1').addEventListener('click', toggleSubmitButton);
  document.getElementById('agree2').addEventListener('click', toggleSubmitButton);
  document.getElementById('agree3').addEventListener('click', toggleSubmitButton);

  // 전체 선택 체크박스 클릭 시 전체 선택 이벤트 추가
  document.getElementById('agree_all').addEventListener('click', AllCheck);

  // 각 모달 버튼에 이벤트 리스너 추가
  document.getElementById('agree_modal_btn1').addEventListener('click', function() {
      openModal('#agree_modal1', '../media/agree_content1.html'); // 모달1 내용 불러오기
  });
  document.getElementById('agree_modal_btn2').addEventListener('click', function() {
      openModal('#agree_modal2', '../media/agree_content2.html'); // 모달2 내용 불러오기
  });
  document.getElementById('agree_modal_btn3').addEventListener('click', function() {
      openModal('#agree_modal3', '../media/agree_content3.html'); // 모달3 내용 불러오기
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

  // 필수 입력값과 정규 표현식 검증이 모두 통과하고
  // 상단 두 체크박스가 선택된 경우에만 submit 버튼을 활성화
  function toggleSubmitButton() {
    if (checkRequiredFields() && checkValidInputs()) {
      document.querySelector('#join_btn input').disabled = false;
    } else {
      document.querySelector('#join_btn input').disabled = true;
    }
    document.querySelector('#join_btn input').disabled = !(document.getElementById('agree1').checked && document.getElementById('agree2').checked); 
  }
  
  // submit 버튼이 클릭되었을 때 경고창 띄우기
  form.addEventListener('submit', function(event) {
    if (!checkRequiredFields() || !checkValidInputs()) {
      event.preventDefault(); // 제출 막기
      alert('모든 필수 입력값을 작성하고, 형식에 맞게 입력해 주세요.');
    }
  });
  
  // 회원가입 성공/실패 메세지 띄우기
  function success() {
    alert(register.dataset.success_msg);
  }

  // 최종 가입 버튼에 성공 여부 메세지 출력 이벤트 추가
  register.addEventListener('submit', success);
});