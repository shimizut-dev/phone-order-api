# 05_api

`phone-order-api` の API 設計資料を管理します。

---

## 目的

- API の外部仕様を整理する
- 実装・レビュー・テストの基準をそろえる
- OpenAPI を主資料として運用する

---

## 管理資料

- `openapi.yaml`
    - API 仕様の正本
- `api-design-rules.md`
    - API 設計ルール
- `error-response.md`
    - エラーレスポンス方針
- `examples.md`
    - リクエスト / レスポンス例

---

## 現在の主な対象 API

- `GET /api/v1/orders`
- `GET /api/v1/orders/{orderCode}`
- `POST /api/v1/orders`

---

## 運用ルール

- エンドポイント追加・変更時は `openapi.yaml` を先に更新する
- `openapi.yaml` は `openapi-generator-maven-plugin` の入力として使用する
- API インターフェースと API 入出力モデルは Maven の `generate-sources` フェーズで生成する
- 生成物は `target/generated-sources/openapi` 配下に出力されるため、手動編集しない
- 例外レスポンス構造の変更時は `error-response.md` も更新する
- README には概要のみを書き、詳細仕様は個別資料へ分ける
