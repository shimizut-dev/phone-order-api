# presentation-layer.md

## 1. 目的

本ドキュメントは presentation layer の責務を定義する。

---

## 2. 役割

presentation layer は外部からの入力を受け付け、結果を返却する。

主な役割は以下の通り。

- HTTP endpoint の公開
- request の受信
- request validation
- application input への変換
- application output の response への変換
- 例外を HTTP エラーへ変換

---

## 3. MVP時点で想定する controller

- OrderController
- DeliveryController

---

## 4. 想定API

### 4.1 注文

- POST /orders
- GET /orders/{orderId}
- POST /orders/{orderId}/cancel

### 4.2 配送

- POST /orders/{orderId}/deliveries
- GET /deliveries/{deliveryId}

---

## 5. presentation に置くもの

- Controller
- Request DTO
- Response DTO
- ExceptionHandler

---

## 6. presentation に置かないもの

- ドメイン整合性ロジック
- SQL
- 永続化ロジック
- ユースケース本体

---

## 7. バリデーション方針

### 7.1 presentation で扱うもの

- 必須チェック
- 形式チェック
- 文字数チェック
- JSON構造の妥当性

### 7.2 domain で扱うもの

- 業務ルール
- 注文成立条件
- 明細整合性
- 配送数量整合性

---

## 8. エラー方針

- 存在しない注文は 404
- リクエスト不正は 400
- ドメインルール違反は 409 または 400 を検討する
- 想定外エラーは 500