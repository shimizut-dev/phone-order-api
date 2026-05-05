# Javadoc作成ガイド

プロダクトコードとテストコードに記載した Javadoc から HTML ドキュメントを生成する手順を定義します。

---

## 目的

- 実装の公開契約とテストシナリオを Javadoc から確認できるようにする
- レビュー時にプロダクトコードとテストコードの説明不足を見つけやすくする
- 生成物の出力先と運用ルールを統一する

---

## 対象

- プロダクトコード: `src/main/java`
- テストコード: `src/test/java`
- OpenAPI から生成される `target/generated-sources/openapi` 配下のコードは、手動で Javadoc を編集しない

---

## 生成コマンド

PowerShell では次のコマンドを使用します。

```powershell
.\mvnw.cmd generate-sources javadoc:javadoc javadoc:test-javadoc `
  -Dshow=private `
  -Ddoclint=none `
  -Dencoding=UTF-8 `
  -Ddocencoding=UTF-8 `
  -Dcharset=UTF-8
```

Git Bash では次のコマンドを使用します。

```bash
./mvnw generate-sources javadoc:javadoc javadoc:test-javadoc \
  -Dshow=private \
  -Ddoclint=none \
  -Dencoding=UTF-8 \
  -Ddocencoding=UTF-8 \
  -Dcharset=UTF-8
```

`generate-sources` は OpenAPI 生成コードを参照する実装を解決するために実行します。  
`-Dshow=private` は package-private のテストクラスやテストメソッドも確認対象に含めるために指定します。  
`-Ddoclint=none` は OpenAPI 生成コードを含む参照解決時に、生成物由来の Javadoc 警告で生成が止まることを避けるために指定します。
本コマンドは HTML 生成確認用であり、Javadoc 品質チェック目的では使用しません。
Javadoc の品質確認はレビューまたは別途チェック用コマンドで行います。

---

## 出力先

| 対象       | 出力先                          | 入口ファイル                                  |
|----------|------------------------------|-----------------------------------------|
| プロダクトコード | `target/reports/apidocs`     | `target/reports/apidocs/index.html`     |
| テストコード   | `target/reports/testapidocs` | `target/reports/testapidocs/index.html` |

`target/` 配下は生成物のためコミット対象にしません。

---

## 記載例

プロダクトコードの Javadoc は、利用者が仕様として読む情報を中心に記載します。
実装手順の逐語説明ではなく、役割、入力、戻り値、仕様として扱う例外を明確にします。
Javadoc の本文末尾に句点「。」は付けません。

### クラス

クラスの Javadoc は名詞で記載します。
必要な場合のみ、クラスが扱う境界や責務を補足します。

```java
/**
 * 注文サービス
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    // ...
}
```

### フィールド

フィールドの Javadoc は名詞で記載します。
定数の場合は、値そのものではなく用途が分かる名詞句で記載します。

```java
/**
 * 注文リポジトリ
 */
private final OrderRepository orderRepository;

/**
 * ログ最大文字数
 */
private static final int MAX_LOG_LENGTH = 5000;
```

### コンストラクタ

コンストラクタの Javadoc は「コンストラクタ」で記載します。
引数には業務名を記載します。
特殊用途のコンストラクタでは、括弧内に用途を補足してもよいです。
生成時の制約や補正仕様がある場合は、`@param` または `<p>` 補足で説明します。

public コンストラクタの場合の例:

```java
/**
 * コンストラクタ
 *
 * @param orderedAt 注文日時
 */
public CreateOrderCommand(final OffsetDateTime orderedAt) {
    this.orderedAt = orderedAt;
}
```

インスタンス生成を防止する private コンストラクタの場合の例:

```java
/**
 * コンストラクタ(インスタンス化を防止)
 */
private OrderMapper() {
}
```

record の compact constructor では、補正や不変化など、通常のコンストラクタ呼び出し以外の仕様を記載します。

```java
/**
 * コンストラクタ
 *
 * <p>バリデーションエラー一覧は null を空リストに補正し、不変リストとして保持する</p>
 */
public ApiErrorResponse {
    validationErrors = validationErrors == null
        ? List.of()
        : List.copyOf(validationErrors);
}
```

### メソッド

メソッドの Javadoc は動詞で記載します。
処理の目的、引数、戻り値、呼び出し側が仕様として扱う例外を記載します。
業務例外や入力値不正の例外は `@throws` に記載します。

`@return` は戻り値がある場合に記載します。
`void` メソッドでは `@return` を記載しません。
戻り値が `Optional`、空リスト、`null` になり得る場合は、返却仕様が分かるように記載します。
boolean を返すメソッドでは、`true` と `false` のどちらの意味か分かるように記載します。
`@return 判定結果` のように意味が曖昧な表現は使用しません。

`Optional` を返すメソッドでは、値が存在しない場合に空の `Optional` を返すことを記載します。
コレクションを返すメソッドでは、対象が存在しない場合に空コレクションを返すのか、`null` を返し得るのかを記載します。

`@throws` は checked exception だけでなく、呼び出し側が仕様として扱う `RuntimeException` にも記載します。
業務例外、入力値不正、呼び出し側が結果として扱う例外を記載します。
内部実装都合の例外、通常は発生させない想定外例外、`NullPointerException` など低レベル例外は記載しません。

主に次のような動詞を使用します。

- 取得する
- 登録する
- 更新する
- 削除する
- 生成する
- 変換する
- 判定する
- 検証する
- 出力する

登録系のメソッドでは、プロジェクト内の表現を `登録する` に寄せます。
`作成する`、`保存する` は、業務上または技術上の意味を明確に分けたい場合のみ使用します。

```text
/**
 * 注文をキャンセルする
 *
 * @param orderCode 注文コード
 * @param version   キャンセル要求時の注文バージョン
 * @return 注文
 * @throws NullPointerException 注文コードまたはバージョンが null の場合
 * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
 * @throws OrderCannotBeCancelledException 注文の状態によりキャンセルできない場合
 * @throws OrderVersionConflictException 注文のバージョンが一致しない場合
 */
@Transactional
public Order cancelOrder(final OrderCode orderCode, final Version version) {
    // ...
}
```

単純な変換メソッドでは、変換元と変換先が分かる粒度に留めます。

```java
/**
 * 注文を注文レスポンスへ変換する
 *
 * @param order 注文
 * @return 注文レスポンス
 */
public static OrderResponse toResponse(final Order order) {
    // ...
}
```

boolean を返すメソッドでは、判定条件と `true` の意味を記載します。

```java
/**
 * 注文コード文字列が有効形式か判定する
 *
 * @param value 注文コード文字列
 * @return 注文コード文字列が有効形式の場合 true
 */
public static boolean isValid(final String value) {
    // ...
}
```

`Optional` を返すメソッドでは、該当データが存在しない場合の返却仕様を記載します。

```java
/**
 * 注文コードで注文を取得する
 *
 * @param orderCode 注文コード
 * @return 注文。存在しない場合は空の Optional
 */
Optional<Order> findByOrderCode(OrderCode orderCode);
```

一覧を返すメソッドでは、該当データが存在しない場合の返却仕様を記載します。
ページング済み一覧の場合は、ソート順、範囲外ページの扱い、ページング条件も記載します。

```java
/**
 * 注文一覧を取得する
 *
 * @param pagingCondition ページング条件
 * @return 注文日時降順、同一日時は注文コード降順のページング済み注文一覧。存在しない場合または範囲外ページの場合は空のページ
 */
PageResult<Order> findAll(PagingCondition pagingCondition);
```

### テストメソッド

テストメソッドの Javadoc は、原則として Given / When / Then で記載します。
Given / When / Then を記載する場合は `<pre>` を使用します。
Given / When / Then はそれぞれ 1 行で簡潔に記載します。
本文末尾に句点「。」は付けません。
`@DisplayName` はテスト目的、Javadoc は前提・実行内容・期待結果を表します。
テスト基盤由来の `throws Exception` は原則として `@throws` に記載しません。
テストの期待例外は Then に記載します。
`@throws` を記載する場合は、呼び出し側が仕様として扱う例外だけを具体的に記載します。

```java
/**
 * <pre>
 * Given 注文データが保存されている
 * When 注文コードで注文を取得する
 * Then 対象注文が取得できる
 * </pre>
 */
@Test
@DisplayName("注文コードで注文を取得できること")
void shouldGetOrderByOrderCode() {
    // ...
}
```

例外を期待するテストでは、Then に発生する例外を記載します。

```java
/**
 * <pre>
 * Given キャンセル対象の注文データが保存されていない
 * When 注文をキャンセルする
 * Then 注文未存在例外が発生する
 * </pre>
 */
@Test
@DisplayName("未存在の注文はキャンセルできないこと")
void shouldThrowExceptionWhenCancelTargetDoesNotExist() {
    // ...
}
```

単純なテストでは、Given / When / Then を使わず簡潔に記載してもよいです。

```java
/**
 * 不正な注文コード形式を無効と判定できること
 */
@Test
@DisplayName("不正な注文コード形式を無効と判定できること")
void shouldReturnFalseWhenOrderCodeFormatIsInvalid() {
    // ...
}
```

### 避ける表現

Javadoc は、コードを読めば分かる型名や抽象語だけで説明しません。
次の表現は意味が曖昧なため使用しません。

| 避ける表現                  | 推奨する表現の例                            |
|------------------------|-------------------------------------|
| `@param value 値`       | `@param value 注文コード文字列`             |
| `@return 戻り値`          | `@return メソッドの戻り値` ではなく業務上の返却物を記載する |
| `@return 判定結果`         | `@return 注文コード文字列が有効形式の場合 true`     |
| `@throws Exception 例外` | 原則として記載しない。必要な場合は具体的な例外と発生条件を記載する   |
| `処理する`                 | `登録する`、`変換する`、`検証する` など具体的な動詞を使用する  |

---

## 運用ルール

- クラス、フィールド、コンストラクタ、メソッドには役割に応じた Javadoc を記載する
- public なクラス、record、interface、enum、メソッドは利用者が仕様として読む前提で Javadoc を記載する
- OpenAPI から生成される `target/generated-sources/openapi` 配下のコードは手動編集しない
- Javadoc の本文末尾に句点「。」は付けない
- 仕様として呼び出し側が扱う例外は `@throws` に記載する
- `@param`、`@return`、`@throws` は抽象語だけで記載せず、仕様として意味が分かる表現にする
- boolean の `@return` は `true` または `false` の意味が分かるように記載する
- `Optional` やコレクションの `@return` は空の場合の返却仕様を記載する
- テストメソッドの Javadoc は省略せず、原則として Given / When / Then で記載する
- Given / When / Then を記載する場合は `<pre>` を使用する
- Given / When / Then はそれぞれ 1 行で簡潔に記載する
- テスト基盤由来の `throws Exception` は原則として `@throws` に記載しない
- Javadoc を更新した場合は、必要に応じて本コマンドで HTML 表示も確認する

---

## 関連資料

- `実装命名ルール.md`
- `テストコード命名とJavadocルール.md`
- `../../00_はじめに/ドキュメントガイドライン.md`
- Oracle JDK 21 公式仕様: <https://docs.oracle.com/javase/jp/21/docs/specs/javadoc/doc-comment-spec.html>
