async function fetchPaymentHistory() {
    try {
        const orders = await fetchApi('/order-service');
        displayPaymentHistory(orders);
    } catch (error) {
        handleApiError(error);
    }
}

function displayPaymentHistory(orders) {
    const container = document.getElementById('payment-history');
    container.innerHTML = orders.map(order => `
        <div class="order-item">
            <h3>주문 ID: ${order.orderId}</h3>
            <p>주문 상태: ${order.orderStatus}</p>
            <p>총 금액: ${formatPrice(order.amount)}</p>
            <p>주문 일시: ${new Date(order.updatedAt).toLocaleString()}</p>
            <button class="cancel-order" data-order-id="${order.orderId}">주문 취소</button>
            <button class="refund-order" data-order-id="${order.orderId}">환불 요청</button>
        </div>
    `).join('');

    setupOrderActions();
}

function setupOrderActions() {
    document.querySelectorAll('.cancel-order').forEach(button => {
        button.addEventListener('click', cancelOrder);
    });

    document.querySelectorAll('.refund-order').forEach(button => {
        button.addEventListener('click', refundOrder);
    });
}

async function cancelOrder(e) {
    const orderId = e.target.dataset.orderId;
    try {
        await fetchApi(`/order-service/cancel/${orderId}`, 'POST');
        showToast('주문이 취소되었습니다.', 'success');
        fetchPaymentHistory();
    } catch (error) {
        handleApiError(error);
    }
}

async function refundOrder(e) {
    const orderId = e.target.dataset.orderId;
    try {
        await fetchApi(`/order-service/refund/${orderId}`, 'POST');
        showToast('환불 요청이 처리되었습니다.', 'success');
        fetchPaymentHistory();
    } catch (error) {
        handleApiError(error);
    }
}

document.addEventListener('DOMContentLoaded', fetchPaymentHistory);
