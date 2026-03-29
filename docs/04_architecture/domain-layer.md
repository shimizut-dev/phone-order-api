# domain-layer.md

## 1. 目的

本ドキュメントは domain layer の責務と構成を定義する。

---

## 2. 役割

domain layer は本システムの業務知識を表現する中心である。

主な役割は以下の通り。

- 業務概念の表現
- ドメインルールの保持
- 不変条件の保証
- 不正状態の防止

---

## 3. 主な要素

### 3.1 Entity

- Order
- OrderParty
- OrderLine
- Delivery
- DeliveryLine
- Line
- Sim
- Phone
- Accessory

### 3.2 ValueObject

- OrderId
- OrderLineId
- OrderQuantity
- PersonName
- PersonNameKana
- Address
- PostalCode
- Prefecture
- City
- Town
- StreetAddress
- BuildingName
- SimForm
- SimStatus
- OrderStatus
- DeliveryQuantity

### 3.3 Aggregate

- Order

### 3.4 Repository interface

- OrderRepository

### 3.5 DomainException

- 業務ルール違反を表す例外

---

## 4. 責務

### 4.1 Order

- 注文全体の整合性保証
- 注文関係者一覧の管理
- 注文明細一覧の管理
- 配送一覧の管理
- 配送数量整合性保証

### 4.2 OrderLine

- 明細種別と保持要素の整合性保証
- 不正状態の生成防止

### 4.3 Delivery / DeliveryLine

- 配送情報の保持
- 配送数量ルールの一部表現

---

## 5. domain に置かないもの

- HTTP リクエスト/レスポンス
- DB entity
- SQL
- 外部API連携
- Spring のアノテーション
- トランザクション制御

---

## 6. 実装方針

- コンストラクタで不正状態を防ぐ
- 明細生成は static factory method を優先する
- 値オブジェクトで型安全を高める
- null の持ち込みを最小限にする

---
