# 📘 examples.md

## 1. 目的

本ドキュメントは `phone-order-api` の API リクエスト・レスポンス例を定義する。  
API 利用者・設計レビュー・実装時の理解を目的とする。

---

# 2. 注文作成

## Request

POST /orders

```json
{
  "orderParties": [
    {
      "role": "契約者",
      "name": "山田 太郎",
      "nameKana": "ヤマダ タロウ",
      "gender": "男性",
      "birthDate": "1990-01-01",
      "address": {
        "postalCode": "100-0001",
        "prefecture": "東京都",
        "city": "千代田区",
        "street": "千代田1-1-1",
        "building": "サンプルビル101"
      }
    }
  ],
  "orderLines": [
    {
      "orderLineType": "回線",
      "quantity": 1,
      "line": {
        "lineContractKind": "新規契約",
        "sim": {
          "simForm": "物理SIM",
          "simStatus": "白SIM"
        }
      }
    }
  ],
  "orderedAt": "2026-03-31T10:00:00+09:00"
}
```

---

## Response

201 Created

```json
{
  "orderId": "ORD-000001",
  "orderStatus": "受付"
}
```

---

# 3. 注文参照

## Request

GET /orders/ORD-000001

---

## Response

200 OK

```json
{
  "orderId": "ORD-000001",
  "orderStatus": "受付"
}
```

---

# 4. 注文キャンセル

## Request

POST /orders/ORD-000001/cancel

```json
{
  "cancelReason": "顧客都合"
}
```

---

## Response

200 OK

```json
{
  "orderId": "ORD-000001",
  "orderStatus": "キャンセル"
}
```

---

# 5. 配送作成

## Request

POST /orders/ORD-000001/deliveries

```json
{
  "address": {
    "postalCode": "100-0001",
    "prefecture": "東京都",
    "city": "千代田区",
    "street": "千代田1-1-1"
  },
  "deliveryLines": [
    {
      "orderLineId": "ODL-000001",
      "quantity": 1
    }
  ]
}
```

---

## Response

201 Created

```json
{
  "deliveryId": "DLV-000001"
}
```

---

# 6. 配送参照

## Request

GET /deliveries/DLV-000001

---

## Response

200 OK

```json
{
  "deliveryId": "DLV-000001"
}
```

---

# 7. エラー例

## 404

```json
{
  "code": "ORDER_NOT_FOUND",
  "message": "注文が存在しません。",
  "details": []
}
```

---

## 400

```json
{
  "code": "VALIDATION_ERROR",
  "message": "入力内容に誤りがあります。",
  "details": []
}
```

---

## 409

```json
{
  "code": "BUSINESS_RULE_VIOLATION",
  "message": "業務ルール違反です。",
  "details": []
}
```
