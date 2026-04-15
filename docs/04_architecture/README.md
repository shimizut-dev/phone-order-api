# 04_architecture

アーキテクチャ設計に関する正式ドキュメントを管理します。

---

## 目的

- アーキテクチャ方針の明確化
- レイヤ責務の定義
- 依存ルールの明確化
- パッケージ構成とレイヤ構成の整合性確保

---

## 管理資料

- `architecture-overview.md`
- `clean-architecture.md`
- `dependency-rules.md`
- `layer-and-package-mapping.md`
- `package-structure.md`
- `naming-conventions.md`
- `dev-environment.md`
- `presentation-layer.md`
- `application-layer.md`
- `domain-layer.md`
- `infrastructure-layer.md`

---

## 運用ルール

- レイヤ責務や依存方向を変更した場合は必ず更新する
- 命名規則のうち実装寄りのものは `09_development` と役割を分ける
- 上位方針はここに、実装時の具体ルールは `09_development` に書く
