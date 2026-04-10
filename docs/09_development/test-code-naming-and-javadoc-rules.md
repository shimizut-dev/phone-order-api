# テストコード命名・記載ルール

## 目的

テストコードの可読性と保守性を高め、実装者ごとの記載ゆれを防ぐ。  
特に、メソッド名・`@DisplayName`・Javadoc の役割を統一し、レビューや保守で読みやすいテストコードを維持する。

---

## 基本方針

- テストメソッド名は英語で記載する
- `@DisplayName` は日本語で記載する
- テストメソッドの Javadoc は省略不可とする
- Javadoc は日本語で記載する
- Javadoc は Gherkin 記法（Given / When / Then）を基本とする
- Gherkin 記法を記載する場合は `<pre>` を使用する
- 単純なテストでは簡潔化を許可する
- 単純なテストでは `<pre>` を使用しない
- 必要に応じて `@throws Exception` を記載する

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

Javadoc は日本語で記載し、省略不可とする。  
実装者によるばらつきを防ぎ、テストの前提・実行内容・期待結果を統一的に記述することを目的とする。

Javadoc は Gherkin 記法（Given / When / Then）を基本とする。  
Gherkin 記法を記載する場合は `<pre>` を使用する。  
ただし、単純なテストでは簡潔な記載を許可し、その場合は `<pre>` を使用しない。

---

## Javadoc に何を書くか

Javadoc には以下を記載する。

### 1. テストの対象・目的

何を確認するテストかを 1 行で記載する。

例

- 注文コードで注文を参照できること
- 必須項目不足の場合は400を返すこと
- 注文コードで注文を取得できること

### 2. 前提条件（Given）

テスト実行前の状態を記載する。

例

- 注文データが登録されている
- 必須項目が不足したリクエストを用意する
- Repository に対象データを保存済みである

### 3. 実行内容（When）

何を実行するかを記載する。

例

- 注文コードで注文参照APIを実行する
- 注文登録APIを実行する
- 注文コードで検索する

### 4. 期待結果（Then）

何を期待するかを記載する。

例

- 200 OK と注文情報が返る
- 400 Bad Request が返る
- 注文が1件取得できる

### 5. 例外情報

`throws Exception` がある場合は `@throws Exception 例外` を記載する。

---

## Javadoc に何を書かなくてよいか

Javadoc には以下のような、コードを読めば分かる詳細は記載しなくてよい。

### 1. 実装手順の逐語説明

例

- `mockMvc.perform(...)` の内容をそのまま文章で書く
- `jsonPath(...)` の条件をすべて列挙する
- SQL 文をそのまま説明する

### 2. テストコードから明らかな細部

例

- 使用している HTTP メソッド名の逐語説明
- ライブラリ名やアノテーションの説明
- 引数名の逐語説明

### 3. 汎用的すぎる説明

例

- 正常に動作すること
- 問題ないこと
- テストすること

意図が曖昧になるため避ける。

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
 * 注文コードで注文を参照できること。
 *
 * Given 注文データが登録されている
 * When 注文コードで注文参照APIを実行する
 * Then 200 OK と注文情報が返る
 * </pre>
 *
 * @throws Exception 例外
 */
@Test
@DisplayName("注文コードで注文を参照できること")
void shouldReturnOrderWhenOrderCodeExists() throws Exception {
    // ...
}
```

---

## テスト種類ごとのテンプレート例

## APIテスト

### Gherkin 記法

```text
/**
 * <pre>
 * 注文コードで注文を参照できること。
 *
 * Given 注文データが登録されている
 * When 注文コードで注文参照APIを実行する
 * Then 200 OK と注文情報が返る
 * </pre>
 *
 * @throws Exception 例外
 */
@Test
@DisplayName("注文コードで注文を参照できること")
void shouldReturnOrderWhenOrderCodeExists() throws Exception {
    // ...
}
```

### 単純なテストでは簡潔化可

```text
/**
 * 注文コードで注文を参照できること。
 *
 * @throws Exception 例外
 */
@Test
@DisplayName("注文コードで注文を参照できること")
void shouldReturnOrderWhenOrderCodeExists() throws Exception {
    // ...
}
```

---

## バリデーションテスト

### Gherkin 記法

```text
/**
 * <pre>
 * 必須項目不足の場合は400を返すこと。
 *
 * Given 必須項目が不足したリクエストを用意する
 * When 注文登録APIを実行する
 * Then 400 Bad Request が返る
 * </pre>
 *
 * @throws Exception 例外
 */
@Test
@DisplayName("必須項目不足の場合は400を返すこと")
void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {
    // ...
}
```

### 単純なテストでは簡潔化可

```text
/**
 * 必須項目不足の場合は400を返すこと。
 *
 * @throws Exception 例外
 */
@Test
@DisplayName("必須項目不足の場合は400を返すこと")
void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {
    // ...
}
```

---

## Serviceテスト

### Gherkin 記法

```text
/**
 * <pre>
 * 注文コードで注文を取得できること。
 *
 * Given Repository に対象注文が保存されている
 * When 注文コードで注文を取得する
 * Then 対象注文が取得できる
 * </pre>
 */
@Test
@DisplayName("注文コードで注文を取得できること")
void shouldGetOrderWhenOrderCodeExists() {
    // ...
}
```

### 単純なテストでは簡潔化可

```text
/**
 * 注文コードで注文を取得できること。
 */
@Test
@DisplayName("注文コードで注文を取得できること")
void shouldGetOrderWhenOrderCodeExists() {
    // ...
}
```

---

## Repositoryテスト

### Gherkin 記法

```text
/**
 * <pre>
 * 注文コードで注文を取得できること。
 *
 * Given 対象注文が保存されている
 * When 注文コードで検索する
 * Then 注文が1件取得できる
 * </pre>
 */
@Test
@DisplayName("注文コードで注文を取得できること")
void shouldFindOrderByOrderCode() {
    // ...
}
```

### 単純なテストでは簡潔化可

```text
/**
 * 注文コードで注文を取得できること。
 */
@Test
@DisplayName("注文コードで注文を取得できること")
void shouldFindOrderByOrderCode() {
    // ...
}
```

---

## Mapperテスト

### Gherkin 記法

```text
/**
 * <pre>
 * 注文Entityを注文ResponseDtoへ変換できること。
 *
 * Given 注文Entityを用意する
 * When 注文ResponseDtoへ変換する
 * Then 必要な項目が正しく設定される
 * </pre>
 */
@Test
@DisplayName("注文Entityを注文ResponseDtoへ変換できること")
void shouldMapOrderEntityToOrderResponseDto() {
    // ...
}
```

### 単純なテストでは簡潔化可

```text
/**
 * 注文Entityを注文ResponseDtoへ変換できること。
 */
@Test
@DisplayName("注文Entityを注文ResponseDtoへ変換できること")
void shouldMapOrderEntityToOrderResponseDto() {
    // ...
}
```

---

## 記載上の注意

- `@DisplayName` と Javadoc の1行目は、できるだけ同じ意味にそろえる
- メソッド名・`@DisplayName`・Javadoc の内容が矛盾しないようにする
- Gherkin 記法を採用する場合は `<pre>` を使用する
- 単純なテストでは `<pre>` を使用しない
- Gherkin 記法を採用する場合でも、冗長になりすぎないようにする
- 単純なテストでは簡潔化してよいが、意図が曖昧にならないことを優先する
- 重要な業務シナリオのテストでは、Gherkin 記法を優先する

---

## 推奨例

```text
/**
 * <pre>
 * 注文コードで注文を参照できること。
 *
 * Given 注文データが登録されている
 * When 注文コードで注文参照APIを実行する
 * Then 200 OK と注文情報が返る
 * </pre>
 *
 * @throws Exception 例外
 */
@Test
@DisplayName("注文コードで注文を参照できること")
void shouldReturnOrderWhenOrderCodeExists() throws Exception {
    // ...
}
```

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
