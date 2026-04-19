# ログ運用ルール

## 目的

本ドキュメントは、`phone-order-api` におけるログの実装方針と運用ルールを定義する。  
ログの記載ゆれを防ぎ、調査しやすく、安全に運用できる状態を維持することを目的とする。

---

## 基本方針

- ログは **調査・障害解析・動作確認** のために出力する
- ログは **読みやすさ** と **安全性** を優先する
- ログメッセージの形式はプロジェクト内で統一する
- 個人情報・認証情報・機微情報は **必ずマスキング** する
- 本番環境だけでなく、**全環境でマスキングを有効化** する
- 環境差は「マスキング有無」ではなく **ログ量・詳細度** で制御する
- ログの責務は層ごとに分ける
- 必要以上に大量のログを出さない

---

## ログの責務分担

### HTTPログ

HTTPの入出力は `RequestResponseLogFilter` で出力する。

対象

- HTTPメソッド
- リクエストURI
- クエリ文字列
- パラメータ
- Content-Type
- クライアントIP
- リクエストID
- リクエストヘッダー
- リクエストボディ
- レスポンスステータス
- レスポンスヘッダー
- レスポンスボディ
- レスポンスサイズ
- 処理時間
- 例外クラス
- 例外メッセージ

### メソッドログ

アプリケーション処理の開始・終了は `MethodLogAspect` で出力する。

対象

- メソッド開始
- 引数
- メソッド終了
- 戻り値
- 実行時間
- 例外クラス
- 例外メッセージ

### SQLログ

SQLログは Hibernate の logger 設定で制御する。  
常時詳細に出し続けるのではなく、必要に応じてレベルを切り替える。

---

## traceId 運用

### 方針

- リクエスト単位の追跡用に `traceId` を使用する
- `TraceIdFilter` で MDC に `traceId` を設定する
- ログフォーマットに `traceId` を含める
- 同一リクエスト内のログを `traceId` で追跡できる状態を維持する

### ルール

- `traceId` は自動採番とする
- アプリケーションコード側で手動採番しない
- ログ出力時に `traceId` を必須項目とする
- `traceId` を業務項目の代わりに使わない

---

## ログメッセージ形式

### 基本形式

ログメッセージは、用途が分かる接頭辞を付けて統一する。

例

```text
[request] ...
[response] ...
[method start] ...
[method end] ...
[method exception] ...
[exception] ...
```

### 記載ルール

- 接頭辞は小文字の英語で統一する
- 項目名も原則として小文字英語で統一する
- 同じ意味の項目名を複数作らない
- ログ文言は実装ごとに表記揺れを起こさない

### 例

```text
[request] http method: POST
[request] request uri: /api/v1/orders
[response] status: 400
[method start] methodName: OrderService#createOrder
[method end] duration(ms): 12
```

---

## ログレベル運用

### INFO

通常の業務処理の流れを把握するためのログを出力する。

対象例

- HTTPリクエスト開始
- HTTPレスポンス終了
- メソッド開始
- メソッド終了
- 実行時間
- 必要最小限の業務イベント

### WARN

異常系だが、アプリケーション継続可能な事象を出力する。

対象例

- 業務例外
- 入力不正
- 例外ハンドリング済みエラー
- 接続再試行や一時的失敗

### ERROR

処理継続が困難な異常、または明確な障害を出力する。

対象例

- 想定外例外
- 外部接続不可
- DB接続不可
- システム障害

### DEBUG

調査用の補助ログを出力する。  
通常運用では多用しない。

### TRACE

詳細すぎる調査ログを出力する。  
通常運用では無効を基本とする。

### デフォルト設定

全環境共通のログレベルは `src/main/resources/application.properties` で管理する。

```properties
logging.level.root=INFO
logging.level.jp.co.shimizutdev.phoneorderapi=INFO
```

- root logger は `INFO` を基本とし、フレームワークやライブラリの詳細ログを常時出さない
- アプリケーションパッケージも `INFO` を基本とし、通常運用で必要なログを出力する
- テストでは `src/test/resources/application.properties` で `logging.level.jp.co.shimizutdev.phoneorderapi=TRACE`
  に上書きし、詳細ログを確認しやすくする
- ローカル開発やSQL調査向けの追加設定は、プロファイル別の properties に分離する

---

## HTTPログ出力ルール

### 出力する項目

#### リクエスト

- http method
- request uri
- query string
- parameters
- content-type
- client ip
- request id
- headers
- body

#### レスポンス

- status
- content-type
- headers
- body
- size(bytes)
- duration(ms)

#### 例外発生時

- exception class
- exception message

### 注意点

- GETなど body を持たないリクエストは無理に body を出さない
- 非対応 Content-Type は `[unsupported content type]` として扱う
- body は 1 行化して出力する
- body は最大文字数で切り詰める
- レスポンス body を壊さないよう `ContentCachingResponseWrapper` を使用する

---

## メソッドログ出力ルール

### 対象層

メソッドログは以下を対象とする。

- Controller
- Service
- Repository 実装

### 出力する項目

#### 開始時

- methodName
- arguments

#### 終了時

- methodName
- return value
- duration(ms)

#### 例外時

- methodName
- exception class
- exception message
- duration(ms)

### 注意点

- ログ出力対象は広げすぎない
- 全クラスへ indiscriminate に適用しない
- フレームワーク内部やライブラリ内部には適用しない
- 引数や戻り値はマスキング後の値を出力する
- 大きすぎる値は切り詰める

---

## マスキングルール

### 基本方針

- マスキングは **全環境で常時有効** とする
- 開発環境だけ非マスクにしない
- マスキング判定ロジックは **1か所に集約** する
- HTTPログとメソッドログの両方で同じマスキングルールを使う

### 実装方針

- マスキング処理は `LogMasker` に集約する
- `RequestResponseLogFilter` と `MethodLogAspect` は `LogMasker` を利用する
- `@ToString` にマスキング責務を持たせない
- `logback` の regex マスキングは補助用途に限定する

### マスク対象ヘッダー

以下はマスク対象とする。

- Authorization
- Cookie
- Set-Cookie
- X-API-Key
- Proxy-Authorization

### マスク対象ボディ項目

以下は代表例であり、必要に応じて追加する。

- password
- passwd
- token
- access_token
- refresh_token
- authorization
- secret
- api_key
- apikey
- card_number
- cardnumber
- phone_number
- phonenumber
- email
- mail

### マスク方法

- ヘッダーはヘッダー名ベースで判定する
- JSON body はキー名ベースで判定する
- マスク値は `****` とする
- JSONでない文字列は 1 行化のみ行う
- 必要以上に複雑な regex ベース実装へ寄せない

---

## SQLログ運用

### 基本方針

SQLログは調査時に有効だが、常時詳細出力はログ量が多くなるため注意する。

### 設定方針

通常は properties の `logging.level.*` で制御する。

全環境共通のログレベルは `application.properties`、環境別の詳細ログは `application-*.properties` に分離する。

例

```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.orm.jdbc.bind=TRACE
```

### ルール

- SQL文確認が必要なときだけ有効化する
- 常時 `TRACE` を前提にしない
- `extract` ログは必要時のみ有効化する
- `spring.jpa.show-sql=true` は使用しない
- SQLログの責務は properties の `logging.level.*` に寄せる
- ログフォーマットや appender は `logback-spring.xml` に寄せる

---

## 環境差の考え方

### 共通

- マスキングは全環境で有効
- traceId は全環境で有効
- ログ形式は全環境で可能な限り統一

### 開発環境

- 調査しやすさを優先してログ量をやや多めにしてよい
- SQLログを一時的に有効化してよい
- body ログは必要範囲で有効化してよい

### 本番環境

- 必要最小限のログ量に抑える
- ただし調査不能にならない範囲で情報を残す
- SQLの詳細ログは通常無効
- 機微情報は必ずマスキングする

---

## 出力してはいけないもの

以下は原則としてログへ生値を出力しない。

- パスワード
- トークン
- APIキー
- Cookie
- セッションID
- クレジットカード情報
- 個人情報
- メールアドレス
- 電話番号
- 認証ヘッダー
- 機微な業務情報

---

## 実装ルール

### 新規ログ追加時

- 既存の接頭辞・表記ルールに合わせる
- まず INFO / WARN / ERROR のどれに当たるか整理する
- 機微情報を含まないか確認する
- 含む場合は `LogMasker` でマスキングする
- 同じ内容を二重出力しない

### 新規マスク対象追加時

- `LogMasker` の対象一覧へ追加する
- HTTPログとメソッドログの両方への影響を確認する
- 単体テストを追加する

### テスト方針

以下はテスト対象とする。

- `LogMasker`
- `RequestResponseLogFilter`
- `GlobalExceptionHandler` のレスポンス形式
- `MethodLogAspect`

---

## 推奨しない実装

- 各クラスで独自にマスキング実装を書く
- `@ToString` にマスキング責務を持たせる
- ログ文言を都度自由に決める
- 重要情報を DEBUG にだけ書く
- 本番だけマスキングする
- 開発だけ非マスクにする
- 何でも TRACE で常時出す

---

## 今後の拡張候補

必要に応じて以下を追加検討する。

- マスク対象キーの外部設定化
- 環境別のログレベルテンプレート
- 業務イベントログの追加
- 外部API呼び出しログの標準化
- バッチ処理ログの標準化
- 監査ログの別管理

---

## まとめ

本プロジェクトでは、以下をログ運用の基本とする。

- HTTPログは `RequestResponseLogFilter` で出力する
- メソッドログは `MethodLogAspect` で出力する
- `traceId` によりリクエスト単位で追跡可能にする
- マスキングは `LogMasker` に集約する
- マスキングは全環境で有効とする
- 環境差はログ量と詳細度で制御する
- SQLログは必要時にのみ詳細化する
- ログは調査しやすさと安全性の両立を重視する
