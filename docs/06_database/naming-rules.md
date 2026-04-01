# DB命名規則

## 1. 目的

本資料は `phone-order-api` におけるDBオブジェクトの命名規則を定義する。
命名を統一することで、DDL・ER図・実装コード・レビュー観点のブレを防ぐことを目的とする。

---

## 2. 前提

- DB：PostgreSQL
- ER図：A5:SQL Mk-2
- 命名は原則として **英小文字 + スネークケース** を使用する
- 論理名は日本語、物理名は英語で管理する

---

## 3. 基本方針

### 3.1 テーブル名

- **複数形にしない**
- **業務上の実体を表す名詞**を使う
- 略語は多用しない
- ただし、業務上意味が定着している略語は使用可とする

例

```text
order
order_detail
customer
product
delivery
```

---

### 3.2 カラム名

- **英小文字 + スネークケース**
- カラム名だけで意味が分かるようにする
- 不要な省略は避ける
- 外部キーは `参照先テーブル名_id` を基本とする

例

```text
id
order_code
order_id
customer_id
detail_type
created_at
updated_at
created_by
updated_by
```

---

### 3.3 主キー

- 主キー名は原則として **`id`** に統一する
- 型は `uuid` を使用する
- DBで採番する

例

```sql
id uuid primary key default gen_random_uuid()
```

---

### 3.4 外部キー

- `参照先テーブル名_id` とする
- 例：注文を参照する注文明細は `order_id`

例

```text
order_id
customer_id
product_id
```

---

## 4. 業務向けIDの命名規則

### 4.1 基本方針

- UUIDの主キーとは別に、業務上識別しやすい **業務向けID** を必要に応じて持つ
- 業務向けIDのカラム名は **`対象名_code`** を基本とする
- 型は原則 `varchar(10)` とする
- 値の形式は **`プレフィックス-6桁連番`** を基本とする

基本形式

```text
XXX-000001
```

例

```text
ORD-000001
ODT-000001
CUS-000001
PRD-000001
DLV-000001
```

---

### 4.2 カラム名ルール

| 論理名      | 物理名                  | 備考           |
|----------|----------------------|--------------|
| 注文コード    | `order_code`         | 注文の業務向けID    |
| 明細コード    | `order_detail_code`  | 注文明細の業務向けID  |
| 回線コード    | `line_code`          | 回線の業務向けID    |
| 移動機コード   | `entry_code`         | 移動機の業務向けID   |
| アクセサリコード | `accessory_code`     | アクセサリの業務向けID |
| 配送コード    | `delivery_code`      | 配送の業務向けID    |
| 配送明細コード  | `delivery_line_code` | 配送明細の業務向けID  |
| 注文関係者    | `order_party_code`   | 注文関係者の業務向けID |

---

### 4.3 プレフィックス規則

| 対象    | プレフィックス | サンプル         |
|-------|---------|--------------|
| 注文    | `ORD`   | `ORD-000001` |
| 注文明細  | `ODT`   | `ODT-000001` |
| 回線    | `LIN`   | `LIN-000001` |
| 移動機   | `PHN`   | `PHN-000001` |
| アクセサリ | `ACC`   | `ACC-000001` |
| 配送    | `DLV`   | `DLV-000001` |
| 配送明細  | `DLD`   | `DLD-000001` |
| 注文関係者 | `ORP`   | `ORP-000001` |

補足

- プレフィックスは **3文字** を基本とする
- 英大文字で管理する
- ハイフン区切りとする

---

### 4.4 桁数ルール

- 連番部分は **6桁固定** を基本とする
- 0埋めで表現する

例

```text
ORD-000001
ORD-000123
ORD-999999
```

補足

- `ORD-0001` のような可変桁は採用しない
- フォーマットを固定することで、視認性と検索性を高める

---

### 4.5 データ型

業務向けIDの例

```text
ORD-000001
```

文字数

- `ORD` = 3文字
- `-` = 1文字
- `000001` = 6文字
- 合計 = 10文字

したがって、データ型は原則として以下を採用する。

```sql
varchar(10)
```

---

### 4.6 一意制約

業務向けIDは、必要に応じて **一意制約（UNIQUE）** を付与する。

例

```sql
order_code varchar(10) not null unique
```

---

## 5. 区分・種別コードの命名規則

### 5.1 基本方針

- 種別・区分・ステータスは、意味が分かる物理名を付ける
- `kbn` のような略語は原則使用しない
- 値はコード化して管理する

例

```text
detail_type
order_status
delivery_status
```

---

### 5.2 コード値の方針

- コード値は原則として **文字列コード** で管理する
- 桁数は仕様に応じて最小限とする
- コメントにはコードと意味の両方を書く

例

```text
001:回線 / 002:移動機 / 003:アクセサリ
```

データ型例

```sql
order_linek_ind varchar(10) not null
```

補足

- 現時点で3桁運用でも、将来拡張を考慮して `varchar(10)` を許容する
- ただし、過度に大きい `varchar(20)` は原則避ける

---

## 6. 監査項目の命名規則

監査項目は以下に統一する。

| 論理名 | 物理名 | 型 | 備考 |
|---|---|---|---|
| 作成日時 | `created_at` | `timestamptz` | `not null` |
| 作成者 | `created_by` | `varchar(50)` | `not null` |
| 更新日時 | `updated_at` | `timestamptz` | `not null` |
| 更新者 | `updated_by` | `varchar(50)` | `not null` |

例

```sql
created_at timestamptz not null default CURRENT_TIMESTAMP,
created_by varchar(50) not null,
updated_at timestamptz not null default CURRENT_TIMESTAMP,
updated_by varchar(50) not null
```

---

## 7. 制約名の命名規則

制約名は、DBエラー時に判別しやすいように命名する。

| 種別 | 命名規則 | 例 |
|---|---|---|
| 主キー | `pk_<table_name>` | `pk_order` |
| 外部キー | `fk_<table_name>_<column_name>` | `fk_order_detail_order_id` |
| 一意制約 | `uk_<table_name>_<column_name>` | `uk_order_order_code` |
| チェック制約 | `ck_<table_name>_<column_name>` | `ck_order_status` |

---

## 8. インデックス名の命名規則

| 種別 | 命名規則 | 例 |
|---|---|---|
| 通常インデックス | `idx_<table_name>_<column_name>` | `idx_order_order_code` |
| 複合インデックス | `idx_<table_name>_<col1>_<col2>` | `idx_order_customer_id_order_status` |

---

## 9. 非推奨ルール

以下は原則として採用しない。

- テーブル名の複数形
- 意味が曖昧な略語
- `kbn` `flg` `no` の乱用
- 可変フォーマットの業務向けID
- 過度に長い `varchar`

例

```text
orders
orderDtl
customerNo
status_kbn
varchar(20) の業務向けID
```

---

## 10. まとめ

- テーブル名は単数形
- カラム名は英小文字 + スネークケース
- 主キーは `id` + `uuid`
- 外部キーは `参照先テーブル名_id`
- 業務向けIDは `対象名_code`
- 業務向けIDの形式は `XXX-000001`
- 業務向けIDの型は原則 `varchar(10)`
- 区分値コメントはコードと意味を両方書く
- 監査項目は `created_at / created_by / updated_at / updated_by` に統一する
