function showToast(message, type = 'info') {
    // 토스트 메시지 표시 로직
}

function formatPrice(price) {
    return price.toLocaleString() + '원';
}

function handleApiError(error) {
    showToast(error.message, 'error');
}

function updateNavMenu() {
    const token = localStorage.getItem('token');
    const guestElements = document.querySelectorAll('.guest-only');
    const userElements = document.querySelectorAll('.user-only');

    if (token) {
        guestElements.forEach(el => el.style.display = 'none');
        userElements.forEach(el => el.style.display = 'inline-block');
    } else {
        guestElements.forEach(el => el.style.display = 'inline-block');
        userElements.forEach(el => el.style.display = 'none');
    }
}

async function LoginfetchApi(endpoint, method, body) {
    const url = `${BASE_URL}${endpoint}`;
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        }

    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    const token = response.headers.get('Authorization');

    
    if (!response.ok) {
        throw new Error(data.message || 'API request failed');
    }

    console.log(token);

    return token;
}

async function fetchApi(endpoint, method = 'GET', body = null) {
    const url = `${BASE_URL}${endpoint}`;
    const token = localStorage.getItem('token');
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        }
    };

    if (token) {
        options.headers['Authorization'] = token;
    }

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'API request failed');
    }

    return data;
}

document.addEventListener('DOMContentLoaded', () => {
    updateNavMenu();

    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('token');
            updateNavMenu();
            showToast('로그아웃되었습니다.', 'success');
        });
    }
});

async function logout() {
    const logoutBtn = document.getElementById('logout-btn');

    if(logoutBtn){
        logoutBtn.addEventListener('click', (e) => {
            try{
                e.preventDefault();
                fetchApi('/user-service/logout', 'POST'); // 서버에 로그아웃 요청
                localStorage.removeItem('token');
                updateNavMenu();
                showToast('로그아웃되었습니다.', 'success');
                window.location.href = 'index.html'; // 메인 페이지로 리다이렉트
            }catch (error) {
                handleApiError(error); // 에러 처리
            }
           
        });
    }
}

function setupCartButton() {
    const cartButton = document.getElementById('cart-button'); // 장바구니 버튼의 ID를 확인하세요.

    if (cartButton) {
        cartButton.addEventListener('click', (e) => {
            e.preventDefault(); // 기본 동작 방지

            // 로컬 스토리지에서 토큰 확인
            const token = localStorage.getItem('token');
            if (!token) {
                // 토큰이 없으면 로그인 페이지로 리다이렉트
                window.location.href = 'login.html';
            } else {
                // 토큰이 있으면 장바구니 페이지로 이동 (예: cart.html)
                window.location.href = 'cart.html';
            }
        });
    }
}

async function loadHeader() {
    const response = await fetch('header.html');
    const headerHtml = await response.text();
    
    document.getElementById('header').innerHTML = headerHtml;

    // 네비게이션 메뉴 업데이트 및 장바구니 버튼 클릭 이벤트 리스너 설정
    updateNavMenu();
    setupCartButton(); // 장바구니 버튼 설정 함수 호출
    logout();
}

document.addEventListener('DOMContentLoaded', loadHeader);
