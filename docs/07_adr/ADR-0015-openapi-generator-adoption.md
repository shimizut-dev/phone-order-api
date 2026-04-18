# ADR-0015: OpenAPI Generator を採用する

## ステータス

採用

## 日付

2026-04-19

---

## コンテキスト

phone-order-api では、API の外部仕様を `docs/05_api/openapi.yaml` で管理している。

これまでは API のリクエスト / レスポンスモデルや Controller のメソッドシグネチャを手書きしていたため、
OpenAPI 定義と実装の間にずれが発生する余地があった。

今後、API 仕様を正本として運用するためには、OpenAPI 定義から実装側の境界コードを生成し、
仕様変更が実装に反映される流れを Maven ライフサイクルに組み込む必要がある。

---

## 決定

OpenAPI Generator を採用する。

`openapi-generator-maven-plugin` を使用し、Maven の `generate-sources` フェーズで
`docs/05_api/openapi.yaml` から API インターフェースと API 入出力モデルを生成する。

生成物は `target/generated-sources/openapi` 配下に出力し、手動編集しない。

---

## 採用方針

### 1. OpenAPI を API 仕様の正本とする

API のエンドポイント、リクエスト、レスポンス、エラー構造は `docs/05_api/openapi.yaml` に定義する。

API 入出力モデルの項目追加、型変更、必須項目変更は Java の生成物ではなく `openapi.yaml` を修正する。

### 2. Maven ライフサイクルで生成する

OpenAPI Generator は `generate-sources` フェーズで実行する。

これにより、通常の Maven コマンドである `mvn test` や `mvn package` の前に生成処理が実行され、
CI でも追加手順なしで生成コードを利用できる。

### 3. 生成物は presentation 層の境界モデルとして扱う

生成された API インターフェースと API 入出力モデルは presentation 層の境界に限定して使用する。

application 層や domain 層へ生成モデルを持ち込まない。
application / domain へ渡す場合は mapper で変換する。

### 4. 生成物はコミットしない

生成物は `target/generated-sources/openapi` 配下に出力されるビルド成果物として扱う。

生成コードを直接修正せず、必要な変更は `openapi.yaml` または generator 設定に対して行う。

---

## 理由

### 1. OpenAPI と実装のずれを減らせる

OpenAPI 定義から API インターフェースと API 入出力モデルを生成することで、
仕様と実装の差分が発生しにくくなる。

特にリクエスト / レスポンス項目、必須項目、型の変更が Java 側に反映されやすくなる。

### 2. 手書き DTO の保守コストを下げられる

API DTO を手書きすると、OpenAPI 更新時に同じ変更を Java 側にも反映する必要がある。

生成モデルを使用することで、API 境界モデルの単純な同期作業を減らせる。

### 3. CI とローカル開発で同じ生成手順を使える

Maven Plugin として導入することで、ローカル開発、CI、テスト実行時に同じ生成手順を使用できる。

個別に CLI を実行する手順を追加しなくても、Maven ライフサイクルにより生成処理が実行される。

### 4. presentation 層の責務が明確になる

生成モデルを presentation 層の境界モデルとして扱い、domain model とは mapper で分離することで、
外部 API 仕様と内部ドメイン表現の境界を明確にできる。

---

## 代替案

### 1. API DTO と Controller シグネチャを手書きする

メリット

- 生成ツールの設定が不要
- Java コードを細かく制御しやすい

デメリット

- OpenAPI 定義と Java 実装の二重管理になる
- 仕様変更時の修正漏れが発生しやすい
- API 仕様を正本として運用しにくい

### 2. OpenAPI Generator CLI を手動実行する

メリット

- Maven 設定を増やさずに利用できる
- 必要なタイミングで明示的に生成できる

デメリット

- 実行漏れが発生しやすい
- ローカル環境と CI で手順が分かれやすい
- プロジェクト標準のビルド手順に組み込みにくい

### 3. 生成コードを `src/main/java` に出力してコミットする

メリット

- 生成後のコードをリポジトリ上で確認しやすい
- 生成環境がなくてもコードを参照できる

デメリット

- 生成物の差分が大きくなりやすい
- 手動編集が混ざるリスクがある
- 再生成時の差分管理が煩雑になる

---

## メリット

- OpenAPI 定義を API 仕様の正本として扱いやすい
- API 入出力モデルの二重管理を減らせる
- Maven の通常ライフサイクルで生成できる
- CI の追加手順を最小化できる
- presentation 層の外部境界を明確にできる

---

## デメリット

- OpenAPI Generator の設定を理解する必要がある
- 生成コードの構造が generator の仕様に依存する
- 生成モデルへ個別の Java 実装都合を直接入れられない
- Bean Validation のメッセージなど、生成コードに直接書けない設定は別途運用が必要になる

---

## 影響

### 実装への影響

- Controller は生成された API インターフェースを実装する
- API 入出力モデルは `presentation.generated.model` の生成モデルを使用する
- domain / application 層では生成モデルを直接使用しない
- 生成モデルと domain model の変換は presentation 層の mapper で行う

### ドキュメントへの影響

- API 仕様変更時は `docs/05_api/openapi.yaml` を先に更新する
- `docs/05_api` と README には、生成物を手動編集しない方針を記載する
- 生成モデルのバリデーションメッセージ運用は `ValidationMessages.properties` / `ValidationMessages_ja.properties` に合わせる

### CI への影響

- `mvn test` 実行時に `generate-sources` フェーズが実行される
- CI で OpenAPI Generator 用の追加ステップは必須ではない

---

## 結論

phone-order-api では OpenAPI Generator を採用する。

OpenAPI を API 仕様の正本とし、Maven の `generate-sources` フェーズで API インターフェースと
API 入出力モデルを生成することで、仕様と実装のずれを減らし、API 境界の保守性を高めるため。

---

## Supersedes

なし

---

## Superseded by

なし
