# バリューオブジェクト設計ルール

## 目的

バリューオブジェクトの作り方を統一し、不変条件を型に閉じ込める。

---

## 基本方針

- raw な値をそのまま渡さず、意味のある型で包む
- 不変条件は constructor / factory method 内で検証する
- 小さく独立した型として定義する
- 継承より自己完結した設計を優先する

---

## クラス設計のルール

- 1つの値を表す小さな型として作る
- フィールドは `private final`
- constructor は `private` を基本とする
- 生成は `of` などの factory method 経由にする

例:

```java
public class OrderCode {

    private final String value;

    private OrderCode(final String value) {
        // validation
        this.value = value;
    }

    public static OrderCode of(final String value) {
        return new OrderCode(value);
    }
}
```

---

## バリデーションのルール

- 不正な値は生成時に拒否する
- `IllegalArgumentException` など明確な例外で失敗させる
- 事前判定が有用な場合のみ `isValid` を持たせる
- `isValid` は constructor / `of` の代替ではなく補助として扱う

---

## 命名のルール

- `OrderCode`, `OrderId`, `OrderedAt` のように業務上の意味をそのまま名前にする
- raw 型名を前面に出さない
- 値へのアクセスは `getValue()` に揃える

---

## 共通化のルール

- 基底クラスや共通 interface は安易に導入しない
- 似ていても、不変条件や内部型が異なるなら個別クラスのままにする
- 共通化するのは、実際に重複が痛くなってから検討する

---

## Lombok のルール

- `@Getter`, `@ToString`, `@EqualsAndHashCode` は使ってよい
- `@Data` は使わない
- `@Value` はこのプロジェクトでは基本としない

---

## `final` の考え方

- `final class` は必要なら付けてよい
- ただし private constructor により実質的に継承不可なら必須ではない
- 一貫性を重視する場合は、同種の value object で揃える

---

## このプロジェクトでの推奨

- `OrderCode`, `OrderId`, `OrderedAt` のパターンを基本形とする
- factory method で生成する
- 値検証は生成時に行う
- バリューオブジェクト用の基底クラスは導入しない

