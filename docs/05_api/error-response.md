# エラーレスポンス

## 1. 目的

本ドキュメントは `phone-order-api` におけるエラーレスポンス方針を定義する。  
HTTPステータスとエラー応答形式を統一し、API利用者と実装者の認識を合わせることを目的とする。

---

## 2. 基本方針

- エラー形式を統一する
- HTTPステータスを適切に使い分ける
- バリデーションエラーと業務ルール違反を区別する
- 想定外エラーは共通形式で返却する

---

## 3. エラーレスポンス形式

エラー時は以下の形式で返却する。

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

---

## 4. 項目定義

| 項目               | 説明                       |
|------------------|--------------------------|
| timestamp        | エラー発生日時                  |
| status           | HTTPステータスコード             |
| error            | HTTPステータス名               |
| message          | 利用者向けメッセージ               |
| path             | リクエストパス                  |
| validationErrors | バリデーションエラー一覧。該当なしの場合は空配列 |

---

## 5. validationErrors の形式

入力値不正がある場合は `validationErrors` にフィールド単位のエラーを返却する。
該当する明細がない場合は空配列を返却する。

例

```json
{
  "timestamp": "2026-04-07T10:15:30+09:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "入力内容に誤りがあります。",
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

## 6. HTTPステータス方針

| ステータス | 用途              |
|-------|-----------------|
| 400   | リクエスト形式不正、入力値不正 |
| 404   | 対象リソースが存在しない    |
| 409   | 業務ルール違反、状態不整合   |
| 500   | 想定外エラー          |

---

## 7. ステータス別ルール

### 7.1 400 Bad Request

以下の場合に使用する。

- JSON構造不正
- 必須項目不足
- 型不正
- 形式不正
- presentation層で判定できる入力不正

例

- `orderedAt` の形式不正
- `quantity` が数値でない
- 必須フィールド未指定

推奨 `error` 値

- `BAD_REQUEST`

### 7.2 404 Not Found

以下の場合に使用する。

- 指定した注文が存在しない
- 指定した配送が存在しない

推奨 `error` 値

- `NOT_FOUND`

### 7.3 409 Conflict

以下の場合に使用する。

- 注文キャンセル不可状態
- 配送数量が注文数量を超過
- ドメインルール違反
- 業務上ありえない状態

推奨 `error` 値

- `CONFLICT`

### 7.4 500 Internal Server Error

以下の場合に使用する。

- 想定外例外
- 実装不備
- 一時的な内部障害

推奨 `error` 値

- `INTERNAL_SERVER_ERROR`

---

## 8. 代表的なエラー例

### 8.1 注文が存在しない

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

HTTPステータス

```text
404
```

### 8.2 注文キャンセル不可

```json
{
  "timestamp": "2026-04-07T10:15:30+09:00",
  "status": 409,
  "error": "CONFLICT",
  "message": "完了済の注文はキャンセルできません。",
  "path": "/api/v1/orders/ORD000001",
  "validationErrors": []
}
```

HTTPステータス

```text
409
```

### 8.3 入力値不正

```json
{
  "timestamp": "2026-04-07T10:15:30+09:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "入力内容に誤りがあります。",
  "path": "/api/v1/orders",
  "validationErrors": [
    {
      "field": "orderedAt",
      "message": "必須項目です。"
    }
  ]
}
```

HTTPステータス

```text
400
```

---

## 9. レイヤ別責務

### 9.1 presentation

- 入力形式エラーを 400 に変換する
- domain / application の例外を HTTP エラーに変換する
- 共通エラーレスポンス形式に整形する

### 9.2 application

- ユースケース実行中の例外を適切に伝搬する
- HTTPには依存しない

### 9.3 domain

- 業務ルール違反を表す例外を送出する
- HTTPステータスを知らない

---

## 10. OpenAPI との関係

OpenAPI では各APIに以下を定義する。

- 400
- 404
- 409
- 必要に応じて 500

共通エラースキーマは `openapi.yaml` の `components/schemas/ErrorResponse` と
`components/schemas/ValidationError` に定義する。
`openapi-generator-maven-plugin` により、これらのスキーマから生成モデルが作成される。

---

## 11. MVP時点の簡略方針

MVPでは以下を優先する。

- シンプルなエラー形式
- 必要最小限の HTTP ステータス表現
- 一貫したHTTPステータス

MVPでは国際化、多言語メッセージ、複雑なサブコード体系は扱わない。

---

## 12. 今後の拡張

将来的に以下を検討する。

- 必要になった場合の共通エラーコード追加
- 監査用エラーID
- ログトレース用 correlationId
- 多言語対応
- OpenAPI の共通エラースキーマ強化
