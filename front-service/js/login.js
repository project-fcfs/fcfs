const loginForm = document.getElementById('login-form');

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault(); // 기본 폼 제출 방지
    const formData = new FormData(loginForm);
    const loginData = Object.fromEntries(formData.entries());

    try {
        // customFetchApi를 사용하여 로그인 요청 (POST)
        const token = await LoginfetchApi('/user-service/api/login', 'POST', loginData);
        
        if (token) {
            localStorage.setItem('token', token); // 토큰 저장
            showToast('로그인 성공했습니다!', 'success'); // 성공 메시지 표시
            
            // 즉시 메인 페이지로 리다이렉트
            window.location.href = 'index.html'; // 메인 페이지로 즉시 리다이렉트
        } else {
            throw new Error('No token received');
        }
    } catch (error) {
        handleApiError(error); // 에러 처리
    }
});
