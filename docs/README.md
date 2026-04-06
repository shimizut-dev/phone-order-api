# 📱 phone-order-api

移動機注文API（phone-order-api）

DDD / Clean Architecture をベースにした  
移動機申込ドメインのサンプルプロジェクトです。

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

主なドメイン

- Order
- OrderLine
- OrderParty
- Line
- Sim
- Phone
- Accessory
- Delivery

---

# 📚 ドキュメント構成

詳細設計は docs 配下に格納

```
docs/
├─ 01_requirements/
├─ 02_domain/
├─ 03_usecases/
├─ 04_architecture/
└─ 90_supporting/
```

---

# 🎯 MVPスコープ

- 注文作成
- 注文参照
- 注文キャンセル
- 配送作成
- 配送参照

---

# 🧭 設計方針

## DDD

- Order を集約ルート
- ドメイン整合性保証
- 値オブジェクト活用

## Clean Architecture

- presentation
- application
- domain
- infrastructure

---

# 👨‍💻 Author

個人開発プロジェクト
