
# 命名規約 (Naming Conventions)

本ドキュメントでは、phone-order-api における命名規約を定義する。

---

# ID命名規約

## 基本方針

ドメインモデルとDBのID命名は一致させない。  
それぞれのレイヤーに適した命名を採用する。

---

# ドメインモデル

ドメインモデルでは、意味が分かる命名とする。

例:

- orderId
- customerId
- productId
- orderDetailId

理由:

- 複数IDを扱う際に識別しやすい
- 可読性向上
- ドメインの意味を表現できる

---

# ER図 / DB

主キーはすべて `id` とする。

例:

orders

- id

order_details

- id

理由:

- シンプル
- 一貫性の確保
- 実務で一般的な設計

---

# 外部キー

外部キーは `{テーブル名}_id` とする。

例:

- order_id
- customer_id
- product_id

例:

order_details

- id
- order_id

---

# 業務向けID

業務向けIDは `{対象名}_code` とする。

例:

- order_code
- customer_code
- product_code

形式:

XXX-000001

例:

- ORD-000001
- CUS-000001
- PRD-000001

---

# 命名対応例

## ドメインモデル

Order

- orderId
- orderCode

## ER図 / DB

orders

- id
- order_code

order_details

- id
- order_id

---

# まとめ

| 対象 | 命名 |
|------|------|
| ドメインID | orderId |
| 主キー | id |
| 外部キー | order_id |
| 業務ID | order_code |

---

# 方針

- ドメインは意味重視
- DBはシンプル重視
- 外部キーは明確性重視
- 業務IDは業務用途重視
