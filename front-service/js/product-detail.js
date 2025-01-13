const productId = new URLSearchParams(window.location.search).get('id');

async function fetchProductDetail() {
    try {
        const response = await fetchApi(`/product-service/products/${productId}`);
        const product = response.data; // ResponseDto에서 data를 가져옴
        displayProductDetail(product);
    } catch (error) {
        handleApiError(error);
    }
}

function displayProductDetail(product) {
    const container = document.getElementById('product-detail');

    // 기본 이미지 URL
    const defaultImageUrl = 'https://cdsassets.apple.com/live/SZLF0YNV/images/sp/111883_macbookair.png';
    
    // 이미지 URL이 없으면 기본 이미지 사용
    const imageUrl = product.imageUrl || defaultImageUrl;

    container.innerHTML = `
        <img src="${imageUrl}" alt="${product.name}">
        <h2>${product.name}</h2>
        <p>가격: ${formatPrice(product.price)}</p>
        <p>수량: ${product.quantity}</p>
        <p>상태: ${product.status}</p>
        <div class="quantity-control">
            <button id="decrease">-</button>
            <input type="number" id="quantity" value="1" min="1" max="${product.quantity}">
            <button id="increase">+</button>
        </div>
        <button id="add-to-cart" class="btn">장바구니 추가</button>
        <button id="buy-now" class="btn">바로 구매</button>
    `;

    setupQuantityControl();
    setupAddToCart(product);
    setupBuyNow(product);
}


function setupQuantityControl() {
    const quantity = document.getElementById('quantity');
    document.getElementById('decrease').addEventListener('click', () => {
        if (quantity.value > 1) quantity.value--;
    });
    document.getElementById('increase').addEventListener('click', () => {
        if (quantity.value < quantity.max) quantity.value++;
    });
}

function setupAddToCart(product) {
    document.getElementById('add-to-cart').addEventListener('click', async () => {
        const quantity = document.getElementById('quantity').value;
        try {
            await fetchApi('/order-service/carts', 'POST', {
                productId: product.productId,
                count: parseInt(quantity)
            });
            showToast('장바구니에 추가되었습니다.', 'success');
        } catch (error) {
            handleApiError(error);
        }
    });
}

function setupBuyNow(product) {
    document.getElementById('buy-now').addEventListener('click', () => {
        const quantity = document.getElementById('quantity').value;
        localStorage.setItem('buyNow', JSON.stringify({
            productId: product.productId,
            count: parseInt(quantity)
        }));
        window.location.href = 'order.html';
    });
}

document.addEventListener('DOMContentLoaded', fetchProductDetail);
