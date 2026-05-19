let generatedCode = ""; // 서버에서 받아온 인증번호 (확인용)
let timeLeft = 180;      // 3분 (초 단위)
let timerInterval;

/**
 * 1. 인증번호 발송 요청 (백엔드와 통신)
 */
async function sendSms() {
    const name = document.getElementById("name").value;
    const phone = document.getElementById("phone").value;

    if (!name || !phone) {
        alert("성명과 휴대폰 번호를 모두 입력해주세요.");
        return;
    }

    try {
        // 백엔드 Controller(@PostMapping("/api/auth/send-sms")) 호출
        const response = await fetch('/api/auth/send-sms', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: name,
                phone: phone
            })
        });

        if (response.ok) {
            alert("인증번호가 발송되었습니다. (실제 문자를 확인하세요!)");

            // 타이머 초기화 및 시작
            clearInterval(timerInterval);
            timeLeft = 180;
            startTimer();
        } else {
            const errorText = await response.text();
            alert("발송 실패: " + errorText);
        }
    } catch (error) {
        console.error("통신 에러:", error);
        alert("서버와 연결할 수 없습니다. 백엔드가 실행 중인지 확인하세요.");
    }
}

/**
 * 2. 3분 타이머 구동
 */
function startTimer() {
    const timerDisplay = document.getElementById("timer");

    timerInterval = setInterval(() => {
        let minutes = Math.floor(timeLeft / 60);
        let seconds = timeLeft % 60;

        // 00:00 형식으로 표시
        timerDisplay.innerText = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;

        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            timerDisplay.innerText = "만료";
            alert("인증 시간이 만료되었습니다. 다시 시도해주세요.");
        }
        timeLeft--;
    }, 1000);
}

/**
 * 3. 인증번호 확인 로직
 */
async function verifyCode() {
    const name = document.getElementById("name").value;         // 👈 HTML에서 이름 가져오기
    const birth = document.getElementById("birth").value;       // 👈 HTML에서 생년월일 가져오기
    const phone = document.getElementById("phone").value;
    const inputCode = document.getElementById("authCode").value;

    if (!inputCode) {
        alert("인증번호를 입력해주세요.");
        return;
    }

    if (timeLeft <= 0) {
        alert("인증 시간이 지났습니다.");
        return;
    }

    try {
        const response = await fetch('/api/auth/verify', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: name,          // 👈 서버의 AuthVerifyRequest.name 과 매핑
                birthDate: birth,    // 👈 서버의 AuthVerifyRequest.birthDate 와 매핑
                phone: phone,
                code: inputCode
            })
        });

        if (response.ok) {
            const memberData = await response.json();

            alert("인증되었습니다! 기부금 영수증 페이지로 이동합니다.");
            window.location.href = `/receipt/${memberData.memberId}?year=2026`;

        } else {
            alert("입력하신 정보와 일치하는 교인이 없거나 인증번호가 올바르지 않습니다.");
        }
    } catch (error) {
        console.error("인증 에러:", error);
    }
}