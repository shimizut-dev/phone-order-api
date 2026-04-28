# テストコード命名・記載ルール

## 目的

テストコードの可読性と保守性を高め、実装者ごとの記載ゆれを防ぐ。  
特に、メソッド名・`@DisplayName`・Javadoc の役割を統一し、レビューや保守で読みやすいテストコードを維持する。

---

## 基本方針

- テストメソッド名は英語で記載する
- `@DisplayName` は日本語で記載する
- テストメソッドの Javadoc は省略不可とする
- Javadoc の共通記載ルールと生成手順は `javadoc-generation-guideline.md` を正本とする

---

## 役割の使い分け

### メソッド名

テストメソッド名は英語で記載する。  
Java / Spring の一般的な慣習に合わせ、検索性・保守性を高めるためである。

例

```text
void shouldReturnOrderWhenOrderCodeExists()
void shouldReturnBadRequestWhenRequiredFieldsAreMissing()
void shouldSaveOrderWhenRequestIsValid()
```

---

### `@DisplayName`

`@DisplayName` は日本語で記載し、テスト結果画面で表示される見出しとする。  
業務視点で何を確認しているかが、実行結果からすぐ分かることを目的とする。

例

```text
@DisplayName("注文コードで注文を参照できること")
@DisplayName("必須項目不足の場合は400を返すこと")
@DisplayName("正常なリクエストで注文を登録できること")
```

---

### Javadoc

Javadoc は省略不可とする。
Javadoc の共通記載ルール、Given / When / Then の書き方、`@throws Exception` の扱いは `javadoc-generation-guideline.md`
を参照する。

---

## 推奨する記載順

テストメソッドは次の順で記載する。

1. Javadoc
2. `@Test`
3. `@DisplayName`
4. 必要な `@Sql`、`@Transactional` など
5. テストメソッド本体

例

```text
/**
 * <pre>
 * Given 注文データが登録されている
 * When 注文コードで注文参照APIを実行する
 * Then 200 OK と注文情報が返る
 * </pre>
 */
@Test
@DisplayName("注文コードで注文を参照できること")
void shouldReturnOrderWhenOrderCodeExists() throws Exception {
    // ...
}
```

---

## テスト種類ごとのテンプレート例

API テスト、バリデーションテスト、Service テスト、Repository テスト、Mapper テストの Javadoc テンプレートは
`javadoc-generation-guideline.md` を参照する。

---

## 記載上の注意

- メソッド名・`@DisplayName`・Javadoc の内容が矛盾しないようにする
- `@DisplayName` はテスト目的、Javadoc は前提・実行内容・期待結果を表す

---

## 非推奨例

```text
/**
 * テスト
 */
@Test
void test1() {
    // ...
}
```

理由

- 何を確認しているか分からない
- 前提条件が分からない
- 期待結果が分からない
- 実装者による品質差が大きくなる
