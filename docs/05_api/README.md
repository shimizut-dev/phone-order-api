# 📘 05_api

phone-order-api の API 設計ドキュメント

---

## 1. 目的

本ディレクトリは `phone-order-api` の API 設計資料を管理する。  
MVPで提供する API の仕様を整理し、実装・レビュー・テストの基準とする。

本ディレクトリでは **OpenAPI を主資料** として扱う。

---

## 2. 主資料

API仕様の正式な定義は以下とする。

- `openapi.yaml`

補助資料として以下を今後追加する。

- `api-design-rules.md`
- `error-response.md`
- `examples.md`

---

## 3. 対象範囲

MVPで対象とする API は以下とする。

- 注文作成
- 注文参照
- 注文キャンセル
- 配送作成
- 配送参照

---

## 4. 対象エンドポイント一覧

| API名 | Method | Path | 用途 |
|------|--------|------|------|
| 注文作成 | POST | `/orders` | 注文を作成する |
| 注文参照 | GET | `/orders/{orderId}` | 注文を参照する |
| 注文キャンセル | POST | `/orders/{orderId}/cancel` | 注文をキャンセルする |
| 配送作成 | POST | `/orders/{orderId}/deliveries` | 配送を作成する |
| 配送参照 | GET | `/deliveries/{deliveryId}` | 配送を参照する |

---

## 5. API 設計方針

- REST を基本とする
- OpenAPI で仕様を管理する
- JSON を利用する
- ドメインルールは domain で保証する
- API は application / presentation の責務に従って設計する
- MVPでは過剰な汎用化を避ける

---

## 6. 関連ドキュメント

- `../01_requirements/`
- `../02_domain/`
- `../03_usecases/usecases.md`
- `../04_architecture/`

---

## 7. 今後追加する資料

今後、以下の資料を追加予定とする。

- `api-design-rules.md`
- `error-response.md`
- `examples.md`

---

## 8. 補足

OpenAPI の更新時は、本 README と対象範囲・エンドポイント一覧の整合も確認すること。
