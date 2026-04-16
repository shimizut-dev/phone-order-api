# ADR-0012: REST API 設計方針を採用する

## ステータス

採用

---

## 背景

phone-order-api では、移動機注文を扱う API を提供する。  
注文という業務概念を外部に分かりやすく公開し、将来的な拡張にも耐えられる API 設計方針が必要である。

---

## 課題

- URL の表現を統一したい
- 実務想定のポートフォリオとして説明しやすい設計にしたい
- 実装と資料のずれを減らしたい

---

## 検討した選択肢

### 1. RPC 風 API

例

- `/createOrder`
- `/getOrder`

### 2. REST API

例

- `/api/v1/orders`
- `/api/v1/orders/{orderCode}`

---

## 採用理由

REST API は Web API の代表的な設計方針であり、多くのシステムで採用されている。  
業務リソースを URL で自然に表現しやすく、実務想定のポートフォリオとして説明しやすい。

---

## 採用方針

phone-order-api では以下の REST 設計方針を採用する。

### 1. リソース指向で表現する

例

- `/api/v1/orders`

### 2. 外部公開パスは `/api/v1` を付与する

例

- `/api/v1/orders`
- `/api/v1/orders/{orderCode}`

### 3. 外部公開上の注文識別子は `orderCode` を使用する

例

- `GET /api/v1/orders/{orderCode}`

### 4. 実装済み範囲を正として docs / OpenAPI を更新する

現時点の対象 API

- `GET /api/v1/orders`
- `GET /api/v1/orders/{orderCode}`
- `POST /api/v1/orders`

---

## 影響

- Controller のパス表現を統一する
- OpenAPI と README を同じ外部公開パスにそろえる
- テストコードや設計資料も同じ URL 表現に合わせる

---

## 結論

phone-order-api では REST API 設計方針を採用する。  
業務リソースを自然に表現しやすく、実務想定・保守性・説明しやすさの観点で最も適しているため。
