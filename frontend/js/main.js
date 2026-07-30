import { getCurrentUser, isLoggedIn, restoreSession, signup, login, logout } from "./auth.js";
import { initMap, loadRegions, flyToRegion, renderRestaurantMarkers } from "./map.js";
import { fetchRestaurantsByRegion, fetchRestaurantDetail } from "./restaurant.js";
import { fetchReviews, createReview, updateReview, deleteReview } from "./review.js";

const dom = {
    guestArea: document.getElementById("guest-area"),
    userArea: document.getElementById("user-area"),
    userNickname: document.getElementById("user-nickname"),
    loginBtn: document.getElementById("login-btn"),
    signupBtn: document.getElementById("signup-btn"),
    logoutBtn: document.getElementById("logout-btn"),

    restaurantListPanel: document.getElementById("restaurant-list-panel"),
    regionName: document.getElementById("region-name"),
    restaurantList: document.getElementById("restaurant-list"),
    closeListPanel: document.getElementById("close-list-panel"),

    restaurantModal: document.getElementById("restaurant-modal"),
    detailName: document.getElementById("detail-name"),
    detailCategory: document.getElementById("detail-category"),
    detailAddress: document.getElementById("detail-address"),
    detailTabelogScore: document.getElementById("detail-tabelog-score"),
    detailTabelogUrl: document.getElementById("detail-tabelog-url"),
    detailYoutubeUrl: document.getElementById("detail-youtube-url"),
    detailAvgRating: document.getElementById("detail-avg-rating"),
    detailReviewCount: document.getElementById("detail-review-count"),

    reviewList: document.getElementById("review-list"),
    reviewForm: document.getElementById("review-form"),
    reviewRating: document.getElementById("review-rating"),
    reviewContent: document.getElementById("review-content"),
    reviewSubmitBtn: document.getElementById("review-submit-btn"),
    reviewCancelBtn: document.getElementById("review-cancel-btn"),
    reviewLoginHint: document.getElementById("review-login-hint"),

    authModal: document.getElementById("auth-modal"),
    loginFormWrap: document.getElementById("login-form-wrap"),
    signupFormWrap: document.getElementById("signup-form-wrap"),
    loginForm: document.getElementById("login-form"),
    loginEmail: document.getElementById("login-email"),
    loginPassword: document.getElementById("login-password"),
    signupForm: document.getElementById("signup-form"),
    signupEmail: document.getElementById("signup-email"),
    signupPassword: document.getElementById("signup-password"),
    signupNickname: document.getElementById("signup-nickname"),
    switchToSignup: document.getElementById("switch-to-signup"),
    switchToLogin: document.getElementById("switch-to-login"),

    toast: document.getElementById("toast"),
};

let currentRestaurantId = null;
let currentReviews = [];
let editingReviewId = null;
let toastTimer = null;

async function init() {
    initMap();
    await restoreSession();
    updateAuthUI();

    try {
        await loadRegions(handleRegionClick);
    } catch (error) {
        showToast(error.message, true);
    }

    bindEvents();
}

function bindEvents() {
    dom.loginBtn.addEventListener("click", () => {
        showAuthForm("login");
        openModal(dom.authModal);
    });
    dom.signupBtn.addEventListener("click", () => {
        showAuthForm("signup");
        openModal(dom.authModal);
    });
    dom.logoutBtn.addEventListener("click", handleLogout);
    dom.switchToSignup.addEventListener("click", (event) => {
        event.preventDefault();
        showAuthForm("signup");
    });
    dom.switchToLogin.addEventListener("click", (event) => {
        event.preventDefault();
        showAuthForm("login");
    });
    dom.loginForm.addEventListener("submit", handleLoginSubmit);
    dom.signupForm.addEventListener("submit", handleSignupSubmit);

    dom.closeListPanel.addEventListener("click", () => {
        dom.restaurantListPanel.classList.add("hidden");
    });

    dom.reviewForm.addEventListener("submit", handleReviewFormSubmit);
    dom.reviewCancelBtn.addEventListener("click", () => {
        renderReviews(currentReviews);
    });

    // data-close 속성을 가진 배경/버튼 클릭 시 해당 모달을 닫는다.
    document.addEventListener("click", (event) => {
        const closeTargetId = event.target.dataset.close;
        if (closeTargetId) {
            document.getElementById(closeTargetId).classList.add("hidden");
        }
    });
}

/* ---------- 지역 / 맛집 ---------- */

async function handleRegionClick(region) {
    flyToRegion(region);
    dom.regionName.textContent = `${region.name} (${region.city})`;
    dom.restaurantListPanel.classList.remove("hidden");

    try {
        const restaurants = await fetchRestaurantsByRegion(region.id);
        renderRestaurantMarkers(restaurants, openRestaurantDetail);
        renderRestaurantList(restaurants);
    } catch (error) {
        showToast(error.message, true);
    }
}

function renderRestaurantList(restaurants) {
    dom.restaurantList.innerHTML = "";

    if (restaurants.length === 0) {
        const li = document.createElement("li");
        li.className = "empty-hint";
        li.textContent = "이 지역에는 아직 등록된 맛집이 없습니다.";
        dom.restaurantList.appendChild(li);
        return;
    }

    restaurants.forEach((restaurant) => {
        const li = document.createElement("li");

        const nameEl = document.createElement("span");
        nameEl.className = "r-name";
        nameEl.textContent = restaurant.name;

        const metaEl = document.createElement("span");
        metaEl.className = "r-meta";
        metaEl.textContent = `${restaurant.category} · 타베로그 ${formatRating(restaurant.tabelogScore)}`;

        li.appendChild(nameEl);
        li.appendChild(metaEl);
        li.addEventListener("click", () => openRestaurantDetail(restaurant.id));
        dom.restaurantList.appendChild(li);
    });
}

async function openRestaurantDetail(restaurantId) {
    currentRestaurantId = restaurantId;
    try {
        const [detail, reviews] = await Promise.all([
            fetchRestaurantDetail(restaurantId),
            fetchReviews(restaurantId),
        ]);
        currentReviews = reviews;
        renderRestaurantDetail(detail);
        renderReviews(reviews);
        openModal(dom.restaurantModal);
    } catch (error) {
        showToast(error.message, true);
    }
}

async function refreshRestaurantDetail() {
    if (!currentRestaurantId) {
        return;
    }
    const [detail, reviews] = await Promise.all([
        fetchRestaurantDetail(currentRestaurantId),
        fetchReviews(currentRestaurantId),
    ]);
    currentReviews = reviews;
    renderRestaurantDetail(detail);
    renderReviews(reviews);
}

function renderRestaurantDetail(detail) {
    dom.detailName.textContent = detail.name;
    dom.detailCategory.textContent = detail.category;
    dom.detailAddress.textContent = detail.address;
    dom.detailTabelogScore.textContent = formatRating(detail.tabelogScore);

    setOptionalLink(dom.detailTabelogUrl, detail.tabelogUrl);
    setOptionalLink(dom.detailYoutubeUrl, detail.youtubeUrl);

    dom.detailAvgRating.textContent = formatRating(detail.averageUserRating);
    dom.detailReviewCount.textContent = detail.reviewCount;
}

function setOptionalLink(anchorEl, url) {
    if (url) {
        anchorEl.href = url;
        anchorEl.classList.remove("hidden");
    } else {
        anchorEl.removeAttribute("href");
        anchorEl.classList.add("hidden");
    }
}

/* ---------- 리뷰 ---------- */

function renderReviews(reviews) {
    const user = getCurrentUser();
    dom.reviewList.innerHTML = "";

    if (reviews.length === 0) {
        const empty = document.createElement("li");
        empty.className = "muted";
        empty.textContent = "아직 작성된 리뷰가 없습니다.";
        dom.reviewList.appendChild(empty);
    }

    let myReview = null;

    reviews.forEach((review) => {
        const isMine = Boolean(user) && user.nickname === review.nickname;
        if (isMine) {
            myReview = review;
        }

        const li = document.createElement("li");
        li.className = "review-item";

        const head = document.createElement("div");
        head.className = "review-item-head";

        const nicknameEl = document.createElement("span");
        nicknameEl.className = "review-nickname";
        nicknameEl.textContent = review.nickname;

        const ratingEl = document.createElement("span");
        ratingEl.className = "review-rating";
        ratingEl.textContent = `★ ${formatRating(review.rating)}`;

        head.appendChild(nicknameEl);
        head.appendChild(ratingEl);

        const contentEl = document.createElement("p");
        contentEl.className = "review-content";
        contentEl.textContent = review.content;

        li.appendChild(head);
        li.appendChild(contentEl);

        if (isMine) {
            const actions = document.createElement("div");
            actions.className = "review-item-actions";

            const editBtn = document.createElement("button");
            editBtn.type = "button";
            editBtn.className = "btn btn-outline";
            editBtn.textContent = "수정";
            editBtn.addEventListener("click", () => startEditReview(review));

            const deleteBtn = document.createElement("button");
            deleteBtn.type = "button";
            deleteBtn.className = "btn btn-danger";
            deleteBtn.textContent = "삭제";
            deleteBtn.addEventListener("click", () => handleDeleteReview(review.id));

            actions.appendChild(editBtn);
            actions.appendChild(deleteBtn);
            li.appendChild(actions);
        }

        dom.reviewList.appendChild(li);
    });

    updateReviewFormVisibility(myReview);
}

function updateReviewFormVisibility(myReview) {
    editingReviewId = null;
    dom.reviewForm.reset();
    dom.reviewSubmitBtn.textContent = "등록";
    dom.reviewCancelBtn.classList.add("hidden");

    if (!isLoggedIn()) {
        dom.reviewForm.classList.add("hidden");
        dom.reviewLoginHint.classList.remove("hidden");
        return;
    }

    dom.reviewLoginHint.classList.add("hidden");

    if (myReview) {
        // 이미 이 맛집에 리뷰를 작성했으면, 새 작성 폼 대신 위 목록의 수정/삭제 버튼을 사용한다.
        dom.reviewForm.classList.add("hidden");
    } else {
        dom.reviewForm.classList.remove("hidden");
    }
}

function startEditReview(review) {
    editingReviewId = review.id;
    dom.reviewForm.classList.remove("hidden");
    dom.reviewRating.value = review.rating;
    dom.reviewContent.value = review.content;
    dom.reviewSubmitBtn.textContent = "수정 완료";
    dom.reviewCancelBtn.classList.remove("hidden");
    dom.reviewForm.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

async function handleReviewFormSubmit(event) {
    event.preventDefault();
    const rating = Number(dom.reviewRating.value);
    const content = dom.reviewContent.value.trim();

    try {
        if (editingReviewId) {
            await updateReview(editingReviewId, { rating, content });
            showToast("리뷰를 수정했습니다.");
        } else {
            await createReview(currentRestaurantId, { rating, content });
            showToast("리뷰를 등록했습니다.");
        }
        await refreshRestaurantDetail();
    } catch (error) {
        showToast(error.message, true);
    }
}

async function handleDeleteReview(reviewId) {
    if (!window.confirm("리뷰를 삭제하시겠습니까?")) {
        return;
    }
    try {
        await deleteReview(reviewId);
        showToast("리뷰를 삭제했습니다.");
        await refreshRestaurantDetail();
    } catch (error) {
        showToast(error.message, true);
    }
}

/* ---------- 인증 ---------- */

function updateAuthUI() {
    const user = getCurrentUser();
    if (user) {
        dom.guestArea.classList.add("hidden");
        dom.userArea.classList.remove("hidden");
        dom.userNickname.textContent = `${user.nickname}님`;
    } else {
        dom.guestArea.classList.remove("hidden");
        dom.userArea.classList.add("hidden");
    }
}

function showAuthForm(mode) {
    const isLogin = mode === "login";
    dom.loginFormWrap.classList.toggle("hidden", !isLogin);
    dom.signupFormWrap.classList.toggle("hidden", isLogin);
}

async function handleLoginSubmit(event) {
    event.preventDefault();
    try {
        await login({
            email: dom.loginEmail.value.trim(),
            password: dom.loginPassword.value,
        });
        updateAuthUI();
        closeModal(dom.authModal);
        dom.loginForm.reset();
        showToast("로그인했습니다.");
        if (currentRestaurantId) {
            await refreshRestaurantDetail();
        }
    } catch (error) {
        showToast(error.message, true);
    }
}

async function handleSignupSubmit(event) {
    event.preventDefault();
    try {
        await signup({
            email: dom.signupEmail.value.trim(),
            password: dom.signupPassword.value,
            nickname: dom.signupNickname.value.trim(),
        });
        showToast("회원가입이 완료되었습니다. 로그인해주세요.");
        dom.signupForm.reset();
        showAuthForm("login");
    } catch (error) {
        showToast(error.message, true);
    }
}

function handleLogout() {
    logout();
    updateAuthUI();
    showToast("로그아웃했습니다.");
    if (currentRestaurantId) {
        refreshRestaurantDetail();
    }
}

/* ---------- 공통 UI 유틸 ---------- */

function openModal(modalEl) {
    modalEl.classList.remove("hidden");
}

function closeModal(modalEl) {
    modalEl.classList.add("hidden");
}

function showToast(message, isError = false) {
    dom.toast.textContent = message;
    dom.toast.classList.toggle("toast-error", isError);
    dom.toast.classList.remove("hidden");
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => dom.toast.classList.add("hidden"), 3000);
}

function formatRating(value) {
    if (value === null || value === undefined || value === "") {
        return "-";
    }
    return Number(value).toFixed(1);
}

document.addEventListener("DOMContentLoaded", init);
