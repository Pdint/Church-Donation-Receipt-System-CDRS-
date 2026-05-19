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
function verifyCode() {
    const inputCode = document.getElementById("authCode").value;

    if (!inputCode) {
        alert("인증번호를 입력해주세요.");
        return;
    }

    // [참고] 실제 보안을 위해서는 인증번호 확인도 백엔드에서 수행해야 합니다.
    // 지금은 우선 프론트에서 흐름만 잡는 용도로 작성되었습니다.
    if (timeLeft > 0) {
        alert("인증되었습니다! 기부금 영수증 출력 페이지로 이동합니다.");
    } else {
        alert("인증 시간이 지났습니다.");
    }
}