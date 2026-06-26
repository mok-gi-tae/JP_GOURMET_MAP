# API Specification

## 1. Auth API

### 1-1. Sign Up

회원가입을 한다.

- Method: `POST`
- URL: `/api/auth/signup`
- Auth Required: No

#### Request Body

```json
{
  "email": "user@example.com",
  "password": "1234",
  "nickname": "gitae"
}
```
#### Response 
```json
{
  "id": 1,
  "email": "user@example.com",
  "nickname": "gitae"
}
```
### 1-2. Login

로그인을 한다.

- Method: `POST`
- URL: `/api/auth/login`
- Auth Required: No

#### Request Body

```json
{
  "email": "user@example.com",
  "password": "password1234"
}
```
#### Response
```json
{
  "accessToken": "jwt-token-value",
  "nickname": "gitae"
}
```
---
## 2. Region API

### 2.1 Get Region

주요 관광지 및 지역 목록을 조회한다.

- Method: `GET`
- URL: `/api/regions`
- Auth Required: No

#### Response
```json
[
  {
    "id": 1,
    "name": "Shinjuku",
    "city": "Tokyo",
    "latitude": 35.6938,
    "longitude": 139.7034
  },
  {
    "id": 2,
    "name": "Dotonbori",
    "city": "Osaka",
    "latitude": 34.6687,
    "longitude": 135.5013
  }
]
```
---
### 3. Restaurant API

#### 3-1. Get Restaurants By Region

특정 지역 및 관광지 주변의 맛집 목록을 조회한다.

- Method: `GET`
- URL: `/api/restaurants?regionId=1`
- Auth Required: No

#### Response 
```json
[
  {
    "id": 1,
    "name": "Ichiran Shinjuku",
    "category": "Ramen",
    "tabelogScore": 3.52,
    "latitude": 35.6900,
    "longitude": 139.7000
  }
]
```

#### 3-2. Get Restaurant Detail

특정 맛집의 상세 정보를 조회한다.

- Method: `GET`
- URL: `/api/restaurants/{restaurantId}`
- Auth Required: No

#### Response 
```json
{
  "id": 1,
  "name": "Ichiran Shinjuku",
  "category": "Ramen",
  "address": "Tokyo, Shinjuku...",
  "latitude": 35.6900,
  "longitude": 139.7000,
  "tabelogScore": 3.52,
  "tabelogUrl": "https://tabelog.com/...",
  "youtubeUrl": "https://youtube.com/...",
  "averageUserRating": 4.3,
  "reviewCount": 12
}
```

### 4. Review API

#### 4-1. Create Review 

리뷰를 작성한다.

- Method: `POST`
- URL: `/api/restaurants/{restaurantId}/reviews`
- Auth Required: Yes

#### Header

```http
Authorization: Bearer jwt-token-value
```

#### Request Body
```json
{
  "rating": 4.5,
  "content": "맛있습니다."
}
```
#### Response
```json
{
  "id": 1,
  "rating": 4.5,
  "content": "맛있습니다.",
  "createdAt": SYSTIMESTAMP
}
```

#### 4-2. Get Reviews

- Method: `GET`
- URL: `/api/restaurants/{restaurantId}/reviews`
- Auth Required: No

#### Response 
```json
[
  {
   "id": 1,
   "nickname": "gitae"
   "rating": 4.5,
   "content": "맛있습니다.",
   "createdAt": SYSTIMESTAMP
  }
]
```