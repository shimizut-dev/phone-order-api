# phone-order-api

移動機注文API（phone-order-api）です。  
DDD / Clean Architecture をベースに、移動機申込ドメインを題材として設計・実装・テスト方針を整理するサンプルプロジェクトです。

---

## 概要

本プロジェクトは、以下を目的としています。

- 実務レベルのドメイン設計の整理
- Clean Architecture の適用
- DDD 設計の実践
- API 設計のベストプラクティス検証
- AI 活用による設計品質向上

---

## 技術スタック

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Validation
- PostgreSQL 16.x
- Flyway
- Testcontainers
- Docker / Docker Compose
- Lombok
- Maven
- OpenAPI Generator

---

## 実行・検証環境

| 用途                       | バージョン                                      |
|--------------------------|--------------------------------------------|
| ローカル DB / Testcontainers | PostgreSQL 16.x（`postgres:16`）             |
| CI コンテナ                  | Debian 13.x（Trixie / `debian:trixie-slim`） |

---

## 現在の実装範囲

現在は注文 API を中心に実装を進めています。

### 実装済み API

- `GET /api/v1/orders`
    - 注文一覧取得
- `GET /api/v1/orders/{orderCode}`
    - 注文コードによる注文取得
- `POST /api/v1/orders`
    - 注文登録
- `POST /api/v1/orders/{orderCode}/cancel`
    - 注文キャンセル

### エラーレスポンス方針

- 例外レスポンスは `ErrorResponse` を使用する
- バリデーションエラー詳細は `ValidationError` を使用する
- 業務ルール違反や状態不整合は `409 Conflict` を使用する
- API インターフェースと API 入出力モデルは `docs/05_api/openapi.yaml` から生成する
- 生成物は `target/generated-sources/openapi` 配下に出力し、手動編集しない

---

## 設計方針

### DDD

- `Order` を集約ルートとする
- ドメインルールはドメイン層で保証する
- 値オブジェクトや区分を用いて業務表現を明確にする

### Clean Architecture

以下のレイヤ構成を採用しています。

- presentation
- application
- domain
- infrastructure

### ログ・例外処理

- リクエスト / レスポンスログを出力する
- `traceId` を付与してログを追跡しやすくする
- 例外レスポンスは `GlobalExceptionHandler` で集約する

---

## ローカル起動方法

### 1. PostgreSQL を起動する

`docker/docker-compose.yml` を使用してローカル DB を起動します。

```bash
docker compose -f docker/docker-compose.yml up -d
```

ローカル DB の設定は以下です。

- image: `postgres:16`
- container name: `phone-order-api-local-db`
- database: `phone_order_api_local_db`
- username: `local`
- password: `local`

既存の volume に旧設定が残っている場合は、必要に応じて再作成してください。  
`down -v` は既存のローカル DB データを削除します。

```bash
docker compose -f docker/docker-compose.yml down -v
docker compose -f docker/docker-compose.yml up -d
```

### 2. DB 接続用の環境変数を設定する

`application-local.properties` では、ローカル DB の認証情報を以下の環境変数から読み込みます。

- `PHONE_ORDER_DB_USERNAME`
- `PHONE_ORDER_DB_PASSWORD`

PowerShell で設定する例:

```powershell
$env:PHONE_ORDER_DB_USERNAME="local"
$env:PHONE_ORDER_DB_PASSWORD="local"
```

`.env` などで管理する場合も、アプリ起動時には環境変数として渡してください。

### 3. `local` プロファイルでアプリを起動する

ローカル用設定は `src/main/resources/application-local.properties` に定義しています。

IntelliJ IDEA から起動する場合は、Spring Boot の実行/デバッグ構成で **有効なプロファイル** に以下を設定します。

```text
local
```

CLI で起動する場合の例:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

IntelliJ IDEA から起動する場合の環境変数設定手順は
`docs/09_development/intellij-local-profile-setup.md` を参照してください。

---

## テスト

主なテスト方針:

- API テストは Spring Boot + MockMvc を使用する
- DB を利用するテストは PostgreSQL 16.x + Testcontainers を使用する
- Flyway マイグレーションを適用した状態で検証する

テスト実行例:

```bash
./mvnw test
```

`generate-sources` フェーズで OpenAPI Generator が実行され、API インターフェースと API 入出力モデルが生成されます。

---

## ディレクトリ構成

```text
phone-order-api/
├─ docker/
├─ docs/
├─ src/
│  ├─ main/
│  │  ├─ java/jp/co/shimizutdev/phoneorderapi/
│  │  │  ├─ application/
│  │  │  ├─ domain/
│  │  │  ├─ infrastructure/
│  │  │  ├─ presentation/
│  │  │  │  ├─ error/
│  │  │  │  ├─ generated/
│  │  │  │  ├─ log/
│  │  │  │  └─ order/
│  │  └─ resources/
│  └─ test/
├─ pom.xml
├─ mvnw
└─ README.md
```

---

## ドキュメント構成

詳細資料は `docs/` 配下に整理しています。

```text
docs/
├─ 01_requirements/
├─ 02_domain/
├─ 03_usecases/
├─ 04_architecture/
├─ 05_api/
├─ 06_database/
├─ 07_adr/
├─ 09_development/
└─ README.md
```

補足:

- ドキュメントの入口は `docs/README.md`
- ローカル起動時の IntelliJ 設定は `docs/09_development/intellij-local-profile-setup.md`
- 開発ルールや命名規則は `docs/09_development/` に配置
- ADR は `docs/07_adr/` に配置

---

## このプロジェクトで重視していること

- 設計品質
- 保守性
- 拡張性
- テスト容易性
- ログ / 例外処理の一貫性

---

## Author

個人開発プロジェクト
