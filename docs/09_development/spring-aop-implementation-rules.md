# Spring / AOP 実装ルール

## 目的

Spring の proxy / AOP を前提にした実装上の注意点を統一し、動作不良や設計のぶれを防ぐ。

---

## 基本方針

- Spring 管理クラスは Spring の proxy 前提で設計する
- `@Transactional` や AOP の対象メソッドを、機械的な Java 流儀で壊さない
- constructor injection を基本にする

---

## Bean 設計のルール

- Spring Bean の依存は constructor injection にする
- フィールドインジェクションは原則使わない
- `@RequiredArgsConstructor` と `private final` を基本にする

---

## `final` のルール

- Spring Bean の `public` メソッドに一律で `final` は付けない
- `@Transactional` 対象メソッドに機械的に `final` を付けない
- proxy / AOP の拡張余地を塞がないことを優先する

---

## `@Transactional` のルール

- transaction 境界は application service に置くことを基本とする
- class レベルの既定値と method レベルの上書きを使い分ける
- 読み取り系は `@Transactional(readOnly = true)` を基本にする
- 更新系は method レベルで `@Transactional` を付ける

例:

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    @Transactional
    public Order createOrder(final OffsetDateTime orderedAt) {
        // ...
    }
}
```

---

## AOP 対象クラスのルール

- ログや監視のアスペクト対象クラスでは、public API を素直に保つ
- advice の適用を前提に、不要な `final` や複雑な継承を避ける
- AOP で横断関心を扱い、業務ロジック本体に混ぜすぎない

---

## クラス設計のルール

- Spring Bean は役割がわかるアノテーションを先に置く
- 振る舞いを決めるアノテーションをその次に置く
- Lombok は最後に置く
- 詳細は `java-annotation-order-rules.md` に従う

---

## このプロジェクトでの推奨

- service 層を transaction 境界とする
- logging は aspect / filter に寄せる
- constructor injection を徹底する
- `public` メソッドの `final` は原則使わない

