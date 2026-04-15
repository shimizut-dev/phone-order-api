# IntelliJ で Spring Boot の local プロファイルを有効にする手順

## 目的

ローカル実行時に `application-local.properties` を読み込むため、
IntelliJ IDEA の実行/デバッグ構成で Spring Boot の **有効なプロファイル** に `local` を設定する。

---

## 対象

- ローカル環境で phone-order-api を起動する開発者
- IntelliJ IDEA から Spring Boot アプリを実行する場合

---

## 設定手順

### 1. 実行/デバッグ構成を開く

IntelliJ IDEA 上部メニューから以下を開く。

`実行` → `実行/デバッグ構成の編集...`

---

### 2. Spring Boot の実行構成を選択する

`phone-order-api` の Spring Boot 実行構成を選択する。

未作成の場合は、Spring Boot の実行構成を新規作成する。

---

### 3. 有効なプロファイルを設定する

**有効なプロファイル** に以下を設定する。

```text
local
```

---

### 4. 保存して起動する

設定を保存し、その実行構成でアプリを起動する。

---

## 補足

- `local` プロファイルを有効にすると、`application-local.properties` の設定が適用される。
- ローカル用の datasource 設定や SQL 詳細ログ設定は `local` プロファイルで利用する。
- `application.properties` には全環境共通の設定を置き、ローカル専用設定は `application-local.properties` に分離する。

---

## 運用ルール

- IntelliJ IDEA からローカル起動する場合は、Spring Boot の **有効なプロファイル** に `local` を設定する。
- `application.properties` に `spring.profiles.active=local` を固定で記載しない。
- 環境ごとの差分はプロファイルで切り替える。

---

## 配置候補

新規資料として以下に配置する想定。

`docs/09_development/intellij-local-profile-setup.md`
