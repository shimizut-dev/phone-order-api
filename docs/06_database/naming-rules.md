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

- **物理テーブル名は実装で採用した名前を正とする**
- 複数形 / 単数形は、プロジェクトで採用した物理名に合わせる
- 制約名・インデックス名は、**実際の物理テーブル名** をそのまま使用する
- 業務上の実体を表す名詞を使う
- 略語は多用しない
- ただし、業務上意味が定着している略語は使用可とする

例

```text
orders
order_details
customers
products
deliveries
```

補足

- 本プロジェクトでは、既存DDLとの整合を優先し、物理テーブル名として `orders` を採用している
- 制約名も `orders` をそのまま使用する

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

- 主キーカラム名は原則として **`id`** に統一する
- 型は `uuid` を使用する
- 採番方式はプロジェクト方針に従う

例

```text
id uuid primary key
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

```text
varchar(10)
```

---

### 4.6 一意制約

業務向けIDは、必要に応じて **一意キー制約（UNIQUE）** を付与する。

例

```text
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

```text
order_line_kind varchar(10) not null
```

補足

- 現時点で3桁運用でも、将来拡張を考慮して `varchar(10)` を許容する
- ただし、過度に大きい `varchar(20)` は原則避ける

---

## 6. 監査項目の命名規則

監査項目は以下に統一する。

| 論理名  | 物理名          | 型             | 備考         |
|------|--------------|---------------|------------|
| 作成日時 | `created_at` | `timestamptz` | `not null` |
| 作成者  | `created_by` | `varchar(50)` | `not null` |
| 更新日時 | `updated_at` | `timestamptz` | `not null` |
| 更新者  | `updated_by` | `varchar(50)` | `not null` |

例

```text
created_at timestamptz not null default CURRENT_TIMESTAMP,
created_by varchar(50) not null,
updated_at timestamptz not null default CURRENT_TIMESTAMP,
updated_by varchar(50) not null
```

---

## 7. 制約名の命名規則

制約名は、DBエラー時に判別しやすいように命名する。

### 7.1 基本方針

- 制約名には、**実際の物理テーブル名** と **実際の物理カラム名** を使用する
- 物理テーブル名が複数形の場合でも、制約名ではそのまま複数形を使う
- カラム名は定義順で連結する
- 接頭辞で制約種別を表す

### 7.2 接頭辞

| 種別     | 接頭辞  |
|--------|------|
| 主キー    | `pk` |
| 外部キー   | `fk` |
| 一意キー制約 | `uk` |
| チェック制約 | `ck` |

### 7.3 命名規則

| 種別     | 命名規則                                              | 例                                  |
|--------|---------------------------------------------------|------------------------------------|
| 主キー    | `pk_<physical_table_name>`                        | `pk_orders`                        |
| 外部キー   | `fk_<physical_table_name>_<physical_column_name>` | `fk_order_details_order_id`        |
| 一意キー制約 | `uk_<physical_table_name>_<physical_column_name>` | `uk_orders_order_code`             |
| 複合一意キー | `uk_<physical_table_name>_<col1>_<col2>`          | `uk_orders_customer_id_order_code` |
| チェック制約 | `ck_<physical_table_name>_<physical_column_name>` | `ck_orders_order_status`           |

### 7.4 一意キー制約の補足

- 一意キー制約名は **`uk_`** を接頭辞とする
- 物理テーブル名は **実装DDLのテーブル名をそのまま使用する**
- 単一カラムの一意キー制約は **`uk_<table>_<column>`** とする
- 複合一意キー制約は **定義順にカラム名を連結** する
- 既存DDLが `orders` のような複数形テーブル名を採用している場合も、制約名では `orders` をそのまま使用する

例

```text
uk_orders_order_code
uk_orders_customer_id_order_code
```

---

## 8. インデックス名の命名規則

| 種別       | 命名規則                             | 例                                     |
|----------|----------------------------------|---------------------------------------|
| 通常インデックス | `idx_<table_name>_<column_name>` | `idx_orders_order_code`               |
| 複合インデックス | `idx_<table_name>_<col1>_<col2>` | `idx_orders_customer_id_order_status` |

---

## 9. 非推奨ルール

以下は原則として採用しない。

- 実際の物理テーブル名と異なる制約名を付けること
- 意味が曖昧な略語
- `kbn` `flg` `no` の乱用
- 可変フォーマットの業務向けID
- 過度に長い `varchar`

例

```text
uk_order_order_code
orderDtl
customerNo
status_kbn
varchar(20) の業務向けID
```

---

## 10. まとめ

- テーブル名は **実装で採用した物理名を正とする**
- カラム名は英小文字 + スネークケース
- 主キーは `id` + `uuid`
- 外部キーは `参照先テーブル名_id`
- 業務向けIDは `対象名_code`
- 業務向けIDの形式は `XXX-000001`
- 業務向けIDの型は原則 `varchar(10)`
- 区分値コメントはコードと意味を両方書く
- 監査項目は `created_at / created_by / updated_at / updated_by` に統一する
- 一意キー制約名は `uk_<physical_table_name>_<physical_column_name>` とする
