export const API_BASE_URL = "http://localhost:8080";

const TOKEN_KEY = "accessToken";

export function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

export function setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

export function clearToken() {
    localStorage.removeItem(TOKEN_KEY);
}

export async function apiFetch(path, options = {}) {
    const headers = { "Content-Type": "application/json", ...(options.headers || {}) };
    const token = getToken();
    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }

    let response;
    try {
        response = await fetch(`${API_BASE_URL}${path}`, { ...options, headers });
    } catch (networkError) {
        throw new Error("서버에 연결할 수 없습니다. 잠시 후 다시 시도해주세요.");
    }

    if (response.status === 204) {
        return null;
    }

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        const message = data && data.message ? data.message : "요청 처리 중 오류가 발생했습니다.";
        throw new Error(message);
    }

    return data;
}
