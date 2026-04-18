# 実装命名ルール

## 目的

実装クラス・メソッドの命名ルールを統一し、可読性・保守性を高める。  
特に、presentation / application / domain / infrastructure ごとの責務に応じて、使用する動詞を明確にし、実装者ごとの命名ゆれを防ぐ。

---

## 対象レイヤー

- presentation
- application
- domain
- infrastructure

---

## 基本方針

- presentation は HTTP API の入出力を扱う
- application はユースケースの実行を扱う
- domain は業務ルールと値オブジェクトを扱う
- infrastructure は DB・採番・設定など技術実装を扱う
- 一覧取得・1件取得・登録・更新・削除で、使用する動詞をできるだけ統一する
- Repository は Spring Data JPA の利用を前提に、取得は `find`、登録・更新は `save`、削除は `delete` を基本とする

---

## パッケージごとの命名ルール

## presentation層

### Controller

現行実装では、一覧取得・1件取得ともに `get` を使用している。  
そのため、presentation の Controller は次の命名を採用する。

#### 一覧取得

- 日本語: 一覧取得
- 英語: `get + 複数形`

例

```text
getOrders
getDeliveries
```

#### 1件取得

- 日本語: 1件取得
- 英語: `get`

例

```text
getOrderByOrderCode
getDeliveryById
```

#### 登録

- 日本語: 登録
- 英語: `create`

例

```text
createOrder
createDelivery
```

#### 更新

- 日本語: 更新
- 英語: `update`

例

```text
updateOrder
updateDelivery
```

#### 削除

- 日本語: 削除
- 英語: `delete`

例

```text
deleteOrder
deleteDelivery
```

### Request / Response

- Request クラスは `XxxRequest`
- Response クラスは `XxxResponse`
- Error Response は `ErrorResponse`
- バリデーション明細は `ValidationError`
- DTO suffix は付けず、役割が分かる名称を使用する
- API 入出力モデルは `docs/05_api/openapi.yaml` のスキーマ名から生成する
- 生成されたモデルは `presentation.generated.model` に属する presentation 境界モデルとして扱う

例

```text
OrderRequest
OrderResponse
ErrorResponse
ValidationError
```

### DTO / Error Response の実装方針

- presentation 層の API 入出力モデルは `openapi-generator-maven-plugin` で生成する
- 対象は `Request` / `Response` / `ErrorResponse` / `ValidationError` など HTTP の入出力を表すモデルとする
- 生成元は `docs/05_api/openapi.yaml` とする
- 生成物は `target/generated-sources/openapi` 配下に出力されるため、手動編集しない
- API 入出力モデルの項目追加・型変更・バリデーション変更は `openapi.yaml` を修正する
- OpenAPI 生成対象外の補助 DTO を手書きする場合のみ、`record` を基本とする
- domain object から API Response モデルへの変換は `Mapper` に集約する
- JPA Entity は DTO と別ルールで扱い、本方針の対象外とする

例

```text
docs/05_api/openapi.yaml
  components.schemas.OrderRequest
  components.schemas.OrderResponse
  components.schemas.ErrorResponse
  components.schemas.ValidationError

target/generated-sources/openapi/src/main/java/.../presentation/generated/model
  OrderRequest
  OrderResponse
  ErrorResponse
  ValidationError
```

### Mapper

presentation 配下の変換クラスは `XxxMapper` とする。

例

```text
OrderMapper
BaseAuditMapper
```

---

## application層

### Service

現行実装では、application の Service も Controller と同様に `get + 複数形` を採用している。  
そのため、application の Service は次の命名を採用する。

#### 一覧取得

- 日本語: 一覧取得
- 英語: `get + 複数形`

例

```text
getOrders
getDeliveries
```

#### 1件取得

- 日本語: 1件取得
- 英語: `get`

例

```text
getOrderByOrderCode
getDeliveryById
```

#### 登録

- 日本語: 登録
- 英語: `create`

例

```text
createOrder
createDelivery
```

#### 更新

- 日本語: 更新
- 英語: `update`

例

```text
updateOrder
updateDelivery
```

#### 削除

- 日本語: 削除
- 英語: `delete`

例

```text
deleteOrder
deleteDelivery
```

---

## domain層

### 値オブジェクト

値オブジェクトは、既存の値を包むファクトリメソッドとして `of` を使用する。

例

```text
OrderId.of
OrderCode.of
OrderedAt.of
```

### エンティティ

現行実装では、`Order` のファクトリメソッドとして 新規生成 `create` と 再構築 `reconstruct` を使用している。

例

```text
Order.reconstruct(orderedAt, orderCodeGenerator)
Order.reconstruct(orderId, orderCode, orderedAt, orderStatus)
```

### 列挙型

列挙型は業務意味を表す名称を使用する。

例

```text
OrderStatus.RECEIVED
OrderStatus.UNDER_REVIEW
OrderStatus.ARRANGING
```

### ドメインリポジトリ

ドメインのリポジトリは interface とし、永続化の詳細を持たない。

#### 一覧取得

- 日本語: 一覧取得
- 英語: `find`

例

```text
findAll
```

#### 1件取得

- 日本語: 1件取得
- 英語: `find`

例

```text
findByOrderCode
```

#### 登録・更新

- 日本語: 登録・更新
- 英語: `save`

例

```text
save
```

---

## infrastructure層

### DBリポジトリ

テーブル操作するDBリポジトリは `XxxDbRepository` とする。

例

```text
OrdersJpaRepository
DeliveriesJpaRepository
```

### DBエンティティ

テーブルのDBエンティティは `XxxEntity` とする。

例

```text
OrdersEntity
BaseAuditEntity
```

### ドメインリポジトリ実装

ドメインを再構築するドメインリポジトリの実装クラスは `XxxRepositoryImpl` とする。

例

```text
OrderRepositoryImpl
```

### 採番クラス

採番クラスは `XxxGenerator` とする。

例

```text
SequentialOrderCodeGenerator
```

### 設定クラス

設定クラスは `XxxConfig` とする。

例

```text
JpaAuditConfig
```

---

## 使い分けルール

### presentation の例

```text
public class OrderController implements OrdersApi {
    // ...
}

@Override
public List<OrderResponse> getOrders() {
    // ...
}

@Override
public OrderResponse getOrderByOrderCode(@PathVariable String orderCode) {
    // ...
}

@Override
public OrderResponse createOrder(@RequestBody OrderRequest requestDto) {
    // ...
}
```

### application の例

```text
public List<Order> getOrders() {
    // ...
}

public Optional<Order> getOrderByOrderCode(String orderCode) {
    // ...
}

public Order createOrder(OffsetDateTime orderedAt) {
    // ...
}
```

### domain Repository の例

```text
List<Order> findAll();

Optional<Order> findByOrderCode(OrderCode orderCode);

Order save(Order order);
```

### infrastructure DB Repository の例

```text
Optional<OrdersEntity> findByOrderCode(String orderCode);

Optional<OrdersEntity> findTopByOrderCodeStartingWithOrderByOrderCodeDesc(String prefix);

OrdersEntity save(OrdersEntity entity);
```

---

## Javadoc の記載ルール

Javadoc は日本語で記載する。  
命名ルールと対応する日本語を使用する。

### presentation の例

```text
/**
 * 注文一覧を取得
 *
 * @return 注文レスポンス一覧
 */
public List<OrderResponse> getOrders() {
    // ...
}

/**
 * 注文コードで注文を取得
 *
 * @param orderCode 注文コード
 * @return 注文レスポンス
 */
public OrderResponse getOrderByOrderCode(String orderCode) {
    // ...
}
```

### application の例

```text
/**
 * 注文一覧を取得する
 *
 * @return 注文一覧
 */
public List<Order> getOrders() {
    // ...
}

/**
 * 注文コードで注文を取得する
 *
 * @param orderCode 注文コード
 * @return 注文
 */
public Optional<Order> getOrderByOrderCode(String orderCode) {
    // ...
}
```

### domain Repository の例

```text
/**
 * 注文コードで注文を取得する
 *
 * @param orderCode 注文コード
 * @return 注文
 */
Optional<Order> findByOrderCode(OrderCode orderCode);
```

### Request / Response の例

```text
// API 入出力モデルは OpenAPI から生成する。
// Javadoc 相当の説明は openapi.yaml の description に記載する。
components:
  schemas:
    OrderRequest:
      type: object
      properties:
        orderedAt:
          type: string
          format: date-time
          description: 注文日時。タイムゾーンオフセット付きISO 8601形式。
```

---

## 注意事項

- presentation / application は現行実装に合わせて `getOrders` を採用している
- domain の値オブジェクトは `of` を基本とする
- API 入出力モデルは OpenAPI から生成し、生成物を手動編集しない
- OpenAPI 生成対象外の補助 DTO を手書きする場合は `record` を基本とし、単純な `new` のラッパーだけの
  `static factory method` は作成しない
- 補助 DTO に `static factory method` を持たせる場合は、null 補正・不変化・生成意図の明確化などの意味を持たせる
- domain Repository は interface とし、永続化の詳細は infrastructure に置く
- infrastructure の DB Repository は Spring Data JPA の慣習に合わせて `find` / `save` / `delete` を使う
- 命名は役割に応じて統一し、クラス間で混在させない
- 省略語は極力使わず、業務意味が分かる名前にする
- typo を残さない。特に OpenAPI の description は実装内容と一致させる

---

## 推奨例

### presentation

```text
getOrders
getOrderByOrderCode
createOrder
updateOrder
deleteOrder
```

### application

```text
getOrders
getOrderByOrderCode
createOrder
updateOrder
deleteOrder
```

### domain

```text
OrderId.of
OrderCode.of
OrderedAt.of
Order.reconstruct
findByOrderCode
save
```

### infrastructure

```text
OrdersDbRepository
OrdersEntity
OrderRepositoryImpl
SequentialOrderCodeGenerator
JpaAuditConfig
```

---

## 非推奨例

```text
searchOrders
referOrder
registerOrder
modifyOrder
removeOrder
```

理由

- 動詞体系が統一されていない
- presentation / application / domain / infrastructure 間で役割差が見えにくい
- Spring Data JPA の慣習ともずれやすい
