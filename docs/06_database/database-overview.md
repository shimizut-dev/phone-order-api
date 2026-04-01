# database-overview.md

# Database Overview

## 削除方針

本システムでは論理削除を採用する。

削除は物理削除ではなく論理削除とする。

## 削除カラム

全テーブル共通

- deleted_at
- deleted_by

例

deleted_at timestamp
deleted_by varchar(100)

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
