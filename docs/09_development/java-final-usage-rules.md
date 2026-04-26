# Java `final` 使用ルール

## 目的

`final` の使用方針を統一し、可読性と設計意図を揃える。

---

## 基本方針

- `final` は一律に付けない
- 意味が明確な箇所にだけ付ける
- Spring の AOP / Proxy と相性が悪くなる箇所には機械的に付けない

---

## 使用ルール

### フィールド

- 再代入しないフィールドには `final` を付ける
- コンストラクタインジェクションの依存フィールドには `final` を付ける
- 値オブジェクトや entity の不変項目には `final` を付ける

例:

```java
private final OrderRepository orderRepository;
private final String value;
```

---

### 引数

- 引数への `final` は許可する
- このプロジェクトでは既存コードに合わせて付与してよい
- ただし、付けるかどうかは可読性を下げない範囲で統一する

例:

```java
public Optional<Order> getOrderByOrderCode(final String orderCode) {
    // ...
}
```

---

### ローカル変数

- 再代入しないことを明示したい場合のみ `final` を付ける
- 一時変数すべてに機械的に付けない

---

### クラス

- 値オブジェクトなど、継承させない意図を明示したい場合は `final` を付けてよい
- ただし private constructor により実質的に継承不可であれば必須ではない
- Spring 管理クラスに対しては、AOP や proxy の都合を確認してから付ける

---

### メソッド

- 各メソッドに一律で `final` は付けない
- `private` メソッドには付けない
- Spring の `@Transactional` など、proxy / AOP 対象になりうる `public` メソッドには機械的に付けない
- オーバーライド禁止に明確な設計意図がある場合だけ限定的に使用する

例:

```java
public Order createOrder(final OffsetDateTime orderedAt) {
    // ...
}

private String nullToEmpty(final String value) {
    // ...
}
```

上記のようなメソッドに `final` は付けない。

---

## このプロジェクトでの推奨

- フィールドの `final` は積極的に使う
- 引数の `final` は既存コードに合わせて使ってよい
- ローカル変数の `final` は必要なときだけ使う
- クラスの `final` は設計意図が明確なときだけ使う
- メソッドの `final` は原則として使わない

