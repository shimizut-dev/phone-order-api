# docs

本ディレクトリは `phone-order-api` の設計・開発ドキュメントを整理するための入口です。  
要件、ドメイン、ユースケース、アーキテクチャ、API、DB、ADR、開発ルールを番号付きで管理します。

---

## 目的

- ドキュメントの配置場所を明確にする
- 設計判断の参照先を統一する
- 実装とドキュメントのずれを減らす
- README から詳細資料へ辿りやすくする

---

## ディレクトリ構成

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

---

## ディレクトリ一覧

### 01_requirements

要件整理の資料を配置します。

- `scope.md`
- `actors.md`
- `business-rules.md`
- `system-overview.md`

### 02_domain

ドメイン設計の資料を配置します。

- `domain-glossary.md`
- `order-aggregate.md`
- `order-line-creation-rules.md`
- `model/`
- `rules/`

### 03_usecases

ユースケース整理の資料を配置します。

- `usecases.md`

### 04_architecture

アーキテクチャ設計の資料を配置します。

- `architecture-overview.md`
- `clean-architecture.md`
- `dependency-rules.md`
- `layer-and-package-mapping.md`
- `package-structure.md`
- 各レイヤ責務資料

### 05_api

API 設計・仕様資料を配置します。

- `openapi.yaml`
- `api-design-rules.md`
- `error-response.md`
- `examples.md`

### 06_database

データベース設計資料を配置します。

- `database-overview.md`
- `id-generation.md`
- `naming-rules.md`
- `er-diagram/`
- DDL / ER 図関連資料

### 07_adr

Architecture Decision Record を配置します。

- `ADR-0000-adr-naming-rules.md`
- `ADR-0001` 以降の採用判断
- `ADR-template.md`

### 09_development

開発ルール・運用ルールを配置します。

- 開発フロー
- Git 運用
- 命名規則
- IntelliJ / local プロファイル設定
- ログ運用
- テスト規約
- ドキュメントフォーマット規約

---

## ドキュメント運用ルール

- 各ディレクトリには `README.md` を置き、役割と主要ファイルを明記する
- 上位 README には概要と入口だけを書き、詳細は個別資料に分ける
- 実装変更時は関連する docs と README を同時に更新する
- 新しい設計判断は ADR または該当レイヤ資料へ追記する
- フォーマットは `docs/09_development/document-format-guideline.md` に従う

---

## 更新優先順

1. ルート `README.md`
2. `docs/README.md`
3. 各ディレクトリの `README.md`
4. 個別の詳細資料
