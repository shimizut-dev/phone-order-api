# infrastructure-layer.md

## 1. 目的

本ドキュメントは infrastructure layer の責務を定義する。

---

## 2. 役割

infrastructure layer は技術的関心事を担当する。

主な役割は以下の通り。

- 永続化
- 外部連携
- フレームワーク設定
- domain/application が必要とする interface の実装

---

## 3. MVP時点で想定する主な構成

- OrderRepositoryImpl
- JPA Entity
- DB Mapper
- Configuration

---

## 4. repository 実装方針

- domain の repository interface を実装する
- DB entity と domain object を変換する
- 永続化の都合を domain に漏らさない

---

## 5. infrastructure に置くもの

- JPA Entity
- Spring Data Repository
- Repository 実装
- DB Mapper
- 外部接続設定

---

## 6. infrastructure に置かないもの

- ユースケース本体
- ドメインルール
- APIリクエスト/レスポンス定義

---

## 7. 将来拡張

将来は以下の実装追加を想定する。

- 外部配送会社連携
- 在庫システム連携
- 支払システム連携
- 審査システム連携

---
