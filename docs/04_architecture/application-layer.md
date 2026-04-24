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
- presentation と domain の橋渡し
- 入出力DTOの管理

---

## 3. 対象ユースケース

- OrderService

---

## 4. 典型的な処理フロー

### 4.1 注文作成

1. 入力DTOを受け取る
2. ドメインオブジェクトを生成する
3. Order を作成する
4. repository で保存する
5. 出力DTOに変換して返却する

### 4.2 注文参照

1. repository から Order を取得する
2. 出力DTOへ変換する
3. 結果を返却する

### 4.3 注文キャンセル

1. repository から Order を取得する
2. キャンセル可能か判定する
3. Order を更新する
4. 保存する

---

## 5. application に置くもの

- Application Service
- repository coordination
- トランザクション制御

---

## 6. application に置かないもの

- HTTP 固有処理
- SQL 実装
- DB entity
- ドメインルールの本体

---

## 7. 注意点

- domain の単なるラッパーにしない
- 逆に業務ルールを application に書きすぎない
- 「処理の流れ」は application
- 「業務上の正しさ」は domain

---
