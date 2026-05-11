# phone-order-api

`phone-order-api` は移動機注文を管理する Spring Boot ベースの REST API です。  
注文一覧取得、注文取得、注文登録、注文キャンセルを提供します。

## すぐ試せるもの

- Swagger UI: `https://shimizut-dev.github.io/phone-order-api/swagger/`
- Mock Server: `未提供`
- OpenAPI 正本: [specs/openapi/openapi.yaml](specs/openapi/openapi.yaml)

## Contract-First Development

このリポジトリは `specs/openapi/openapi.yaml` を契約の正本として扱い、仕様変更から実装反映までを次の流れで進めます。

1. OpenAPI 仕様を更新する
2. `openapi-generator-maven-plugin` で生成コードを更新する
3. テストを実行して整合性を確認する
4. CI で契約と実装の差分・品質を検証する

```text
Edit OpenAPI spec
        ↓
Generate code (Maven plugin)
        ↓
Run tests locally
        ↓
Validate in CI
```

---

## 概要

- Java 21 / Spring Boot 3 / Maven 構成
- PostgreSQL を利用
- Flyway でスキーマ管理
- OpenAPI を契約の正本として利用
- ArchUnit、Checkstyle、SpotBugs、Spotless、Testcontainers を利用

---

## 主要機能

- `GET /api/v1/orders`
  - 注文一覧取得
- `GET /api/v1/orders/{orderCode}`
  - 注文取得
- `POST /api/v1/orders`
  - 注文登録
- `POST /api/v1/orders/{orderCode}/cancel`
  - 注文キャンセル

---

## 開発の入口

- ドキュメント入口: [docs/README.md](docs/README.md)
- 要件: [docs/01_要件/README.md](docs/01_要件/README.md)
- 設計: [docs/02_設計/README.md](docs/02_設計/README.md)
- 開発ガイド: [docs/03_開発/README.md](docs/03_開発/README.md)
- ADR: [docs/05_ADR/README.md](docs/05_ADR/README.md)
- OpenAPI 仕様の正本: [specs/openapi/openapi.yaml](specs/openapi/openapi.yaml)

---

## 最短手順

最初の 10 分で確認する順番:

1. OpenAPI 正本を確認する  
   [specs/openapi/openapi.yaml](specs/openapi/openapi.yaml)
2. ローカル設定を確認する  
   [docs/03_開発/開発環境/ローカル設定.md](docs/03_開発/開発環境/ローカル設定.md)
3. ローカル DB を起動する
4. テストを実行する
5. アプリを起動する

ローカル DB 起動:

```bash
docker compose -f docker/docker-compose.yml up -d
```

テスト実行:

```bash
./mvnw spotless:check verify
```

アプリ起動:

```bash
./mvnw spring-boot:run
```

Windows の場合:

```powershell
docker compose -f docker/docker-compose.yml up -d
.\mvnw.cmd spotless:check verify
.\mvnw.cmd spring-boot:run
```

## JaCoCo レポート

- ローカルで `./mvnw verify` 実行後、`target/site/jacoco/index.html` にカバレッジレポートを生成
- CI では `jacoco-report` artifact として保存
- GitHub Actions の該当 workflow run から `jacoco-report` をダウンロードして確認可能

---

## エンコーディング方針

- 本リポジトリのテキストファイルは UTF-8 に統一する
- UTF-8 BOM は使用しない
- 文字コード設定は [.editorconfig](.editorconfig) を正本とする

---

## セットアップ

前提:

- Java 21
- Maven 3.9 以上
- Docker

ローカル起動前の主な確認先:

- [docs/03_開発/開発環境/開発環境概要.md](docs/03_開発/開発環境/開発環境概要.md)
- [docs/03_開発/開発環境/ローカル設定.md](docs/03_開発/開発環境/ローカル設定.md)
- [docker/docker-compose.yml](docker/docker-compose.yml)

基本コマンドは `最短手順` を参照する

---

## 品質管理

本プロジェクトでは、実装だけでなく品質管理も重視しています。

- CI で `./mvnw spotless:check verify` を実行
- JaCoCo によるカバレッジレポートを生成
- OpenAPI 正本と生成コード、実装、テストの整合を確認

## テスト / カバレッジ

テストは単体テスト、統合テスト、アーキテクチャテストを対象としています。  
カバレッジは JaCoCo により計測します。

ローカルでの確認手順:

```bash
./mvnw clean verify
```

Windows の場合:

```powershell
.\mvnw.cmd clean verify
```

生成レポート:

`target/site/jacoco/index.html`
