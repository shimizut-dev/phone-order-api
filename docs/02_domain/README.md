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
- `model/`
    - ドメインモデル図
- `rules/domain-rules.md`
    - ドメイン共通ルール
- `rules/delivery-rules.md`
    - 配送関連ルール
- `rules/naming-rules.md`
    - ドメイン命名ルール

---

## 補足

`90_supporting` にある `order-aggregate.md` と `order-line-creation-rules.md` は、必要に応じて本ディレクトリへ移管を検討します。
