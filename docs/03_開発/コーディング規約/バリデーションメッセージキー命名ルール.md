# ValidationMessages 運用ルール

## 目的

本ドキュメントは、`ValidationMessages.properties` / `ValidationMessages_ja.properties` で使用する
バリデーションメッセージキーの運用ルールを定義する。

OpenAPI Generator で生成されるリクエストモデルでは、OpenAPI の `required` 指定から
Bean Validation の `@NotNull` が付与される。生成コードへ個別メッセージを直接書き込まないため、
制約アノテーションのデフォルトメッセージキーを `ValidationMessages` で上書きする。

---

## 基本方針

- 生成コードに依存した個別メッセージ指定は行わない
- Bean Validation 標準のメッセージキーを優先して使用する
- API 利用者に返す必須エラーは項目名に依存しない共通文言とする
- `ValidationMessages.properties` と `ValidationMessages_ja.properties` の文言は揃える
- IDE の未使用プロパティ警告は、Bean Validation から実行時に参照されるキーとして抑止する

---

## 標準キー

制約アノテーションのデフォルトメッセージを上書きする場合は、以下の形式を使用する。

```text
jakarta.validation.constraints.<制約アノテーション名>.message
```

例

```properties
jakarta.validation.constraints.NotNull.message=必須項目です。
```

---

## 現在の設定

本プロジェクトでは、OpenAPI の `required` から生成される `@NotNull` のメッセージを以下に統一する。

```properties
# Bean Validation messages (default)
# suppress inspection "UnusedProperty"
jakarta.validation.constraints.NotNull.message=必須項目です。
```

`ValidationMessages_ja.properties` も同じキーと文言を定義する。

---

## 個別キーを追加する場合

手書きの DTO や独自バリデーションで、項目別のメッセージが必要になった場合のみ個別キーを追加する。
その場合は、アノテーション側で明示的に `message = "{...}"` を指定する。

```java

@NotNull(message = "{validation.order.orderedAt.required}")
private OffsetDateTime orderedAt;
```

個別キーを追加する場合は、以下の形式を標準とする。

```text
validation.<対象>.<項目>.<条件>
```

例

```properties
validation.order.orderedAt.required=注文日時は必須項目です。
validation.order.orderCode.invalid=注文コードの形式が不正です。
```

---

## 非推奨例

### 短すぎるキー

```properties
required=必須項目です。
orderedAt=注文日時は必須項目です。
```

理由

- 対象が分からない
- 条件が分からない
- 他キーと衝突しやすい

### 長すぎるキー

```properties
jp.co.shimizutdev.phoneorderapi.order.orderedAt.required=注文日時は必須項目です。
```

理由

- 冗長で読みづらい
- 入力ミスしやすい
- プロジェクト内で十分一意ならここまで長くする必要がない

### 生成コード前提の項目別キー

```properties
validation.order.orderedAt.required=注文日時は必須項目です。
```

理由

- OpenAPI Generator の生成モデルでは、このキーを参照する `message` 指定が生成されない
- OpenAPI 定義変更や再生成により手修正が失われる
- 現在の必須エラーは `jakarta.validation.constraints.NotNull.message` で一括管理する

---

## 運用ルール

- `required` による必須チェックは `jakarta.validation.constraints.NotNull.message` で管理する
- 新しい制約の共通メッセージを追加する場合は、`jakarta.validation.constraints.<制約>.message` を追加する
- 項目別メッセージが必要な場合だけ、明示参照される個別キーを追加する
- 使われなくなった個別キーは `ValidationMessages` から削除する
- API 例や OpenAPI の `ValidationError.message` 例は、実際の返却文言と一致させる

---

## 現時点の結論

本プロジェクトでは、必須項目不足のメッセージを以下に統一する。

```properties
jakarta.validation.constraints.NotNull.message=必須項目です。
```

OpenAPI Generator による生成コードでは、`@NotNull` のデフォルトメッセージキーを通じてこの文言が使用される。
