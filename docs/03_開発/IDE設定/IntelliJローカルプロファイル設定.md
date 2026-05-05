# IntelliJ で Spring Boot の local プロファイルを有効にする手順

## 概要

ローカル動作用に `application-local.properties` を読み込むため、
IntelliJ IDEA の実行/デバッグ構成で Spring Boot の有効なプロファイルに `local` を設定する。

---

## 前提

- ローカル環境で `phone-order-api` を起動できること
- IntelliJ IDEA から Spring Boot アプリを実行すること

---

## 設定手順

### 1. 実行/デバッグ構成を開く

IntelliJ IDEA 上部メニューから次を開く。

`実行 > 実行/デバッグ構成の編集...`

---

### 2. Spring Boot の実行構成を選択する

`phone-order-api` の Spring Boot 実行構成を選択する。

未作成の場合は、新しく Spring Boot の実行構成を作成する。

---

### 3. 有効なプロファイルを設定する

**有効なプロファイル** に次を設定する。

```text
local
```

---

### 4. 環境変数を設定する

`application-local.properties` では datasource の認証情報を環境変数から読み込む。
**環境変数** に次を設定する。

```text
PHONE_ORDER_DB_USERNAME=local;PHONE_ORDER_DB_PASSWORD=local
```

値は `docker/docker-compose.yml` で起動するローカル PostgreSQL の設定に合わせる。

---

### 5. 保存して起動する

設定を保存し、その実行構成でアプリを起動する。

---

## 実行時の注意

- `local` プロファイルを有効にすると、`application-local.properties` の設定が適用される
- ローカル用 datasource 設定、Flyway の `baseline-on-migrate`、SQL 詳細ログ設定は `local` プロファイルで利用する
- ローカル用 datasource の認証情報は `PHONE_ORDER_DB_USERNAME` / `PHONE_ORDER_DB_PASSWORD` で渡す
- `application.properties` には共通設定を置き、ローカル環境設定は `application-local.properties` に分離する

---

## 文字化け対策

JUnit や Spring Boot の実行ログだけ文字化けする場合は、実行構成の Java 設定を確認する。

### 確認箇所

`実行 > 実行/デバッグ構成の編集...` を開き、対象の JUnit または Spring Boot 実行構成で
`Java` セクションの **設定から継承** を確認する。

### 対応方針

- `git bash` で `./mvnw clean spotless:check test` を実行して文字化けしない場合、ソースや Maven ビルド設定ではなく IntelliJ 実行構成側の問題である可能性が高い
- `設定から継承` のチェックを外すと解消する場合がある
- 必要に応じて VM options に `-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8` を追加する

---

## よくあるルール

- IntelliJ IDEA からローカル起動する場合は、Spring Boot の **有効なプロファイル** に `local` を設定する
- `application.properties` に `spring.profiles.active=local` を直接書かない
- 環境ごとの切り替えはプロファイルで行う

---

## 関連資料

- [IntelliJ生成コード設定](./IntelliJ生成コード設定.md)
- [IntelliJ Maven文字化け対策](./IntelliJ%20Maven文字化け対策.md)
