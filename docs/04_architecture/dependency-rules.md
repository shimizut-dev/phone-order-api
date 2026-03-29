# dependency-rules.md

## 1. 目的

本ドキュメントは `phone-order-api` における依存関係ルールを定義する。

---

## 2. 基本ルール

- 依存方向は外側から内側のみとする
- domain は最内層とする
- domain は他層に依存してはならない
- application は domain にのみ依存する
- presentation は application に依存する
- infrastructure は domain / application の interface に依存する

---

## 3. 許可する依存

### 3.1 presentation

依存可

- application
- presentation 内部

依存不可

- infrastructure の具体実装
- domain への直接操作を前提とした実装

### 3.2 application

依存可

- domain
- application 内部

依存不可

- presentation
- infrastructure の具体クラス
- Spring MVC のアノテーション
- JPA Entity

### 3.3 domain

依存可

- domain 内部
- Java標準ライブラリの必要最小限

依存不可

- Spring Framework
- JPA
- MyBatis
- HTTP関連ライブラリ
- JSONライブラリ
- DB接続処理

### 3.4 infrastructure

依存可

- domain
- application
- infrastructure 内部
- フレームワーク

---

## 4. 特に禁止すること

- domain に `@Entity` を付ける
- domain に `@Table` を付ける
- domain に controller request を持ち込む
- application が repository 実装クラスを直接 new する
- presentation が domain の永続化を直接行う

---

## 5. repository の扱い

- repository interface は domain に置く
- repository 実装は infrastructure に置く
- application は interface を通して利用する

---

## 6. DTO の扱い

- request / response DTO は presentation に置く
- usecase 入出力 DTO は application に置く
- domain object を APIレスポンス都合で変形しない

---

## 7. 例外の扱い

- domain 固有の業務エラーは domain exception とする
- presentation では domain exception をそのまま扱わず、HTTP エラーへ変換する

---
