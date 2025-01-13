async function createOrder() {

    if (buyNow) {
        productIds = [buyNow.productId];
        const order = await fetchApi('/product-service/products/cart', 'GET', productIds);
        orderData = order.data;
    } else {
        const cartItems = await fetchApi('/order-service/carts/products', 'GET');
        orderData = cartItems.data;
    }
    
    productIds = orderData.map(product => product.productId);

    // 주문 요약 표시
    displayOrderSummary(orderData, productIds);
    
}

function displayOrderSummary(order, productIds) {
    const container = document.getElementById('order-summary');
    
    // 상품 총합계 변수 초기화
    let totalAmount = 0;

    // 주문 데이터가 배열인지 확인
    if (!Array.isArray(order)) {
        console.error("주문 상품 목록이 올바르지 않습니다.");
        container.innerHTML = "<p>주문 상품 목록이 올바르지 않습니다.</p>";
        return;
    }

    // 기본 이미지 URL 설정
    const defaultImageUrl = 'https://cdsassets.apple.com/live/SZLF0YNV/images/sp/111883_macbookair.png';

    // 주문 요약 HTML 생성
    container.innerHTML = `
        <h3>주문 상품</h3>
        <ul>
            ${order.map(product => {
                const productTotal = product.price * product.quantity; // 각 상품의 총 가격 계산
                totalAmount += productTotal; // 전체 금액에 추가
                const imageUrl = product.imageUrl || defaultImageUrl; // 이미지 URL 설정
                return `
                    <li class="order-product">
                        <img src="${imageUrl}" alt="${product.name}" style="width: 50px; height: 50px;">
                        <div>
                            <h4>${product.name}</h4>
                            <p>가격: ${formatPrice(product.price)}</p>
                            <p>수량: ${product.quantity}</p>
                            <p>합계: ${formatPrice(productTotal)}</p>
                        </div>
                    </li>
                `;
            }).join('')}
        </ul>
        <h4>총 합계: ${formatPrice(totalAmount)}</h4> <!-- 전체 합계 표시 -->
        <button id="proceed-to-payment" class="btn">결제 진행</button>
    `;

    // 결제 진행 버튼 클릭 이벤트 리스너 추가
    document.getElementById('proceed-to-payment').addEventListener('click', async () => {
        try {
            // 전달할 데이터 준비
            const address = "address"; // address 값 설정 (사용자가 입력한 주소)
            
            const productIdsData = {
                productIds: productIds, // 배열을 필드로 포함
            };

            console.log(productIdsData);
    
            // POST 요청
            const response = await fetchApi(`/order-service/orders/fcfs?address=${encodeURIComponent(address)}`, 'POST', productIdsData);
            
            if (response) {
                console.log(response);
                // 결제 진행을 위한 페이지로 리다이렉트
                window.location.href = `payment.html?orderId=${response.orderId}&amount=${response.amount}`;
            } else {
                throw new Error("결제 진행 중 오류가 발생했습니다.");
            }
        } catch (error) {
            console.error("에러:", error);
            alert("결제 진행에 실패했습니다.");
        }
    });
}

document.addEventListener('DOMContentLoaded', createOrder);
