# er-diagram-guideline.md

# ER図設計ルール

## 共通カラム

すべてのテーブルに以下を追加

created_at
created_by
updated_at
updated_by
deleted_at
deleted_by

## 削除ルール

削除は論理削除

削除時

deleted_at 更新
deleted_by 更新

物理削除は禁止
