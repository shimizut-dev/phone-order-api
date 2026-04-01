# 📦 06_database

## 概要

06_databaseではデータベース設計に関する資料を管理します。

本ディレクトリでは以下を管理対象とします。

- ER図
- テーブル定義
- DDL
- DDL変更履歴
- DB設計ルール

---

## 🎯 設計方針

データベース設計は以下の順番で行います。

ER図  
↓  
テーブル定義  
↓  
DDL生成  
↓  
DB反映  
↓  
Spring Data 実装  

ER図を設計の正とします。

---

## 📁 フォルダ構成

docs/06_database  
├─ README.md  
├─ database-overview.md  
├─ id-generation.md  
├─ naming-rules.md  
├─ er-diagram  
│  ├─ er-diagram.a5er  
│  └─ er-diagram.png  
├─ table-definition  
│  └─ table-definition.xlsx  
├─ ddl  
│  └─ schema.sql  
└─ ddl-changes  

---

## 🧰 使用ツール

- ER図: A5:SQL Mk-2
- テーブル定義: Excel
- DB: PostgreSQL
- ORM: Spring Data JPA
- IDE: IntelliJ
- テスト: Testcontainers

---

## 🔄 開発フロー

1. ER図作成
2. テーブル定義更新
3. DDL生成
4. DB反映
5. Spring Data 実装

---

## ⚠ 運用ルール

- ER図を正とする
- DB直接変更時はDDLを追加する
- Excelは生成物として扱う
- ddl-changesは削除しない
