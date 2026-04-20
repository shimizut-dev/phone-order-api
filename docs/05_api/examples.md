# APIリクエスト・レスポンス例

## 1. 目的

本ドキュメントは `phone-order-api` の API リクエスト・レスポンス例を定義する。  
API利用者、設計レビュー、実装時の理解を目的とする。

---

## 2. 注文作成

### Request

`POST /api/v1/orders`

```json
{
  "orderedAt": "2026-04-07T10:15:30+09:00"
}
```

### Response

`201 Created`

```json
{
  "orderCode": "ORD000001",
  "orderedAt": "2026-04-07T10:15:30+09:00",
  "orderStatus": "001"
}
```

---

## 3. 注文参照

### Request

`GET /api/v1/orders/ORD000001`

### Response

`200 OK`

```json
{
  "orderCode": "ORD000001",
  "orderedAt": "2026-04-07T10:15:30+09:00",
  "orderStatus": "001"
}
```

---

## 4. 注文一覧取得

### Request

`GET /api/v1/orders`

### Response

`200 OK`

```json
[
  {
    "orderCode": "ORD000001",
    "orderedAt": "2026-04-07T10:15:30+09:00",
    "orderStatus": "001"
  },
  {
    "orderCode": "ORD000002",
    "orderedAt": "2026-04-07T11:00:00+09:00",
    "orderStatus": "002"
  }
]
```

---

## 5. エラー例

### 404

```json
{
  "timestamp": "2026-04-07T10:15:30+09:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "注文が見つかりません。",
  "path": "/api/v1/orders/ORD999999",
  "validationErrors": []
}
```

### 400

```json
{
  "timestamp": "2026-04-07T10:15:30+09:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "入力値が不正です。",
  "path": "/api/v1/orders",
  "validationErrors": [
    {
      "field": "orderedAt",
      "message": "必須項目です。"
    }
  ]
}
```

---

## 6. curl 実行例

### 注文作成

```bash
curl -i -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"orderedAt":"2026-04-07T10:15:30+09:00"}'
```

### 注文参照

```bash
curl -i http://localhost:8080/api/v1/orders/ORD000001
```

### 注文一覧取得

```bash
curl -i http://localhost:8080/api/v1/orders
```

### 入力値不正

```bash
curl -i -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{}'
```
