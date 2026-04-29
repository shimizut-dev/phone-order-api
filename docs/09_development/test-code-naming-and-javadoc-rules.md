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
- テストコードはプロダクトコードの仕様書として読めるように記載する
- 入力データ、実行内容、期待結果は可能な限り固定値で明示する

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
- `assertEquals` の第一引数は、原則としてリテラルまたはリテラル相当の固定値にする
- 値オブジェクトや enum のフィールドを検証する場合は、値オブジェクト同士を比較せず、`getValue()` や `getCode()`
  などで取り出した生値をリテラルと比較する
- 動的な値を含むメッセージを検証する場合は、テストデータ側を固定値にできるなら固定値にし、固定できない場合は固定部分と動的部分を分けて検証する

---

## アサーション方針

`assertEquals` は、期待値を左、実測値を右に置く。  
期待値には、テストを読んだ時点で期待結果が分かるリテラルを置く。

テストコードはプロダクトコードの仕様書として扱う。  
そのため、正常系のレスポンス、戻り値、永続化結果、ログ本文は、仕様として重要な項目を省略せずに明示する。

推奨例

```text
assertEquals("ORD000001", actual.getOrderCode().getValue());
assertEquals("001", actual.getOrderStatus().getCode());
assertEquals(1L, actual.getVersion().getValue());
```

非推奨例

```text
assertEquals(OrderCode.of("ORD000001"), actual.getOrderCode());
assertEquals(OrderStatus.RECEIVED, actual.getOrderStatus());
assertEquals(Version.of(1L), actual.getVersion());
```

理由

- 失敗時に期待値と実測値の差分を読み取りやすい
- 値オブジェクトの等価性ではなく、テスト対象の出力値を直接確認できる
- テストごとの記載ゆれを防げる

---

## 仕様書としてのテスト記載方針

- 1つのテストで確認する仕様を、メソッド名・`@DisplayName`・Javadoc・アサーションで一致させる
- テストデータには `now()`、`OffsetDateTime.now()`、`UUID.randomUUID()`、`gen_random_uuid()` を原則使わず、固定日時・固定
  UUID・固定コードを使う
- API レスポンスは、正常系ではステータス、主要なレスポンス項目、ページング項目、エラー詳細など、仕様として公開している項目を明示的に検証する
- エラーレスポンスは、`status`、`error`、`message`、`path`、`validationErrors` の有無と内容を検証する
- ログ仕様を検証するテストでは、ログイベント名、メソッド名、引数、戻り値型、戻り値 JSON の項目、マスク対象項目を明示的に検証する
- 実行時間や自動生成 ID など固定できない値は、値そのものではなく、項目の存在や形式を検証する
- `hasItem`、`startsWith`、広すぎる `contains` は、仕様が曖昧になる場合は避け、順序・項目名・期待値を具体的に書く

推奨例

```text
mockMvc.perform(get("/api/v1/orders"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.items", hasSize(2)))
    .andExpect(jsonPath("$.items[0].orderCode").value("ORD000002"))
    .andExpect(jsonPath("$.items[0].orderStatus").value("002"))
    .andExpect(jsonPath("$.items[0].version").value(1))
    .andExpect(jsonPath("$.page").value(0))
    .andExpect(jsonPath("$.size").value(20))
    .andExpect(jsonPath("$.totalElements").value(2))
    .andExpect(jsonPath("$.totalPages").value(1))
    .andExpect(jsonPath("$.hasNext").value(false))
    .andExpect(jsonPath("$.hasPrevious").value(false));
```

非推奨例

```text
mockMvc.perform(get("/api/v1/orders"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.items[*].version", hasItem(1)));
```

理由

- どの要素のどの項目が仕様なのか分かりにくい
- レスポンス項目が欠落してもテストが通る可能性がある
- テスト結果からプロダクトコードの仕様を読み取りにくい

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
