# ⛪ CDRS (Church Donation Receipt System) 
> **개척교회 및 소규모 교회를 위한 기부금 영수증 자동 발급 시스템**

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)

## 📌 프로젝트 소개
CDRS는 복잡한 데이터베이스나 서버 지식이 없는 일반 교회 행정 담당자도 **단 5분 만에 설치하고 사용할 수 있도록 패키징된 B2B 형태의 SaaS 시스템**입니다. 
매년 연말정산 시즌마다 수기로 작성해야 했던 기부금 영수증 발급 업무를 자동화하고, 국세청 표준 양식에 맞춘 영수증을 즉시 출력할 수 있습니다.

## ✨ 핵심 기능 (Key Features)

### 1. 🔒 안전한 관리자 시스템 (Spring Security)
* 관리자 전용 대시보드 제공
* 로그인 및 세션 관리를 통한 허가되지 않은 접근 차단
* 관리자 웹 화면에서 직접 시스템 접근 아이디 및 비밀번호 변경 가능

### 2. 🏢 맞춤형 교회 정보 덮어쓰기 (Single-Row DB Pattern)
* 영수증에 찍힐 교회명, 대표자명, 고유번호, 주소를 웹에서 즉시 설정
* 교회별 고유 영수증 일련번호(발급번호) 지정 기능
* DB 수정 즉시 서버 재시작 없이 영수증 양식에 실시간 반영

### 3. 💬 외부 API 연동 설정 (Solapi SMS)
* 교인 인증 및 알림 발송을 위한 Solapi SMS API Key 관리 화면 제공
* DB 기반의 Lazy Initialization을 통해 설정 변경 즉시 서버 재시작 없이 API 키 갱신 적용

### 4. 🖨️ 국세청 표준 영수증 출력
* `Thymeleaf`를 활용한 [소득세법 시행규칙 별지 제45호의2서식] 완벽 구현
* 교인 번호 조회 한 번으로 해당 연도 헌금 총액 자동 계산 및 영수증 렌더링
* 인쇄(Print) 전용 CSS를 적용하여 깔끔한 A4 용지 출력 지원

## 🛠 기술 스택 (Tech Stack)
* **Backend:** Java 25, Spring Boot, Spring Data JPA, Spring Security
* **Frontend:** HTML5, CSS3, Thymeleaf (Server-side Rendering)
* **Database:** MySQL 8.0
* **Infrastructure:** Docker, Docker Compose
* **External API:** Solapi (문자 발송 서비스)

---

## 🚀 설치 및 실행 방법 (How to Run)

CDRS는 누구나 쉽게 사용할 수 있도록 **Docker Compose 기반의 설치 패키지**를 제공합니다.

### 1. 사전 준비
* PC 또는 서버에 [Docker Desktop](https://www.docker.com/products/docker-desktop/)이 설치되어 있어야 합니다.

### 2. 설치 패키지 다운로드
* 우측 **[Releases]** 탭에서 최신 버전의 `CDRS_설치프로그램.zip` 파일을 다운로드하고 압축을 해제합니다.

### 3. 시스템 구동
압축을 푼 폴더에서 터미널(또는 명령 프롬프트)을 열고 아래 명령어를 입력합니다.
```bash
docker compose up -d

### 4. 접속 후 설정
관리자 페이지로 들어간후 솔라피 api와 이후의 웹 배포를 진행해주세요
