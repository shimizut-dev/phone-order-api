# 02_domain

ドメイン設計に関する資料を管理します。

---

## 目的

- 業務用語を統一する
- モデル構造を明確にする
- ドメインルールを明文化する
- 実装前提となる業務判断を整理する

---

## ディレクトリ構成

```text
02_domain/
├─ domain-glossary.md
├─ order-aggregate.md
├─ order-line-creation-rules.md
├─ model/
│  ├─ domain-model.drawio
│  └─ domain-model.drawio.png
└─ rules/
   ├─ delivery-rules.md
   ├─ domain-rules.md
   └─ naming-rules.md
```

---

## 主要資料

- `domain-glossary.md`
    - 用語集
- `order-aggregate.md`
    - 注文集約の設計方針
- `order-line-creation-rules.md`
    - 注文明細生成ルール
- `model/`
    - ドメインモデル図
- `rules/domain-rules.md`
    - ドメイン共通ルール
- `rules/delivery-rules.md`
    - 配送関連ルール
- `rules/naming-rules.md`
    - ドメイン命名ルール

---

## 運用ルール

- ドメインルールの変更時は関連資料を同時に更新する
- 集約や生成ルールの変更時は `order-aggregate.md` と `order-line-creation-rules.md` を見直す
- ドメインの正式資料は本ディレクトリで管理する
