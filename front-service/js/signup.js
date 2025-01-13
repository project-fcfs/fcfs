const signupForm = document.getElementById('signup-form');
const requestAuthCodeBtn = document.getElementById('request-auth-code');
const authCodeSection = document.getElementById('auth-code-section');
const additionalInfoSection = document.getElementById('additional-info');
const timerElement = document.getElementById('timer');

let timer;

requestAuthCodeBtn.addEventListener('click', async () => {
    const email = document.getElementById('email').value;
    if (!email) {
        showToast('이메일을 입력해주세요.', 'error');
        return;
    }

    try {
        await fetchApi('/user-service/authcode', 'POST', { email });
        showToast('인증 코드가 이메일로 전송되었습니다.', 'success');
        authCodeSection.style.display = 'block';
        additionalInfoSection.style.display = 'block';
        startTimer();
    } catch (error) {
        handleApiError(error);
    }
});

signupForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(signupForm);
    const signupData = Object.fromEntries(formData.entries());

    try {
        const response = await fetchApi('/user-service/signup', 'POST', signupData);
        if (response.code === 1) {
            showToast('회원가입이 완료되었습니다.', 'success');
            window.location.href = 'login.html';
        } else {
            showToast(response.message, 'error');
        }
    } catch (error) {
        handleApiError(error);
    }
});

function startTimer() {
    let time = 300; // 5분
    timerElement.textContent = formatTime(time);

    timer = setInterval(() => {
        time--;
        timerElement.textContent = formatTime(time);

        if (time <= 0) {
            clearInterval(timer);
            showToast('인증 시간이 만료되었습니다. 다시 시도해주세요.', 'error');
            authCodeSection.style.display = 'none';
            additionalInfoSection.style.display = 'none';
        }
    }, 1000);
}

function formatTime(seconds) {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;
}
