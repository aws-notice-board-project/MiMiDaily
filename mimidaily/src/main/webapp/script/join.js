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
	const genderInput = form.querySelector('[name="gender_code"]');
	const codeInput = form.querySelector('[name="code"]');
	// 동의 체크박스 모음
	const agree1 = form.querySelector('[name="agree1"]');
	const agree2 = form.querySelector('[name="agree2"]');
	const agree3 = form.querySelector('[name="agree3"]');
	// 서버에 넘겨주기 위한 값
	const gender = form.querySelector('[name="gender"]');
	const role = form.querySelector('[name="role"]');
	const marketing = form.querySelector('[name="marketing"]');
	// id 중복확인 버튼
	const idCheckBtn = document.getElementById("id_check");
	// id 사용 버튼
	const idUseBtn = document.getElementById("id_use");
	// 기자 인증 코드 확인 버튼
	const codeCheckBtn = document.getElementById("code_check");
	// 전체 동의 버튼
	const agreeAll = form.querySelector('[name="agree_all"]');
	// 가입 버튼
	const register = form.querySelector('[type="submit"]');
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
		id: /^[a-zA-Z0-9_]{4,20}$/, 
		pw: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*`.])[A-Za-z\d!@#$%^&*`.]{8,20}$/, 
		email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, 
		tel: /^010-?\d{4}-?\d{4}$/, 
		birth: /^\d{6}$/, 
		gender: /^[1-4]$/,
		code: /^(NEWS24X7|PRESS911|JOURN88|MEDIA2025|REP0RT10)$/
	};
	
	// id input 클릭 또는 입력 시작 시 버튼(중복확인)
	idInput.addEventListener("focus", function() {
		if (idInput.readOnly) return; // 비활성화 상태에서는 무시
		idCheckBtn.classList.remove('hidden');
		idUseBtn.classList.add('hidden');
	});
	// id 중복 체크
	idCheckBtn.addEventListener("click", function() {
		const id_value=idInput.value;
		const xhr = new XMLHttpRequest();
		if(!regex.id.test(id_value)) {
			idInput.focus();
			alert("아이디는 4~20자의 영문 대소문자, 숫자, 밑줄(_)만 사용할 수 있습니다.");
		} else {
			// id 중복 체크를 위해 servlet으로 값 넘기기
			xhr.open("GET", `join.do?id=${encodeURIComponent(id_value)}`, true);
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			xhr.onreadystatechange = function () {
				if (xhr.readyState === 4 && xhr.status === 200) {
					const message = xhr.responseText.trim();
					// 응답 메시지를 data 속성에 저장
					idInput.dataset.id_error = message;
					alert(message+"\n 아이디 사용 버튼을 클릭하세요."); // 결과를 사용자에게 보여줌
					// 버튼 변경
					if (message == "사용 가능한 아이디입니다.") {
						idCheckBtn.classList.add('hidden');
						idUseBtn.classList.remove('hidden');
					} else {
						idCheckBtn.classList.remove('hidden');
						idUseBtn.classList.add('hidden');
					}
				}
			}
			xhr.send();
		}
	});
	// 사용 버튼 클릭 시 비활성화(변경 불가 상태)
	idUseBtn.addEventListener("click", function() {
		idInput.readOnly = true;
		idUseBtn.diabled = true;
		document.querySelector('#id').classList.add('readonly');
	});
	
	// 정규 표현식으로 검증 모음
	pwInput.addEventListener('input', function() {
		const pw_value=pwInput.value;
		if (!regex.pw.test(pw_value)) {
			error.pw.classList.remove('hidden');
			error.pw.textContent = '비밀번호는 8~20자이며, 영문자, 숫자, 특수문자(!@#$%^&*)를 각각 하나 이상 포함해야 합니다.';
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
	
	// 기자 인증 코드 확인
	codeCheckBtn.addEventListener('click', function() {
		const code_value=codeInput.value;
		if(!regex.code.test(code_value)) {
			codeInput.focus();
			alert("인증 코드가 올바르지 않습니다.");
		} else {
			alert("기자 인증이 완료되었습니다.");
			codeInput.readOnly = true;
			codeCheckBtn.disabled = true;
			document.querySelector('#code').classList.add('readonly');
		}
	});
	
	// 필수 입력값이 모두 작성되었는지 확인하는 함수
	function checkRequiredFields() {
		return idInput.readOnly &&
			   pwInput.value.trim() &&
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
			   (!genderInput.value || regex.gender.test(genderInput.value)) && 
			   (!codeInput.value || codeInput.readOnly);
	}
	
	// 필수 입력값들이 비어있지 않고 작성된 값들이 알맞은 상태에서
	// 필수 동의에 체크된 경우에만 submit 버튼을 활성화
	function toggleSubmitButton() {
		if (checkValidInputs() && checkRequiredFields()) {
			register.disabled = false;
		} else {
			register.disabled = true;
		}
		register.disabled = !(agree1.checked && agree2.checked); 
	}

	// 전체 선택/해제 체크박스를 클릭하면 나머지 체크박스를 선택하거나 해제하는 함수
	function AllCheck() {
		// 모든 체크박스를 강제로 체크 상태로 설정
		agree1.checked = true;
		agree2.checked = true;
		agree3.checked = true;
		toggleSubmitButton();
	}
	// 각 체크박스에 이벤트 리스너 추가
	agree1.addEventListener('click', toggleSubmitButton);
	agree2.addEventListener('click', toggleSubmitButton);
	agree3.addEventListener('click', toggleSubmitButton);
	// 전체 선택 체크박스 클릭 시 전체 선택 이벤트 추가
	agreeAll.addEventListener('click', AllCheck);
	
	
	// submit 버튼이 클릭되었을 때 경고창 띄우기
	form.addEventListener('submit', function(event) {
		if (!checkRequiredFields() || !checkValidInputs()) {
			event.preventDefault(); // 제출 막기
			alert('모든 필수 입력값을 작성하고, 형식에 맞게 입력해 주세요.');
		} else if (!agree1.checked || !agree2.checked) {
			event.preventDefault(); // 제출 막기
			alert('이용 약관과 개인정보 수집에 모두 동의하셔야 가입이 가능합니다.');
		} else { // 변환이 필요한 입력값은 따로 보내기
			// 성별
			if(genderInput.value=="1" || genderInput.value=="2") {
				gender.value="m";
			} else if(genderInput.value=="3" || genderInput.value=="4") {
				gender.value="f";
			} else {
				gender.value=null;
			}
			// 기자/일반회원
			role.value = codeInput.readOnly ? '2' : '1';
			// 마케팅 동의 여부
			marketing.value = agree3.checked ? '1' : '0';
		}
	});

	// 회원가입 성공/실패 메세지 띄우기
	function success() {
	  alert(register.dataset.success_msg);
	}
	// 최종 가입 버튼에 성공 여부 메세지 출력 이벤트 추가
	register.addEventListener('submit', success);

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
});