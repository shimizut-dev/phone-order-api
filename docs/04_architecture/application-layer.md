# application-layer.md

## 1. 目的

本ドキュメントは application layer の責務と実装方針を定義する。

---

## 2. 役割

application layer はユースケースを実現する層である。

主な役割は以下の通り。

- ユースケース実行
- トランザクション境界の管理
- repository を用いた集約取得・保存
- domain object を用いた処理の流れの制御

---

## 3. 対象ユースケース

- OrderService

---

## 4. 典型的な処理フロー

### 4.1 注文作成

1. 注文日時などのドメイン型を受け取る
2. Order を作成する
3. repository で保存する
4. 保存後の Order を返却する

### 4.2 注文参照

1. 注文コードなどのドメイン型を受け取る
2. repository から Order を取得する
3. 結果を返却する

### 4.3 注文キャンセル

1. 注文コードとバージョンなどのドメイン型を受け取る
2. repository から Order を取得する
3. キャンセル可能か判定する
4. Order を更新する
5. 保存後の Order を返却する

---

## 5. application に置くもの

- Application Service
- repository coordination
- トランザクション制御

---

## 6. application に置かないもの

- HTTP 固有処理
- API 入出力DTO
- API 入力値からドメイン型への変換
- SQL 実装
- DB entity
- ドメインルールの本体

---

## 7. 注意点

- domain の単なるラッパーにしない
- 逆に業務ルールを application に書きすぎない
- 「処理の流れ」は application
- 「業務上の正しさ」は domain
- Application Service の公開メソッド引数は、`OrderCode`、`OrderedAt`、`Version`、`PagingCondition` などのドメイン型を基本とする
- `String`、`OffsetDateTime`、`long` などの API 入力値は presentation layer でドメイン型へ変換する

---
