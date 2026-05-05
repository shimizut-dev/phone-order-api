# IntelliJ で OpenAPI generated sources を扱う手順

## 概要

OpenAPI Generator が出力した generated sources を IntelliJ で正しく参照できるようにする手順を定義する

---

## 対象

- `docs/02_設計/API設計/OpenAPI仕様.yaml` または `specs/openapi/openapi.yaml` を更新した場合
- `presentation.generated.*` 配下の型が IntelliJ で未解決になった場合
- `OrderController` や `OrderMapper` で generated model / api が参照できない場合

---

## 前提

- OpenAPI 仕様の正本は `docs/02_設計/API設計/OpenAPI仕様.yaml`
- OpenAPI Generator の入力は `specs/openapi/openapi.yaml`
- 生成物の出力先は `target/generated-sources/openapi/src/main/java`
- 生成物は手修正しない
- `pom.xml` で `build-helper-maven-plugin` により generated sources を source root として登録している

---

## 手順

### 1. OpenAPI generated sources を生成する

```bash
./mvnw generate-sources
```

### 2. IntelliJ で Maven プロジェクトを再読込する

Maven ツールウィンドウから `Reload All Maven Projects` を実行する

### 3. generated sources が source root として認識されていることを確認する

以下のディレクトリ配下に生成コードが存在することを確認する

```text
target/generated-sources/openapi/src/main/java
```

### 4. unresolved symbol が解消されたことを確認する

以下のような型が解決されることを確認する

- `jp.co.shimizutdev.phoneorderapi.presentation.generated.api.OrdersApi`
- `jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderRequest`
- `jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderResponse`
- `jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderPageResponse`
- `jp.co.shimizutdev.phoneorderapi.presentation.generated.model.CancelOrderRequest`

---

## 補足

- 通常の品質チェックでは `./mvnw spotless:check test` を実行する
- generated sources の未解決はコード不備ではなく、生成または IDE 再読込不足であることが多い
- `Invalidate Caches` の前に `generate-sources` と Maven 再読込を優先する
