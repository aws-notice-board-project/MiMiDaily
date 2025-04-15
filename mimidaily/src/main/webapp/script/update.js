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
	// 입력 필드 모음
	const profileInput = form.querySelector('[name="ofile"]');
	const pwInput = form.querySelector('[name="pw"]');
	const rpwInput = form.querySelector('[name="rpw"]');
	const nameInput = form.querySelector('[name="name"]');
	const emailInput = form.querySelector('[name="email"]');
	const telInput = form.querySelector('[name="tel"]');
	const birthInput = form.querySelector('[name="birth"]');
	const genderInput = form.querySelector('[name="gender_code"]');
	// 마케팅 동의 체크박스
	const agree = form.querySelector('[name="marketing_agree"]');
	// 서버에 넘겨주기 위한 값
	const gender = form.querySelector('[name="gender"]');
	const marketing = form.querySelector('[name="marketing"]');
	// 수정 버튼
	const save = form.querySelector('[type="submit"]');
	// 에러 메시지 영역 모음
	const error = {
		pw: document.querySelector('#pw .error'), 
		rpw: document.querySelector('#rpw .error'), 
		email: document.querySelector('#email .error'), 
		tel: document.querySelector('#tel .error'), 
		birth_gender: document.querySelector('#birth_gender .error')
	};
	// 정규 표현식 모음
	const regex = { 
		pw: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*`.])[A-Za-z\d!@#$%^&*`.]{8,20}$/, 
		email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, 
		tel: /^010-?\d{4}-?\d{4}$/, 
		birth: /^\d{6}$/, 
		gender: /^[1-4]$/
	};
		
	function updateFileText(input) {
	  const labelText = document.getElementById("upload_label_text");
	  if (input.files.length > 0) {
	    labelText.textContent = input.files[0].name; // 파일 이름 표시
	  } else {
	    labelText.textContent = "프로필 선택"; // 초기 텍스트 복구
	  }
	}
	profileInput.addEventListener("change", function () {
	  updateFileText(this);
	});
	pwInput.addEventListener('input', function() {
		const pw_value=pwInput.value;
		if (!regex.pw.test(pw_value)) {
			error.pw.classList.remove('hidden');
			error.pw.textContent = '비밀번호는 8~20자이며, 영문자, 숫자, 특수문자(!@#$%^&*`.)를 각각 하나 이상 포함해야 합니다.';
			pwInput.focus();
		} else {
			error.pw.classList.add('hidden');
		}
	});
	rpwInput.addEventListener('input', function() {
		const pw_value=pwInput.value;
		const rpw_value=rpwInput.value;
		if (rpw_value!=pw_value) {
			error.rpw.classList.remove('hidden');
			error.rpw.textContent = '비밀번호가 일치하지 않습니다.';
			rpwInput.focus();
		} else {
			error.rpw.classList.add('hidden');
		}
	});
	emailInput.addEventListener('input', function() {
		const email_value=emailInput.value;
		if (!regex.email.test(email_value)) {
			error.email.classList.remove('hidden');
			error.email.textContent = '이메일 형식이 올바르지 않습니다.';
			emailInput.focus();
		} else {
			error.email.classList.add('hidden');
		}
	});
	telInput.addEventListener('input', function() {
		const tel_value=telInput.value;
		if (!regex.tel.test(tel_value)) {
			error.tel.classList.remove('hidden');
			error.tel.textContent = '연락처 형식이 올바르지 않습니다.';
			telInput.focus();
		} else {
			error.tel.classList.add('hidden');
		}
	});
	birthInput.addEventListener('input', function() {
		const birth_value=birthInput.value;
		if (!regex.birth.test(birth_value)) {
			error.birth_gender.classList.remove('hidden');
			error.birth_gender.textContent = '주민등록번호 앞자리가 올바르지 않습니다.';
			birthInput.focus();
		} else {
			error.birth_gender.classList.add('hidden');
		}
	});
	genderInput.addEventListener('input', function() {
		const gender_value=genderInput.value;
		if(!regex.gender.test(gender_value)) {
			error.birth_gender.classList.remove('hidden');
			error.birth_gender.textContent = '주민등록번호가 올바르지 않습니다.';
			genderInput.focus();
		}  else {
			error.birth_gender.classList.add('hidden');
		}
	});
	
	// 필수 입력값이 모두 작성되었는지 확인하는 함수
	function checkRequiredFields() {
		return pwInput.value.trim() &&
			   rpwInput.value.trim() &&
			   nameInput.value.trim() &&
			   emailInput.value.trim();
	}

	// 작성된 값들이 알맞은지 확인하는 함수
	function checkValidInputs() {
		return (!pwInput.value || regex.pw.test(pwInput.value)) && 
			   (!rpwInput.value || rpwInput.value === pwInput.value) && 
			   (!emailInput.value || regex.email.test(emailInput.value)) && 
			   (!telInput.value || regex.tel.test(telInput.value)) && 
			   (!birthInput.value || regex.birth.test(birthInput.value)) && 
			   (!genderInput.value || regex.gender.test(genderInput.value));
	}
	
	// submit 버튼이 클릭되었을 때 경고창 띄우기
	form.addEventListener('submit', function(event) {
		if (!checkRequiredFields() || !checkValidInputs()) {
			event.preventDefault(); // 제출 막기
			alert('모든 필수 입력값을 작성하고, 형식에 맞게 입력해 주세요.');
		} else { // 변환이 필요한 입력값은 따로 보내기
			// 성별
			if(genderInput.value=="1" || genderInput.value=="2") {
				gender.value="m";
			} else if(genderInput.value=="3" || genderInput.value=="4") {
				gender.value="f";
			} else {
				gender.value=null;
			}
			// 마케팅 동의 여부
			marketing.value = agree.checked ? '1' : '0';
		}
	});

	// 정보수정 실패 메세지 띄우기
	function fail() {
	  alert(save.dataset.fail_msg);
	}
	// 최종 가입 버튼에 성공 여부 메세지 출력 이벤트 추가
	save.addEventListener('submit', fail);

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
	// 모달 버튼에 이벤트 리스너 추가
	document.getElementById('marketing_agree_modal_btn').addEventListener('click', function() {
		openModal('#marketing_agree_modal', '../media/agree_content3.html'); // 모달3 내용 불러오기
	});
	// 모달 닫기 버튼에 이벤트 리스너 추가
	document.getElementById('close').addEventListener('click', function() {
		closeModal('marketing_agree_modal');
	});
});