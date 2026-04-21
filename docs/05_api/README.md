# 05_api

`phone-order-api` の API 設計資料を管理します。

---

## 目的

- API の外部仕様を整理する
- 実装・レビュー・テストの基準をそろえる
- OpenAPI を API 仕様の正本として運用する
- モックサーバー、Swagger UI、コード生成に利用できる状態を維持する

---

## 管理資料

- `openapi.yaml`
    - API 仕様の正本
    - リクエスト / レスポンス仕様
    - リクエスト / レスポンス例
    - 共通エラーレスポンス
    - モックサーバー向け情報
    - Swagger UI などの表示ツールで利用する curl 例の生成元
- `api-design-rules.md`
    - API 設計ルール
    - OpenAPI 記載ルール
- `error-response.md`
    - エラーレスポンスの考え方
    - HTTP ステータスの使い分け

---

## 現在の主な対象 API

- `GET /api/v1/orders`
- `GET /api/v1/orders/{orderCode}`
- `POST /api/v1/orders`

---

## 運用ルール

- エンドポイント追加・変更時は `openapi.yaml` を先に更新する
- リクエスト例、レスポンス例、curl 生成に必要な情報は `openapi.yaml` に集約する
- 共通エラーレスポンスは `openapi.yaml` の `components/schemas` と `components/responses` に集約する
- openapi-generator の警告抑制を優先し、リクエスト例は schema / property example に、レスポンス例は media type example
  に記載する
- `operationId` とスキーマ名は生成 Java の名前に影響するため、実装側と合わせて安定させる
- `openapi.yaml` は `openapi-generator-maven-plugin` の入力として使用する
- API インターフェースと API 入出力モデルは Maven の `generate-sources` フェーズで生成する
- 生成物は `target/generated-sources/openapi` 配下に出力されるため、手動編集しない
- README には概要と運用ルールのみを書き、API の詳細仕様は `openapi.yaml` に集約する

---

## 資料の役割分担

### `openapi.yaml` に記載するもの

- パス
- HTTP メソッド
- パラメータ
- リクエストボディ
- レスポンス
- スキーマ
- example
- servers
- tags

### md に記載するもの

- 命名規約
- 設計方針
- 実装・生成上の注意点
- OpenAPI 運用ルール
- OpenAPI の記法だけでは表しにくい運用補足
