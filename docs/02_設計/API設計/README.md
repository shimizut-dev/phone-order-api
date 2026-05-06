# API設計

本ディレクトリは `phone-order-api` の API 設計資料を管理します。  
OpenAPI 仕様、API 設計ルール、エラーレスポンス方針を整理します。

---

## 概要

- API の外部仕様と設計ルールを管理する
- OpenAPI を正本として API 契約を共有する
- 実装と外部仕様の対応を明確にする

---

## 管理資料

- `../../../specs/openapi/openapi.yaml`
  - 現行 API の OpenAPI 仕様の正本を定義する
- `API設計ルール.md`
  - パス、HTTP メソッド、JSON 表現、OpenAPI 記載方針を整理する
- `エラーレスポンス.md`
  - HTTP ステータスと共通エラーレスポンスの扱いを整理する

---

## 運用ルール

- API 仕様の正本は `specs/openapi/openapi.yaml` のみとする
- `docs/` 配下に OpenAPI 仕様のコピーを置かない
- API 変更時は実装より先に `specs/openapi/openapi.yaml` を更新する
- OpenAPI 変更後は生成コードと関連ドキュメントの差分を確認する
