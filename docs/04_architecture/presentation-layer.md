# presentation-layer

presentation layer は外部からの入力を受け付け、結果を返却する層です。  
HTTP リクエスト / レスポンスを扱い、application layer との橋渡しを行います。

---

## 1. 役割

- HTTP リクエストを受け付ける
- Request DTO を受け取り application layer へ渡す
- application layer の結果を Response DTO に変換して返却する
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
- Request DTO
- Response DTO
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
- presentation 層の DTO は `record` を基本とする
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
