# API設計ルール

## 1. 目的

本ドキュメントは `phone-order-api` における API 設計ルールを定義する。  
API仕様の表現を統一し、保守性と可読性を向上させることを目的とする。

---

## 2. 基本方針

- API仕様の正式な定義は `openapi.yaml` とする
- REST を基本とする
- JSON を利用する
- 命名規則を統一する
- MVPでは過剰な汎用化を避ける
- 業務ルールは domain で保証し、APIでは入出力と表現を担う

---

## 3. パス設計ルール

### 3.1 基本ルール

- パスは名詞で表現する
- 英小文字を使用する
- リソース名は複数形を基本とする
- 末尾スラッシュは付与しない

例

```text
/orders
/orders/{orderId}
/orders/{orderId}/cancel
/orders/{orderId}/deliveries
/deliveries/{deliveryId}
```

### 3.2 リソース命名

- コレクションは複数形とする
- パスパラメータは `{xxxId}` とする
- 業務操作が必要な場合のみ動詞サブパスを許可する

例

```text
/orders/{orderId}/cancel
```

補足

- `cancel` は注文に対する明示的な業務操作であるため許可する
- MVPでは業務上の分かりやすさを優先する

---

## 4. HTTP メソッドルール

| 用途   | メソッド |
|------|------|
| 作成   | POST |
| 参照   | GET  |
| 業務操作 | POST |

MVP対象API

- 注文作成 → `POST /orders`
- 注文参照 → `GET /orders/{orderId}`
- 注文キャンセル → `POST /orders/{orderId}/cancel`
- 配送作成 → `POST /orders/{orderId}/deliveries`
- 配送参照 → `GET /deliveries/{deliveryId}`

---

## 5. JSON 設計ルール

### 5.1 命名規則

- JSONキーは `camelCase` とする
- 省略語も読みやすさを優先する
- ID項目は `orderId` のように `{resource}Id` で表現する

例

```json
{
  "orderId": "ORD-000001",
  "orderStatus": "受付",
  "orderLines": []
}
```

### 5.2 配列項目

- 複数要素は複数形で表現する

例

```json
{
  "orderParties": [],
  "orderLines": [],
  "deliveries": []
}
```

---

## 6. データ型ルール

### 6.1 ID

- 型は文字列とする
- API上は意味のあるプレフィックス付き文字列を許可する

例

```text
ORD-000001
ODL-000001
DLV-000001
```

### 6.2 日時

- `date-time` を使用する
- タイムゾーン付き ISO 8601 形式とする

例

```text
2026-03-31T10:00:00+09:00
```

### 6.3 日付

- `date` を使用する

例

```text
1990-01-01
```

### 6.4 数量

- 数量は整数とする
- 1以上を前提とする

---

## 7. Request / Response 設計ルール

### 7.1 Request

- 作成APIは専用Requestを定義する
- 業務操作APIは専用Requestを定義する
- 不要な項目は受け取らない

例

- `CreateOrderRequest`
- `CancelOrderRequest`
- `CreateDeliveryRequest`

### 7.2 Response

- 参照しやすい形で返却する
- MVPではネストを過度に深くしない
- レスポンス都合で domain を直接露出しない

---

## 8. スキーマ設計ルール

- OpenAPI の `components/schemas` に集約する
- 共通利用する型はスキーマとして切り出す
- 同一意味のデータ構造を重複定義しない
- ドメインモデルとAPIモデルは完全一致を前提にしない

---

## 9. エラー設計ルール

- エラーレスポンス方針は `error-response.md` に従う
- HTTPステータスは意味に応じて使い分ける
- バリデーション不正と業務ルール違反を区別する

---

## 10. OpenAPI 記述ルール

- API仕様の正式版は `openapi.yaml` とする
- `summary` を必ず記載する
- `operationId` を必ず記載する
- `requestBody` と `responses` を必ず明記する
- パラメータ、スキーマ、レスポンスは再利用可能なものを `components` に寄せる

---

## 11. MVP時点で扱わないもの

以下はMVPでは扱わない。

- ページング
- ソート
- フィルタ検索
- バージョニング戦略の詳細化
- 非同期API
- Webhook
- 外部公開前提の認証認可詳細

---

## 12. 補足

API設計を変更する場合は、以下の整合を確認する。

- `openapi.yaml`
- `README.md`
- `error-response.md`
- `03_usecases/usecases.md`
- `04_architecture/`
