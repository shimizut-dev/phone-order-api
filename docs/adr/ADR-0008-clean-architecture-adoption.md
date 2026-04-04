# ADR-0008: Clean Architecture を採用する

## ステータス
採用

## 日付
2026-04-04

---

## コンテキスト

phone-order-api プロジェクトでは、アーキテクチャ設計を決定する必要がある。

候補として以下を検討した。

- Clean Architecture
- レイヤードアーキテクチャ
- Hexagonal Architecture
- MVC

検討観点

- 保守性
- テスト容易性
- 拡張性
- DDDとの相性
- 実務適用性

---

## 決定

Clean Architecture を採用する

---

## 理由

### 1. 依存関係の分離

Clean Architecture は依存関係逆転の原則に基づき、
ドメイン層を中心とした設計となる。

これにより、以下を実現できる。

- フレームワーク依存の排除
- テスト容易性の向上
- 保守性の向上

---

### 2. DDDとの相性が良い

Clean Architecture はドメイン中心設計であり、
DDDとの親和性が高い。

phone-order-apiではDDDを採用しているため、
Clean Architectureが適している。

---

### 3. テスト容易性の向上

インフラ依存を分離することで、
ユニットテストを容易に実施できる。

---

### 4. 実務での採用が増加

Clean Architectureは近年の実務プロジェクトでも
採用が増えている。

ポートフォリオとしての価値が高い。

---

## レイヤ構成

phone-order-api では以下の構成とする。

- domain
- application
- infrastructure
- presentation

---

## レイヤ責務

### domain

- エンティティ
- 値オブジェクト
- ドメインサービス
- リポジトリインターフェース

---

### application

- ユースケース
- アプリケーションサービス

---

### infrastructure

- DBアクセス
- 外部API
- Repository実装

---

### presentation

- Controller
- API

---

## レイヤードアーキテクチャとの比較

### レイヤードのメリット

- シンプル
- 学習コストが低い

### レイヤードのデメリット

- ドメインが薄くなりがち
- 依存関係が崩れやすい

---

## Hexagonal Architecture との比較

### Hexagonal のメリット

- 柔軟性が高い
- 依存関係分離

### Hexagonal のデメリット

- 学習コストが高い
- 構成が複雑

---

## MVC との比較

### MVC のメリット

- シンプル
- 学習コストが低い

### MVC のデメリット

- 大規模開発に向かない
- ドメイン設計が難しい

---

## 影響

### ポジティブな影響

- 保守性向上
- テスト容易性向上
- 拡張性向上

---

### ネガティブな影響

- 初期設計コスト増加
- 学習コスト増加

---

## 結論

phone-order-api では Clean Architecture を採用する。

DDDとの親和性が高く、保守性・拡張性に優れているため。
