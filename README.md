# 📱 phone-order-api

携帯電話注文API（phone-order-api）

DDD / Clean Architecture をベースにした  
携帯電話申込ドメインのサンプルプロジェクトです。

---

# 📌 概要

本プロジェクトは以下を目的としています。

- 実務レベルのドメイン設計
- Clean Architecture の適用
- DDD設計の実践
- API設計のベストプラクティス検証
- AI活用による設計品質向上

---

# 🧱 技術スタック

- Java
- Spring Boot
- DDD
- Clean Architecture
- Docker（予定）

---

# 📦 ドメイン概要

本システムは携帯電話申込を扱います。

主なドメイン：

- 注文（Order）
- 注文明細（OrderLine）
- 注文関係者（OrderParty）
- 回線（Line）
- SIM（Sim）
- 移動機（Phone）
- アクセサリ（Accessory）
- 配送（Delivery）

---

# 📚 ドキュメント構成

```text
docs/
├─ 01_requirements/
├─ 02_domain/
├─ 03_usecases/
├─ 04_architecture/
└─ 90_supporting/
```

---

# 🎯 MVPスコープ

MVPで扱う機能

- 注文作成
- 注文参照
- 注文キャンセル
- 配送作成
- 配送参照

---

# 🧭 設計方針

## DDD

- Order を集約ルートとする
- OrderLine は注文明細
- OrderParty は注文関係者
- ドメインルールはドメインで保証

## Clean Architecture

- domain
- application
- infrastructure
- presentation

---

# 📁 ディレクトリ構成

```text
phone-order-api
├─ src
├─ docs
├─ README.md
└─ build.gradle
```

---

# 🧠 設計重視プロジェクト

本プロジェクトは以下を重視しています

- 設計品質
- 保守性
- 拡張性
- テスト容易性

---

# 🚀 今後の予定

- API設計
- クラス設計
- 実装
- テスト
- CI/CD

---

# ✨ このプロジェクトの特徴

- 実務レベルのDDD設計
- AIレビュー活用
- Clean Architecture
- 高品質設計

---

# 👨‍💻 Author

個人開発プロジェクト
