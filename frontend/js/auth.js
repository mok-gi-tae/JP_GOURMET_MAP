import { apiFetch, getToken, setToken, clearToken } from "./api.js";

let currentUser = null;

export function getCurrentUser() {
    return currentUser;
}

export function isLoggedIn() {
    return currentUser !== null;
}

// 저장된 토큰이 있으면 /api/auth/me 로 로그인 상태를 복원한다.
export async function restoreSession() {
    if (!getToken()) {
        return null;
    }
    try {
        currentUser = await apiFetch("/api/auth/me");
    } catch (error) {
        clearToken();
        currentUser = null;
    }
    return currentUser;
}

export async function signup({ email, password, nickname }) {
    return apiFetch("/api/auth/signup", {
        method: "POST",
        body: JSON.stringify({ email, password, nickname }),
    });
}

export async function login({ email, password }) {
    const data = await apiFetch("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, password }),
    });
    setToken(data.accessToken);
    currentUser = data.user;
    return currentUser;
}

export function logout() {
    clearToken();
    currentUser = null;
}
