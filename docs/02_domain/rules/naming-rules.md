# ドメイン命名ルール

# 目的
ドメイン設計における命名ルールを統一し、可読性・保守性を向上させる。

---

# コード値の命名ルール

コード値で管理する属性は「区分」を使用する。

例
- 注文状態区分
- 配送方法区分
- 置き配区分
- 配送希望時間帯区分

特徴
- コード値で管理する
- 業務上の分類を表す
- 命名を統一できる

物理名例
- order_status_division
- delivery_method_division
- delivery_time_slot_division

---

# 可否の命名ルール

boolean（真偽値）を表す場合は「可否」を使用する。

例
- 削除可否
- 有効可否
- 配送可否
- 置き配可否

物理名例
- delete_allowed
- active_allowed
- delivery_allowed

---

# ステータスの命名ルール

状態を表す場合は「ステータス」を使用する。

例
- 注文ステータス
- 配送ステータス
- 支払ステータス

物理名例
- order_status
- delivery_status
- payment_status

---

# IDの命名ルール

エンティティ識別子は「ID」を使用する。

例
- 注文ID
- 注文明細ID
- 配送ID
- 回線ID

物理名例
- order_id
- order_detail_id
- delivery_id
- line_id

---

# 一覧の命名ルール

複数を表す場合は「一覧」を使用する。

例
- 注文明細一覧
- 回線一覧
- 配送一覧

---

# 命名ルールまとめ

| 用語 | 用途 | 例 |
|------|------|----|
| 区分 | コード値 | 配送区分 |
| 可否 | boolean | 削除可否 |
| ステータス | 状態 | 注文ステータス |
| ID | 識別子 | 注文ID |
| 一覧 | 複数 | 注文明細一覧 |

---

# 命名ルール方針

- コード値 → 区分（division）
- boolean → 可否（allowed）
- 状態 → ステータス（status）
