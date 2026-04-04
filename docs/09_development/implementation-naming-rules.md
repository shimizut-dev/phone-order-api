# 実装命名ルール

## 目的

実装時の用語の揺れを防ぎ、クラス名・メソッド名・Javadoc・API名の統一性を保つ。

---

## 用語の使い分け

### 参照

「参照」は、画面・API・ユースケース名で使用する。

例

- 注文参照API
- 注文詳細参照
- 注文情報参照ユースケース

---

### 取得

「取得」は、主に1件を返す Repository / Service のメソッド名や Javadoc で使用する。

例

- IDで注文を取得
- 注文コードで注文を取得

---

### 検索

「検索」は、主に条件を指定して対象を探す Repository / Service のメソッド名や Javadoc で使用する。  
複数件を返す場合に使用しやすい。

例

- 注文ステータスで注文を検索
- 顧客IDで注文を検索

---

## 適用ルール

### 画面・API・ユースケース名

「参照」を使用する。

例

- 注文参照API
- 注文詳細参照画面
- 注文参照ユースケース

---

### Repository メソッド

1件取得系は「取得」、条件一致で複数件を返す場合は「検索」を使用する。  
Javadoc も同じ表現に統一する。

例

- `findById` : IDで注文を取得
- `findByOrderCode` : 注文コードで注文を取得
- `findAllByOrderStatus` : 注文ステータスで注文を検索

---

### Service メソッド

Repository と同様に、「取得」「検索」を使用する。

例

- `getOrderById` : IDで注文を取得
- `getOrderByOrderCode` : 注文コードで注文を取得
- `searchOrdersByStatus` : 注文ステータスで注文を検索

---

## Javadoc ルール

メソッドの Javadoc には、説明だけでなく `@param` と `@return` を付ける。

例

```java
/**
 * 注文コードで注文を取得
 *
 * @param orderCode 注文コード
 * @return 注文
 */
Optional<OrderEntity> findByOrderCode(String orderCode);