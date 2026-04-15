# 06_database

データベース設計に関する資料を管理します。

---

## 目的

- DB 設計方針を明確にする
- ER 図と命名ルールを管理する
- ID 採番や DDL 作成ルールを統一する

---

## ディレクトリ構成

```text
06_database/
├─ er-diagram/
│  ├─ er-diagram.a5er
│  └─ er-diagram.pdf
├─ README.md
├─ a5sql-ddl-generation.md
├─ database-overview.md
├─ er-diagram-guideline.md
├─ id-generation.md
└─ naming-rules.md
```

---

## 主要資料

- `database-overview.md`
    - DB 全体方針
- `id-generation.md`
    - ID 採番方針
- `naming-rules.md`
    - DB 命名ルール
- `a5sql-ddl-generation.md`
    - DDL 生成手順
- `er-diagram-guideline.md`
    - ER 図運用ルール
- `er-diagram/`
    - ER 図本体

---

## 設計方針

DB 設計は以下の流れを基本とします。

```text
ER図
↓
命名・制約設計
↓
DDL整理
↓
DB反映
↓
JPA / Repository 実装
```

ER 図を設計の起点とし、命名ルールと整合性を保ちながら実装へ反映します。
