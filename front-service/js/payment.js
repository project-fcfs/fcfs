async function processPayment() {
    const urlParams = new URLSearchParams(window.location.search);
    const orderId = urlParams.get('orderId');
    const amount = urlParams.get('amount');
    
    try {
        const payment = await fetchApi('/payment-service/payments', 'POST', {
            orderId: orderId,
            amount: amount
        });
        displayPaymentResult(payment);
    } catch (error) {
        handleApiError(error);
    }
}

function displayPaymentResult(payment) {
    const container = document.getElementById('payment-result');
    console.log(payment);
    container.innerHTML = `
        <h2>결제 완료</h2>
        <p>결제 ID: ${payment.paymentId}</p>
        <p>주문 ID: ${payment.orderId}</p>
        <p>결제 금액: ${formatPrice(payment.amount)}</p>
        <p>결제 상태: ${payment.status}</p>
        <p>결제 일시: ${new Date(payment.createdAt).toLocaleString()}</p>
        <a href="index.html" class="btn">쇼핑 계속하기</a>
    `;
}

document.addEventListener('DOMContentLoaded', processPayment);
