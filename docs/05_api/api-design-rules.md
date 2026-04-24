# api-design-rules

API 仕様の表現を統一し、保守性と可読性を向上させることを目的とする。

---

## 1. 目的

- API パス表現を統一する
- 命名規則を統一する
- MVP では過剰な汎用化を避ける
- 業務ルールは domain で保証し、API では入出力と表現を担う
- URL は `/api/v1` を付与した形式で統一する
- OpenAPI を正本として管理する

---

## 2. 基本方針

- REST を基本とする
- JSON を利用する
- OpenAPI を API 仕様の正本として管理する
- `openapi.yaml` から API インターフェースと API 入出力モデルを生成する
- 実装済み範囲と docs の整合を保つ
- example は原則 `openapi.yaml` に記載する
- 共通エラーは `components/schemas` と `components/responses` に寄せる

---

## 3. URL / パスルール

### 3.1 パス表現

- 英小文字を使用する
- リソース名は複数形を基本とする
- 末尾スラッシュは付与しない
- 外部公開パスは `/api/v1` を先頭に付与する

例

```text
/api/v1/orders
/api/v1/orders/{orderCode}
```

### 3.2 リソース命名

- コレクションは複数形とする
- 外部公開上の注文識別子は `{orderCode}` とする
- 業務操作が必要な場合のみ動詞サブパスを許可する

---

## 4. HTTP メソッドルール

| 用途   | メソッド |
|------|------|
| 作成   | POST |
| 業務操作 | POST |
| 参照   | GET  |

現在の対象 API

- 注文一覧取得 → `GET /api/v1/orders`
- 注文参照 → `GET /api/v1/orders/{orderCode}`
- 注文作成 → `POST /api/v1/orders`
- 注文キャンセル → `POST /api/v1/orders/{orderCode}/cancel`

---

## 5. リクエスト / レスポンスルール

- JSON キーは `camelCase` とする
- 省略語も読みやすさを優先する
- 注文識別子は `orderCode` のように API 契約に合わせて表現する
- example は API 利用者がそのまま理解できる値を記載する
- 正常系と異常系の両方に example を用意する

例

```json
{
  "orderCode": "ORD000001",
  "orderedAt": "2026-03-31T10:00:00+09:00",
  "orderStatus": "001"
}
```

---

## 6. データ型ルール

### 6.1 ID / コード

- 型は文字列とする
- API 上は意味のある文字列を使用する
- `orderCode` は `ORD` + 6桁数字を基本形式とする

例

```text
ORD000001
```

### 6.2 日時

- 日時は ISO 8601 形式の文字列とする
- タイムゾーン付きで扱う
- `format: date-time` を設定する

例

```text
2026-03-31T10:00:00+09:00
```

---

## 7. エラーレスポンスルール

- エラー時は `ErrorResponse` を返す
- バリデーションエラー詳細は `ValidationError` を返す
- `timestamp`、`status`、`error`、`message`、`path` を基本項目とする
- `validationErrors` は該当なしの場合でも空配列を返す
- 400 / 404 / 500 は共通レスポンスとして再利用できる形で定義する
- 409 は業務ルール違反、状態不整合が発生し得る API に定義する
- 現時点では注文キャンセル API に 409 を定義する
- 各 API には、その API で発生し得るエラーレスポンスだけを関連付ける

---

## 8. OpenAPI 記載ルール

- `operationId` を必ず記載する
- `requestBody` と `responses` を必ず明記する
- パラメータ、スキーマ、レスポンスは再利用可能なものを `components` に寄せる
- パスは `/api/v1` を含めて記載する
- `summary` と `description` を記載する
- example は可能な限り `openapi.yaml` に寄せる
- モックサーバーで利用する想定の response example を明記する
- openapi-generator の警告抑制を優先し、リクエストボディ直下には object example を置かない
- リクエスト例は schema / property example に、レスポンス例は media type example に記載する
- `servers` にローカル用と将来の公開先を記載できる状態にする
- 実装済み範囲に合わせて更新する
- 生成される Java 名に影響するため、スキーマ名と `operationId` は実装で使う名前として安定させる
- 既存 API の `operationId` やスキーマ名を変更する場合は、生成コードを利用する実装も同時に変更する
- 生成物は `target/generated-sources/openapi` 配下に出力されるため、修正は生成元の `openapi.yaml` に対して行う

## Internal DTO Naming

- OpenAPI の公開スキーマ名は `ErrorResponse` / `ValidationError` を維持する
- 例外ハンドラなどで手書きの内部専用 DTO が必要な場合は、生成モデルと衝突しないよう `ApiErrorResponse` /
  `ApiValidationError` のように `Api` 接頭辞を付ける
- OpenAPI 生成モデルと同名の手書き DTO を `presentation.error` などに追加しない
- `ErrorResponse.message` は null を返さない
- `ErrorResponse.validationErrors` は null を返さず、該当なしでも空配列を返す
