# presentation-layer

presentation layer は外部からの入力を受け付け、結果を返却する層です。  
HTTP リクエスト / レスポンスを扱い、application layer との橋渡しを行います。

---

## 1. 役割

- HTTP リクエストを受け付ける
- OpenAPI から生成された API インターフェースを実装する
- OpenAPI から生成された Request モデルを受け取り application layer へ渡す
- application layer の結果を OpenAPI から生成された Response モデルに変換して返却する
- 例外を適切な HTTP ステータスとレスポンスへ変換する
- ログ出力や traceId 付与などの外部入出力制御を行う

---

## 2. 責務

### 2.1 入力受付

- パスパラメータ
- クエリパラメータ
- リクエストボディ
- バリデーション

### 2.2 出力返却

- 正常レスポンス返却
- エラーレスポンス返却
- HTTP ステータス制御

---

## 3. 主な構成要素

- Controller
- OpenAPI 生成 API interface
- OpenAPI 生成 Request / Response model
- Mapper
- GlobalExceptionHandler
- Filter

---

## 4. 現在の対象 API

### 4.1 注文

- `GET /api/v1/orders`
- `GET /api/v1/orders/{orderCode}`
- `POST /api/v1/orders`

---

## 5. 設計方針

- Controller は HTTP API の責務に限定する
- ビジネスロジックは application / domain に委譲する
- API インターフェースと API 入出力モデルは `openapi-generator-maven-plugin` で生成する
- 生成元は `docs/05_api/openapi.yaml` とし、生成物は `target/generated-sources/openapi` 配下に出力する
- 生成された API 入出力モデルは手修正しない
- presentation 層で手書きするモデルは、OpenAPI 生成対象外の補助モデルに限定する
- Entity を直接レスポンスへ返さない
- 例外レスポンスは `GlobalExceptionHandler` で集約する

---

## 6. 例外・レスポンス方針

- 存在しない注文は 404
- リクエスト不正は 400
- 想定外エラーは 500

---

## 7. 補足

今後 API を追加する場合も、`/api/v1` を付与した外部公開パスで統一する。
