# ADR-0006: Spring Data JPA を採用する

## ステータス

採用

## 日付

2026-04-04

---

## コンテキスト

phone-order-api プロジェクトでは、データベースアクセス層の実装方式を決定する必要がある。

候補として以下を検討した。

- Spring Data JPA
- MyBatis
- jOOQ

検討観点

- 実務での採用状況
- 学習価値
- 保守性
- 開発効率
- DDD との相性
- Spring Boot との相性

---

## 決定

Spring Data JPA を採用する

---

## 理由

### 1. DDD との相性が良い

Spring Data JPA は Repository パターンを採用しており、
DDD のアーキテクチャと相性が良い。

ドメインモデル中心の設計を実現しやすい。

---

### 2. 開発効率が高い

Spring Data JPA は以下の機能を提供する。

- CRUD の自動生成
- クエリメソッド
- ページング
- ソート

これにより、ボイラープレートコードを削減できる。

---

### 3. Spring Boot との統合が容易

Spring Boot は Spring Data JPA を標準サポートしており、
設定が簡単で導入が容易。

---

### 4. 実務での採用が多い

Spring Boot プロジェクトでは Spring Data JPA が広く採用されており、
実務想定のポートフォリオとして価値が高い。

---

## MyBatis との比較

### MyBatis のメリット

- SQL を直接記述できる
- パフォーマンス調整がしやすい
- 複雑なクエリに強い

### MyBatis のデメリット

- SQL の記述量が増える
- ドメインモデル中心の設計が難しい
- CRUD 実装が冗長になる

---

## jOOQ との比較

### jOOQ のメリット

- 型安全な SQL
- 複雑なクエリに強い
- SQL 中心設計が可能

### jOOQ のデメリット

- 学習コストが高い
- 実務採用が比較的少ない
- OSS 版の機能制限（商用版あり）

---

## 影響

### ポジティブな影響

- 開発効率向上
- DDD 設計との親和性
- Spring Boot との統合容易

### ネガティブな影響

- SQL チューニングが難しい場合がある
- ORM 特有の問題（N+1 など）

---

## 運用方針

- Repository パターンを採用
- Entity と Domain を分離
- 必要に応じて JPQL / Native Query を使用

---

## 代替案

### MyBatis

メリット

- SQL 中心設計

デメリット

- コード量増加

---

### jOOQ

メリット

- 型安全 SQL

デメリット

- 学習コスト高

---

## 結論

phone-order-api では Spring Data JPA を採用する。

DDD 設計との相性と開発効率の観点から最も適しているため。
