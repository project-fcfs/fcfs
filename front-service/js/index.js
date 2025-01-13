async function fetchProducts() {
    try {
        // 일반 상품과 선착순 특가 상품을 각각의 API 엔드포인트에서 가져옵니다.
        const regularResponse = await fetchApi('/product-service/products');
        const fcfsResponse = await fetchApi('/product-service/products/fcfs'); // 수정된 엔드포인트

        // 응답에서 data 속성을 추출합니다.
        const regularProducts = regularResponse.data; // ResponseDto에서 data를 가져옴
        const fcfsProducts = fcfsResponse.data; // ResponseDto에서 data를 가져옴

        // 가져온 상품들을 각각의 섹션에 표시합니다.
        displayProducts(regularProducts, 'regular-products');
        displayfcfsProducts(fcfsProducts, 'fcfs-products');
    } catch (error) {
        handleApiError(error); // 에러 처리
    }
}

function displayProducts(products, containerId) {
    const container = document.getElementById(containerId);
    // 기본 이미지 URL
    const defaultImageUrl = 'https://cdsassets.apple.com/live/SZLF0YNV/images/sp/111883_macbookair.png';
    
    // 제품이 없을 경우 메시지 표시
    if (!products || products.length === 0) {
        container.innerHTML = '<p>상품이 없습니다.</p>';
        return;
    }

    container.innerHTML = products.map(product => {
        // 이미지가 없을 경우 기본 이미지 사용
        const imageUrl = product.imageUrl || defaultImageUrl;

        return `
            <div class="product-card">
                <img src="${imageUrl}" alt="${product.name}">
                <h3>${product.name}</h3>
                <p>${formatPrice(product.price)}</p>
                <p>수량: ${product.quantity}</p>
                <p>상태: ${product.status}</p>
                <a href="product-detail.html?id=${product.productId}" class="btn">상세보기</a>
            </div>
        `;
    }).join(''); // 상품 카드 HTML 생성
}

function displayfcfsProducts(products, containerId) {
    const container = document.getElementById(containerId);
    // 기본 이미지 URL
    const defaultImageUrl = 'https://cdsassets.apple.com/live/SZLF0YNV/images/sp/111883_macbookair.png';
    
    // 제품이 없을 경우 메시지 표시
    if (!products || products.length === 0) {
        container.innerHTML = '<p>상품이 없습니다.</p>';
        return;
    }

    container.innerHTML = products.map(product => {
        // 이미지가 없을 경우 기본 이미지 사용
        const imageUrl = product.imageUrl || defaultImageUrl;

        return `
            <div class="product-card">
                <img src="${imageUrl}" alt="${product.name}">
                <h3>${product.name}</h3>
                <p>${formatPrice(product.price)}</p>
                <p>수량: ${product.quantity}</p>
                <p>상태: ${product.status}</p>
                <a href="product-fcfs.html?id=${product.productId}" class="btn">상세보기</a>
            </div>
        `;
    }).join(''); // 상품 카드 HTML 생성
}

// 페이지 로드 시 fetchProducts 함수를 호출하여 상품을 가져옵니다.
document.addEventListener('DOMContentLoaded', fetchProducts);
