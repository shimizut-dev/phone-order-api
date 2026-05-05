# ER図作成ガイド

本書は `phone-order-api` の ER 図を更新する際のガイドを整理する。  
ER 図自体は補助資料であり、DDL の正本は Flyway migration とする。

---

## 概要

- ER 図更新時の前提と注意点を整理する
- 共通監査カラムと論理削除カラムの扱いを統一する
- migration と ER 図の差分を減らす

---

## 内容

### 方針

- ER 図は現行 migration の構造を反映する
- テーブル追加やカラム変更時は ER 図も合わせて更新する
- 実装に存在しない概念を ER 図へ先行追加しない

### ルール

- `orders` テーブルには `id`, `order_code`, `ordered_at`, `order_status`, `version` を含める
- 監査カラムとして `created_at`, `created_by`, `updated_at`, `updated_by`, `deleted_at`, `deleted_by` を含める
- 論理名と物理名がずれないように管理する
- 制約名や採番オブジェクト名は migration と同じ名前を使う
- ER 図にない構造を migration へ追加した場合は、後追いで必ず反映する

### 手順

1. Flyway migration の最新状態を確認する
2. ER 図のテーブル、カラム、制約を migration に合わせて更新する
3. A5:SQL Mk-2 で保存し、必要に応じて PDF を出力し直す

---

## 関連資料

- `../データベース概要.md`
- `../命名ルール.md`
- `A5SQL_DDL生成手順.md`
