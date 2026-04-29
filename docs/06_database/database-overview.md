# database-overview.md

# Database Overview

## DB 製品・バージョン

- DB：PostgreSQL 16.x
- Docker イメージ：`postgres:16`
- ローカル DB と Testcontainers の DB は同じメジャーバージョンを使用する

## 削除方針

本システムでは論理削除を採用する。

削除は物理削除ではなく論理削除とする。

## 削除カラム

全テーブル共通

- deleted_at
- deleted_by

例

deleted_at timestamp
deleted_by varchar(50)

削除判定

deleted_at is null

## 採用理由

- 誤削除防止
- 監査対応
- 履歴管理
- 外部キー整合性維持

## 適用対象

原則すべてのテーブル

例

- orders
- order_lines
- deliveries
- products
- accessories

## 設計ルール

削除時

- deleted_at 更新
- deleted_by 更新

物理削除は禁止

## 共通監査カラム

注文系テーブルでは以下の監査カラムを使用する。

```text
created_at timestamptz not null
created_by varchar(50) not null
updated_at timestamptz not null
updated_by varchar(50) not null
deleted_at timestamptz
deleted_by varchar(50)
```

作成者・更新者は、アプリケーションで解決した監査ユーザーを保存する。
監査ユーザーは 50 文字以内とし、超過した場合は API 入力不正として扱う。

## orders テーブルのカラム順

`orders` テーブルは以下の順序で定義する。

```text
id
order_code
ordered_at
order_status
version
created_at
created_by
updated_at
updated_by
deleted_at
deleted_by
```
