# clean-architecture.md

## 1. 目的

本ドキュメントは `phone-order-api` における Clean Architecture の適用方針を定義する。

---

## 2. 採用理由

Clean Architecture を採用する理由は以下の通り。

- ドメイン知識をフレームワークから分離するため
- 保守性を高めるため
- テストしやすい構造にするため
- 外部I/Oの変更影響を最小化するため

---

## 3. 層構成

本システムの層構成は以下とする。

### 3.1 Domain Layer

- Entity
- ValueObject
- Aggregate
- DomainService
- Repository interface

### 3.2 Application Layer

- UseCase
- Input / Output DTO
- Application Service
- Transaction 管理
- Domain の呼び出し制御

### 3.3 Infrastructure Layer

- Repository 実装
- DBアクセス
- 外部連携
- 永続化マッピング

### 3.4 Presentation Layer

- Controller
- Request / Response DTO
- Validation
- Exception Handler

---

## 4. 依存ルール

依存方向は必ず内向きとする。

- presentation -> application
- infrastructure -> application / domain
- application -> domain
- domain -> どこにも依存しない

domain は Spring Framework や DB ライブラリに依存してはならない。

---

## 5. 境界の考え方

### 5.1 Domain と Application の境界

- domain は業務ルールを表現する
- application は処理の流れを制御する

### 5.2 Application と Infrastructure の境界

- application は interface を使う
- infrastructure はその実装を提供する

### 5.3 Presentation と Application の境界

- presentation は HTTP を扱う
- application は HTTP を知らない

---

## 6. phone-order-api への適用方針

MVPでは `Order` 集約を中心に設計する。

- 注文作成ユースケース
- 注文参照ユースケース
- 注文キャンセルユースケース
- 配送作成ユースケース
- 配送参照ユースケース

これらを application 層に配置する。

domain では以下を中心に扱う。

- Order
- OrderLine
- OrderParty
- Delivery
- DeliveryLine
- Line
- Sim
- Phone
- Accessory

---

## 7. 採用しないこと

MVP時点では以下を無理に導入しない。

- 過度に細かいサービス分割
- CQRS の本格導入
- Event Sourcing
- 複雑なメッセージ駆動構成
- 支払や審査の先行実装

---
