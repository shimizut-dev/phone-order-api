# api-design-rules

API 仕様の表現を統一し、保守性と可読性を向上させることを目的とする。

---

## 1. 目的

- API パス表現を統一する
- 命名規則を統一する
- MVP では過剰な汎用化を避ける
- 業務ルールは domain で保証し、API では入出力と表現を担う
- URL は `/api/v1` を付与した形式で統一する

---

## 2. 基本方針

- REST を基本とする
- JSON を利用する
- OpenAPI を主資料として管理する
- `openapi.yaml` から API インターフェースと API 入出力モデルを生成する
- 実装済み範囲と docs の整合を保つ

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

| 用途 | メソッド |
|----|------|
| 作成 | POST |
| 参照 | GET  |

現在の対象 API

- 注文一覧取得 → `GET /api/v1/orders`
- 注文参照 → `GET /api/v1/orders/{orderCode}`
- 注文作成 → `POST /api/v1/orders`

---

## 5. リクエスト / レスポンスルール

- JSON キーは `camelCase` とする
- 省略語も読みやすさを優先する
- 注文識別子は `orderCode` のように API 契約に合わせて表現する

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

例

```text
ORD000001
```

### 6.2 日時

- 日時は ISO 8601 形式の文字列とする
- タイムゾーン付きで扱う

例

```text
2026-03-31T10:00:00+09:00
```

---

## 7. エラーレスポンスルール

- エラー時は `ErrorResponse` を返す
- バリデーションエラー詳細は `ValidationError` を返す
- `timestamp`、`status`、`error`、`message`、`path` を基本項目とする

---

## 8. OpenAPI 記載ルール

- `operationId` を必ず記載する
- `requestBody` と `responses` を必ず明記する
- パラメータ、スキーマ、レスポンスは再利用可能なものを `components` に寄せる
- パスは `/api/v1` を含めて記載する
- 実装済み範囲に合わせて更新する
- 生成される Java 名に影響するため、スキーマ名と `operationId` は実装で使う名前として安定させる
- 生成物は `target/generated-sources/openapi` 配下に出力されるため、修正は生成元の `openapi.yaml` に対して行う
