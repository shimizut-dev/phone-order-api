# ADR-0003: PostgreSQL 16.x を採用する

## ステータス

採用

## 日付

2026-04-04

---

## コンテキスト

phone-order-api プロジェクトでは Web API のデータベースとして RDB を採用する。

候補として以下を検討した。

- PostgreSQL
- MySQL
- H2

検討観点

- 実務での採用状況
- 学習価値
- 機能性
- 本番運用適性
- テスト環境構築の容易さ
- Spring Boot との相性

---

## 決定

PostgreSQL 16.x を採用する

対象バージョン:

- PostgreSQL 16.x
- Docker イメージ: `postgres:16`

---

## 理由

### 1. 実務での採用が増えている

PostgreSQL は近年多くの企業で採用されており、
特にクラウド環境（AWS / GCP / Azure）との相性が良い。

実務想定のポートフォリオとして価値が高い。

---

### 2. 機能が豊富

PostgreSQL は以下の機能が標準で利用可能

- JSON 型
- UUID 型
- 配列型
- 高度なインデックス
- 拡張機能

phone-order-api では UUID を主キーとして採用しており、
PostgreSQL との相性が良い。

---

### 3. Testcontainers との相性が良い

本プロジェクトでは Testcontainers を使用予定。

PostgreSQL は Testcontainers の利用例が多く、
実務に近いテスト環境を構築しやすい。

---

### 4. OSS であり制約が少ない

PostgreSQL は完全な OSS であり、
ライセンス制約が少ない。

---

## MySQL との比較

### MySQL のメリット

- 採用実績が多い
- 学習情報が多い
- シンプルで扱いやすい

### MySQL のデメリット

- PostgreSQL より機能が少ない
- JSON や UUID の扱いがやや弱い
- 将来性では PostgreSQL の方が優位

---

## H2 との比較

### H2 のメリット

- 軽量
- インメモリ DB として利用可能
- テスト用途に便利

### H2 のデメリット

- 本番 DB と挙動が異なる
- SQL 方言の違い
- 実務想定としては不十分

---

## H2 を採用しない理由

phone-order-api では

- 実務想定
- Testcontainers 使用

を前提としているため、
H2 を採用するメリットが少ない。

---

## 影響

### ポジティブな影響

- 実務に近い構成になる
- 高度な DB 機能が利用可能
- Testcontainers と相性が良い

### ネガティブな影響

- H2 より起動が遅い
- ローカル環境構築がやや複雑

---

## 代替案

### MySQL

メリット

- 学習しやすい
- 採用実績が多い

デメリット

- PostgreSQL より機能が少ない

---

### H2

メリット

- 軽量
- セットアップが簡単

デメリット

- 本番との差異が発生

---

## 結論

phone-order-api では PostgreSQL 16.x を採用する。

実務想定・学習価値・将来性の観点から最も適しているため。
