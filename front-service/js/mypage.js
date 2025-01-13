document.addEventListener('DOMContentLoaded', function() {
    fetchUserInfo();
    fetchOrderHistory();
});

async function fetchUserInfo() {
    try {
        const response = await fetchApi('/user-service/user/mypage');
        console.log(response);
        displayUserInfo(response);
    } catch (error) {
        console.error('사용자 정보를 불러오는 데 실패했습니다:', error);
    }
}

function displayUserInfo(userData) {
    const userDetails = document.getElementById('user-details');
    userDetails.innerHTML = `
        <p><strong>이름:</strong> ${userData.name}</p>
        <p><strong>이메일:</strong> ${userData.email}</p>
        <p><strong>주소:</strong> ${userData.address}</p>
        <p><strong>가입일:</strong> ${new Date(userData.createdAt).toLocaleDateString()}</p>
    `;
}

async function fetchOrderHistory() {
    try {
        const response = await fetchApi('/order-service/orders');
        const orders = await response.data;
        displayOrderHistory(orders);
    } catch (error) {
        console.error('주문 내역을 불러오는 데 실패했습니다:', error);
    }
}

function displayOrderHistory(orders) {
    const orderList = document.getElementById('order-list');
    console.log(orders);
    orderList.innerHTML = orders.map(order => `
        <li>
            <p><strong>주문 번호:</strong> ${order.orderId}</p>
            <p><strong>주문 상태:</strong> ${order.orderStatus}</p>
            <p><strong>주문 날짜:</strong> ${new Date(order.updatedAt).toLocaleDateString()}</p>
            <p><strong>총 금액:</strong> ${order.amount.toLocaleString()}원</p>
            <p><strong>상품:</strong> ${order.products.map(product => product.orderPrice || '상품명 없음').join(', ')}</p>
        </li>
    `).join('');
}
