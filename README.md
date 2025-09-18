# <h1 align="left"><img src="/public/icon.png" alt="MiMiDaily Icon" width="36" style="vertical-align:middle;"> MiMiDaily</h1>

<!-- 메인 이미지: 사용자가 교체할 예정 -->

![Main placeholder](/public/readme/main-placeholder.png)

MiMiDaily는 개인/팀용 간단한 데일리 노트(공지/일정/회고) 웹 애플리케이션입니다. React + Vite 기반의 SPA로, 사용자가 빠르게 공지사항을 올리고 날짜별로 기록을 남길 수 있도록 설계되어 있습니다.

---

## ✨ 주요 기능

* 공지사항(Notice) 작성/수정/삭제
* 날짜별 데일리(메모) 작성 및 캘린더 기반 조회
* 공지에 태그 및 중요도(아이콘) 표시
* 임시 데이터로 빠른 미리보기 지원
* 이미지 업로드(프로필/공지 썸네일) — 로컬 `/public` 경로에 넣어 교체 가능

---

## 🧩 기술 스택 (배지 형태)

### Front End

![HTML5](https://img.shields.io/badge/HTML5-E34F26?logo=html5\&logoColor=white) ![CSS3](https://img.shields.io/badge/CSS3-1572B6?logo=css3\&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript\&logoColor=black) ![jQuery](https://img.shields.io/badge/jQuery-0769AD?logo=jquery\&logoColor=white) ![TailwindCSS](https://img.shields.io/badge/TailwindCSS-38B2AC?logo=tailwind-css\&logoColor=white)

### Back End

![Java](https://img.shields.io/badge/Java-007396?logo=java\&logoColor=white) ![JSP](https://img.shields.io/badge/JSP-000000?logo=java\&logoColor=white) ![JDBC](https://img.shields.io/badge/JDBC-005A9C?logo=database\&logoColor=white)

### DB

![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql\&logoColor=white)

### IDE

![VSCode](https://img.shields.io/badge/VSCode-007ACC?logo=visual-studio-code\&logoColor=white) ![Eclipse](https://img.shields.io/badge/Eclipse-2C2255?logo=eclipseide\&logoColor=white)

### Management

![Git](https://img.shields.io/badge/Git-F05032?logo=git\&logoColor=white) ![Jira](https://img.shields.io/badge/Jira-0052CC?logo=jira\&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000?logo=notion\&logoColor=white) ![Figma](https://img.shields.io/badge/Figma-F24E1E?logo=figma\&logoColor=white)

### Cloud / Infra

![AWS](https://img.shields.io/badge/AWS-232F3E?logo=amazon-aws\&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker\&logoColor=white) ![Argo CD](https://img.shields.io/badge/ArgoCD-6E4A9E?logo=argocd\&logoColor=white) ![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?logo=kubernetes\&logoColor=white) ![Tomcat](https://img.shields.io/badge/Tomcat-F8DC75?logo=apache-tomcat\&logoColor=black) ![Linux](https://img.shields.io/badge/Linux-FCC624?logo=linux\&logoColor=black)

---

## 📁 권장 폴더 구조

```
MiMiDaily/
├─ public/
│  ├─ icon.png               # 프로젝트 아이콘 (이름 옆)
│  └─ readme/
│     └─ main-placeholder.png # 첫 페이지에 들어갈 임시 이미지 (사용자가 교체)
├─ src/
│  ├─ components/
│  ├─ pages/
│  ├─ data/                  # 임시 JSON 데이터 위치
│  └─ styles/
├─ package.json
└─ vite.config.js
```

---

## ⚙️ 설치 및 실행

```bash
git clone https://github.com/aws-notice-board-project/MiMiDaily.git
cd MiMiDaily
npm install
npm run dev
```

---

## 🔧 README에 포함된 임시(샘플) 데이터

아래 JSON은 `src/data/mock-notices.json` 또는 `src/data/mock-dailies.json` 같은 파일로 임시 저장해 UI를 빠르게 확인할 수 있도록 만든 예시입니다. 실제 배포 시 API 또는 DB로 대체하세요.

### 공지 샘플 (mock-notices.json)

```json
[
  {
    "id": "notice-001",
    "title": "서비스 점검 안내",
    "author": "관리자",
    "icon": "/public/icons/maintenance.svg",
    "pinned": true,
    "date": "2025-09-01",
    "summary": "서버 점검으로 인해 서비스가 일시 중단됩니다. 자세한 사항은 내부 문서를 확인하세요.",
    "image": "/public/readme/notice-sample-1.png"
  },
  {
    "id": "notice-002",
    "title": "버전 1.1 릴리즈",
    "author": "dev-team",
    "icon": "/public/icons/release.svg",
    "pinned": false,
    "date": "2025-09-10",
    "summary": "UI 개선 및 버그 수정이 포함된 릴리즈입니다.",
    "image": "/public/readme/notice-sample-2.png"
  }
]
```

### 데일리 샘플 (mock-dailies.json)

```json
[
  {
    "id": "daily-2025-09-15",
    "date": "2025-09-15",
    "entries": [
      { "time": "09:00", "content": "팀 스탠드업 — 어제 작업 내용/오늘 계획", "tags": ["스탠드업"] },
      { "time": "11:30", "content": "라이브러리 버전 업그레이드", "tags": ["개발"] }
    ]
  },
  {
    "id": "daily-2025-09-16",
    "date": "2025-09-16",
    "entries": [
      { "time": "14:00", "content": "디자인 리뷰", "tags": ["디자인"] }
    ]
  }
]
```

---

## 🖼 아이콘 & 첫 페이지 이미지 교체 가이드

1. 프로젝트 루트의 `public/` 폴더에 다음 파일을 추가하세요:

   * `icon.png` — 프로젝트 이름 옆에 표시할 아이콘 (권장 크기: 40×40)
   * `readme/main-placeholder.png` — README 상단에 표시되는 메인 이미지 (원하시는 이미지로 교체)
2. 컴포넌트에서 아이콘을 불러올 때는 절대 경로(`/public/icon.png`) 대신 상대 import를 사용하면 빌드 시 문제를 줄일 수 있습니다 (예: `import icon from '/icon.png'`).

---

## 📌 개발 팁

* 빠른 개발을 위해 `src/data`에 mock JSON을 두고 fetch 대신 `import mock from './data/mock-notices.json'` 로 불러와 사용하면 편합니다.
* 공지의 아이콘은 SVG를 권장합니다. 색상 변경이나 사이즈 조절이 쉽습니다.
* 첫 페이지(랜딩)는 사용자가 이미지를 교체할 수 있도록 `public/readme/main-placeholder.png` 경로를 사용한 정적 이미지로 두세요.

---

## 📄 라이선스

MIT License

---

> README 파일의 이미지(아이콘, 첫페이지)는 현재 임시 placeholder로 설정되어 있습니다. 실제 이미지를 `public/` 폴더에 업로드하면 자동으로 대체됩니다.
