이 프로젝트는 로컬 데이터베이스에서 교회 기부금 명세서 자동화를 위한 프로젝트임

설계:
domain: 시스템의 핵심 데이터 모델 (Entity)

    Member.java, Donation.java, Receipt.java

controller: 웹 요청(프론트엔드 통신) 처리

    AuthController.java, DonationController.java

service: 핵심 비즈니스 로직 (인증번호 생성, 헌금 합계 계산 등)

    MemberService.java, AuthService.java, SmsService.java

repository: DB 데이터 접근 (JPA 인터페이스)

    MemberRepository.java, DonationRepository.java

dto: 계층 간 데이터 전송용 가벼운 객체

    SmsRequest.java, LoginResponse.java

infra: 외부 서비스 연동 (Solapi, 가상 PASS 모듈 등)

    SolapiClient.java, VerificationManager.java

global: 공통 예외 처리, 보안 설정, 유틸리티

    GlobalExceptionHandler.java, SecurityConfig.java
