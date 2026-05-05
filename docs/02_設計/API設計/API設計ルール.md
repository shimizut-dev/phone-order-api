# API設計ルール

本書は `phone-order-api` の API 設計ルールを整理する。  
現行の OpenAPI 仕様とコントローラ実装を基準とする。

---

## 概要

- API の外部仕様表現を統一する
- OpenAPI を API 契約の正本として管理する
- 現行実装に存在する注文 API の設計前提を明確にする

---

## 内容

### 方針

- 外部 API は REST を基本とし、JSON を利用する
- OpenAPI 仕様は `OpenAPI仕様.yaml` を正本とする
- 実装は OpenAPI から生成された API インターフェースとモデルに従う
- 外部公開識別子は `orderCode` を用い、内部識別子 `OrderId` は公開しない
- OpenAPI 生成の正本は `docs/02_設計/API設計/OpenAPI仕様.yaml` とする

### ルール

- 外部公開パスは `/api/v1` を先頭に付与する
- リソース名は英小文字の複数形を基本とする
- 末尾スラッシュは付与しない
- JSON キーは `camelCase` とする
- 日時はタイムゾーンオフセット付き ISO 8601 形式の文字列で表現する
- 一覧取得のページングは `page` と `size` を query parameter で受け取る
- `page` は 0 始まりとし、未指定時は `0` とする
- `size` は未指定時 `20`、最大値 `100` とする
- 一覧レスポンスには `items`, `page`, `size`, `totalElements`, `totalPages`, `hasNext`, `hasPrevious` を含める
- 注文一覧は注文日時降順、同一日時は注文コード降順で返却する
- 注文作成は `POST /api/v1/orders` とする
- 注文キャンセルは業務操作のため `POST /api/v1/orders/{orderCode}/cancel` とする
- 共通パラメータ、共通レスポンス、共通スキーマは `components` に寄せる
- `operationId` は必ず定義し、生成コードで安定して利用できる名前にする
- OpenAPI 生成物は手修正せず、変更は生成元の YAML に対して行う

### 手順

1. API を追加または変更する場合は、先に `OpenAPI仕様.yaml` を更新する
2. パス、HTTP メソッド、リクエスト、レスポンス、エラーを仕様へ反映する
3. 既存の `operationId` とスキーマ名を変更する場合は、生成コード利用箇所への影響を確認する
4. 実装側では OpenAPI 生成インターフェースとモデルに合わせて controller と mapper を修正する

---

## 関連資料

- `OpenAPI仕様.yaml`
- `エラーレスポンス.md`
- `../アーキテクチャ設計/レイヤー設計/プレゼンテーション層.md`
