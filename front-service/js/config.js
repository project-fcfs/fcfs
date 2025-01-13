const BASE_URL = 'http://localhost:8000';

function setBaseUrl(newUrl) {
    BASE_URL = newUrl;
}

async function fetchApi(endpoint, method = 'GET', body = null) {
    const url = `${BASE_URL}${endpoint}`;
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    const data = await response.json();

    if (data.code !== 1) {
        console.error('API Error:', data.message);
        throw new Error(data.message);
    }

    return data.data;
}
