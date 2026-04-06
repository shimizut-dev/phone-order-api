# architecture-overview.md

## 1. 目的

本ドキュメントは `phone-order-api` のアーキテクチャ全体像を定義する。  
本システムで採用する設計方針、層構成、責務分担を明確化し、実装時の判断基準とする。

---

## 2. 設計方針

本システムは以下の方針で設計する。

- DDD をベースとする
- Clean Architecture を採用する
- ドメインの整合性を最優先とする
- 保守性・テスト容易性を重視する
- MVPでは対象スコープを絞り、過剰設計を避ける

---

## 3. アーキテクチャ概要

本システムは以下の層で構成する。

- presentation
- application
- domain
- infrastructure

依存関係は外側から内側に向かう一方向とする。

```text
presentation
    ↓
application
    ↓
domain

infrastructure
    ↓
application
    ↓
domain
```

---

## 4. 各層の役割

### 4.1 presentation

- HTTP リクエストを受け付ける
- リクエストをアプリケーション入力に変換する
- アプリケーション結果をレスポンスに変換する
- HTTP ステータスコードを決定する

### 4.2 application

- ユースケースを実行する
- トランザクション境界を持つ
- ドメインオブジェクトを組み立てる
- リポジトリを利用して集約を保存・取得する
- 外部層との調整を行う

### 4.3 domain

- 業務ルールを表現する
- エンティティ、値オブジェクト、集約を定義する
- 不正状態を生成時点で防ぐ
- ドメイン整合性を保証する

### 4.4 infrastructure

- DBアクセスを実装する
- 外部サービス連携を実装する
- フレームワーク依存処理を吸収する
- repository interface の実装を提供する

---

## 5. MVP時点の対象

MVPでは以下を対象とする。

- 注文作成
- 注文参照
- 注文キャンセル
- 配送作成
- 配送参照

以下はMVP対象外とする。

- 支払
- 審査
- 在庫引当
- キャンペーン
- 返品
- 外部配送会社連携

---

## 6. 中心となる集約

MVPでは Order を中心集約とする。

Order は以下を含む。

- Order
- OrderParty
- OrderLine
- Delivery
- DeliveryLine

Order は注文全体の整合性を保証する。
OrderLine は明細単体の整合性を保証する。

---

## 7. 設計原則

- ドメイン知識は domain に置く
- ユースケースの流れは application に置く
- フレームワーク都合は infrastructure / presentation に閉じ込める
- API都合のDTOを domain に持ち込まない
- 永続化都合のEntityを domain に持ち込まない
- 将来拡張（支払・審査・在庫）を見越しつつ、MVPでは責務を増やしすぎない

---

## 8. 今後の拡張

将来的には以下を追加検討する。

- Payment / Billing
- Inventory
- Screening
- Shipment
- 外部連携アダプタ

---
