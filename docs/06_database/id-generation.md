# 🔢 ID Generation

## 概要

本資料では ID 採番ルールを定義する。

---

# 主キー

主キーは UUID を使用する。

## 理由

- 分散環境対応
- 一意性確保
- DB依存回避
- 将来的なスケール対応

---

# 業務ID

業務向け ID を別途定義する。

例

- ORD-000001
- DLV-000001

---

# 採番方式

業務IDは PostgreSQL で採番する。

PostgreSQL の sequence を使用する。

例

```
create sequence order_seq;
```

---

# 採番タイミング

採番は PostgreSQL のレコード登録時に行う。

理由

- 同時実行時の採番競合防止
- アプリケーション依存回避
- DB整合性維持
- 実務での安定性が高い

---

# 採番例

例

```
ORD-000001
ORD-000002
```

---

# 実装例

例

```
ORD- || LPAD(nextval('order_seq')::text, 6, '0')
```

---

# 対象テーブル

例

- orders
- deliveries

---

# 関連資料

- database-overview.md
- naming-rules.md
