# ValidationMessages キー命名ルール

## 目的

本ドキュメントは、`ValidationMessages.properties` / `ValidationMessages_ja.properties` で使用する
バリデーションメッセージキーの命名ルールを定義する。

キーの記載ゆれを防ぎ、意味が分かりやすく、保守しやすい状態を維持することを目的とする。

---

## 基本方針

- キーは **用途 / 対象 / 項目 / 条件** が分かる構造にする
- キーは **小文字 + ドット区切り** で記載する
- Java のパッケージ名はキーに含めない
- 必要以上に長いキーにしない
- 項目名は Java のフィールド名に合わせる
- 条件は末尾に統一して付与する
- キー名はプロジェクト全体で一貫させる

---

## 推奨フォーマット

以下の形式を標準とする。

```text
validation.<対象>.<項目>.<条件>
```

例

```properties
validation.order.orderedAt.required=注文日時は必須です。
validation.order.orderCode.required=注文コードは必須です。
validation.order.orderCode.invalid=注文コードの形式が不正です。
validation.order.orderStatus.invalid=注文状態が不正です。
```

---

## 各要素の意味

### validation

固定プレフィックス。  
このキーが **バリデーションメッセージ用** であることを示す。

### 対象

どの画面 / API / ドメイン / リクエストの項目かを表す。

例

- `order`
- `customer`
- `delivery`

### 項目

どの入力項目かを表す。  
原則として Java のフィールド名に合わせる。

例

- `orderedAt`
- `orderCode`
- `customerName`
- `phoneNumber`

### 条件

どのバリデーション条件かを表す。  
原則として最後に付ける。

例

- `required`
- `invalid`
- `size`
- `min`
- `max`
- `pattern`

---

## 具体例

### 必須

```properties
validation.order.orderedAt.required=注文日時は必須です。
validation.order.orderCode.required=注文コードは必須です。
```

### 形式不正

```properties
validation.order.orderCode.invalid=注文コードの形式が不正です。
validation.order.phoneNumber.pattern=電話番号の形式が不正です。
```

### 文字数制限

```properties
validation.order.customerName.size=顧客名は文字数制限を超えています。
validation.order.note.max=備考は上限文字数を超えています。
```

---

## 命名時の判断基準

キーは次の条件を満たすこと。

- 見ただけで意味が分かる
- どの項目に対するメッセージか分かる
- どの条件に対するメッセージか分かる
- 長すぎず記述しやすい
- 他キーと衝突しない

---

## 非推奨例

### 1. 短すぎるキー

```properties
required=必須です。
orderedAt=注文日時は必須です。
```

理由

- 対象が分からない
- 条件が分からない
- 他キーと衝突しやすい

### 2. 長すぎるキー

```properties
jp.co.shimizutdev.phoneorderapi.order.orderedAt.required=注文日時は必須です。
```

理由

- 冗長で読みづらい
- 入力ミスしやすい
- プロジェクト内で十分一意ならここまで長くする必要がない

### 3. 表記ゆれがあるキー

```properties
validation.order.orderedAt.required=注文日時は必須です。
validation.order.ordered_at.required=注文日時は必須です。
validation.orders.orderedAt.required=注文日時は必須です。
```

理由

- 同じ意味で複数表記が混在すると保守性が落ちる
- 項目名や対象名は統一する必要がある

---

## 条件名の推奨一覧

よく使う条件名は以下を推奨する。

- `required`
- `invalid`
- `size`
- `min`
- `max`
- `pattern`
- `past`
- `future`

---

## 運用ルール

- 新しいバリデーションメッセージを追加する場合は、本ルールに従う
- 既存キーと似た意味のキーを重複して作らない
- 項目名を変更した場合は、キー名も必要に応じて見直す
- バリデーションメッセージは `ValidationMessages.properties` と `ValidationMessages_ja.properties` に配置する
- 例外メッセージは本ルールの対象外とし、Java 定数で管理する

---

## 現時点の結論

本プロジェクトでは、以下の形式を標準とする。

```properties
validation.order.orderedAt.required=注文日時は必須です。
```

この形式を基準として、今後のバリデーションメッセージを追加する。
