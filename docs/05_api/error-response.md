# 🚨 error-response.md

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
  "code": "ORDER_NOT_FOUND",
  "message": "注文が存在しません。",
  "details": []
}
```

---

## 4. 項目定義

| 項目 | 説明 |
|------|------|
| code | エラーコード |
| message | 利用者向けメッセージ |
| details | 詳細情報一覧 |

---

## 5. details の形式

必要に応じて `details` を返却する。

例

```json
{
  "code": "VALIDATION_ERROR",
  "message": "入力内容に誤りがあります。",
  "details": [
    {
      "field": "orderLines[0].quantity",
      "reason": "1以上で入力してください。"
    }
  ]
}
```

---

## 6. HTTPステータス方針

| ステータス | 用途 |
|-----------|------|
| 400 | リクエスト形式不正、入力値不正 |
| 404 | 対象リソースが存在しない |
| 409 | 業務ルール違反、状態不整合 |
| 500 | 想定外エラー |

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

推奨コード例

- `VALIDATION_ERROR`
- `BAD_REQUEST`

---

### 7.2 404 Not Found

以下の場合に使用する。

- 指定した注文が存在しない
- 指定した配送が存在しない

推奨コード例

- `ORDER_NOT_FOUND`
- `DELIVERY_NOT_FOUND`

---

### 7.3 409 Conflict

以下の場合に使用する。

- 注文キャンセル不可状態
- 配送数量が注文数量を超過
- ドメインルール違反
- 業務上ありえない状態

推奨コード例

- `ORDER_CANNOT_CANCEL`
- `DELIVERY_QUANTITY_EXCEEDED`
- `BUSINESS_RULE_VIOLATION`

---

### 7.4 500 Internal Server Error

以下の場合に使用する。

- 想定外例外
- 実装不備
- 一時的な内部障害

推奨コード例

- `INTERNAL_SERVER_ERROR`

---

## 8. 代表的なエラー例

### 8.1 注文が存在しない

```json
{
  "code": "ORDER_NOT_FOUND",
  "message": "注文が存在しません。",
  "details": []
}
```

HTTPステータス

```text
404
```

---

### 8.2 注文キャンセル不可

```json
{
  "code": "ORDER_CANNOT_CANCEL",
  "message": "完了済の注文はキャンセルできません。",
  "details": []
}
```

HTTPステータス

```text
409
```

---

### 8.3 入力値不正

```json
{
  "code": "VALIDATION_ERROR",
  "message": "入力内容に誤りがあります。",
  "details": [
    {
      "field": "orderLines[0].quantity",
      "reason": "1以上で入力してください。"
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

共通エラースキーマは今後 `openapi.yaml` の `components/schemas` に追加予定とする。

---

## 11. MVP時点の簡略方針

MVPでは以下を優先する。

- シンプルなエラー形式
- 必要最小限のエラーコード
- 一貫したHTTPステータス

MVPでは国際化、多言語メッセージ、複雑なサブコード体系は扱わない。

---

## 12. 今後の拡張

将来的に以下を検討する。

- 共通エラーコード一覧
- 監査用エラーID
- ログトレース用 correlationId
- 多言語対応
- OpenAPI の共通エラースキーマ強化
