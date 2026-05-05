# ID採番方針

本書は `phone-order-api` の ID 採番方針を整理する。  
現行の domain 実装、JPA 実装、Flyway migration を基準とする。

---

## 概要

- 内部主キーと外部公開識別子を分離する
- `OrderId` はアプリケーション側で UUID を生成する
- `order_code` は PostgreSQL の sequence と関数で採番する

---

## 内容

### 方針

- システム内部の同一性と外部公開識別子は別の型で管理する
- 主キー採番は domain モデルの生成責務に寄せる
- 業務向けコード採番の実装詳細は DB と infrastructure へ閉じ込める

### ルール

- `OrderId` は `Order.create` の中で UUID を生成する
- `orders.id` の型は `uuid` とする
- 注文の外部公開識別子は `order_code` とする
- `order_code` は `ORD` + 6 桁の形式とする
- `order_code` の採番には PostgreSQL の `order_code_seq` と `next_order_code()` を利用する
- アプリケーションは `OrderCodeGenerator` を通じて採番し、具体実装は `SequenceOrderCodeGenerator` とする
- `version` は楽観的ロック用の更新管理値として `0` から始める

### 手順

1. 新規注文作成時は `OrderCodeGenerator.generate()` で注文コードを採番する
2. 採番済みコードと注文日時を使って `Order.create` で注文を生成する
3. DB 側で採番方式を変更する場合は、migration、`next_order_code()`、`SequenceOrderCodeGenerator` を合わせて更新する

---

## 関連資料

- `データベース概要.md`
- `命名ルール.md`
- `../ドメイン設計/ルール/ドメインルール.md`
