// 장바구니 항목을 가져와서 표시하는 함수
async function fetchCartItems() {
    try {
        const cartItems = await fetchApi('/order-service/carts/products');
        const regularCartItem = cartItems.data; // ResponseDto에서 data를 가져옴
        displayCartItems(regularCartItem);
    } catch (error) {
        handleApiError(error);
    }
}

// 장바구니 항목을 화면에 표시하는 함수
function displayCartItems(cartItems) {  
    const container = document.getElementById('cart-items');
    const defaultImageUrl = 'https://cdsassets.apple.com/live/SZLF0YNV/images/sp/111883_macbookair.png';

    if (!cartItems || cartItems.length === 0) {
        container.innerHTML = '<p>상품이 없습니다.</p>';
        return;
    }

    container.innerHTML = cartItems.map(item => {
        const imageUrl = item.imageUrl || defaultImageUrl;

        return `<div class="cart-item" data-product-id="${item.productId}">
            <input type="checkbox" class="select-item">
            <img src="${imageUrl}" alt="${item.name}">
            <h3>${item.name}</h3>
            <p>${formatPrice(item.price)}</p>
            <input type="number" class="quantity" value="${item.quantity}" min="1">
            <button class="update-quantity">수정</button>
            <button class="remove-item">삭제</button>
        </div>`;
    }).join('');

    setupCartItemEvents();
}

// 장바구니 항목의 이벤트를 설정하는 함수
function setupCartItemEvents() {
    document.querySelectorAll('.update-quantity').forEach(button => {
        button.addEventListener('click', updateCartItem);
    });

    document.querySelectorAll('.remove-item').forEach(button => {
        button.addEventListener('click', removeCartItem);
    });
}

// 장바구니 항목의 수량을 업데이트하는 함수
async function updateCartItem(e) {
    const cartItem = e.target.closest('.cart-item');
    const productId = cartItem.dataset.productId;
    const quantity = cartItem.querySelector('.quantity').value;

    try {
        await fetchApi('/order-service/carts', 'PATCH', {
            productId: parseInt(productId),
            count: parseInt(quantity)
        });
        showToast('장바구니가 업데이트되었습니다.', 'success');
    } catch (error) {
        handleApiError(error);
    }
}

// 장바구니 항목을 제거하는 함수
async function removeCartItem(e) {
    const cartItem = e.target.closest('.cart-item');
    const productId = cartItem.dataset.productId;

    try {
        await fetchApi('/order-service/carts', 'DELETE', [parseInt(productId)]);
        cartItem.remove();
        showToast('상품이 장바구니에서 제거되었습니다.', 'success');
    } catch (error) {
        handleApiError(error);
    }
}

// 페이지 로드 시 장바구니 항목을 가져오는 함수
document.addEventListener('DOMContentLoaded', fetchCartItems);
