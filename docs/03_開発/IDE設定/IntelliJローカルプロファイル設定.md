# IntelliJ で Spring Boot の local プロファイルを有効にする手順

## 目的

`application-local.properties` を利用して、IntelliJ IDEA から `local` プロファイルでアプリを起動する。

---

## 前提

- ローカル環境で `phone-order-api` を開いていること
- IntelliJ IDEA から Spring Boot アプリを実行できること
- IntelliJ プラグイン「`.env files`」をインストール済みであること

---

## 設定手順

### 1. Run/Debug Configurations を開く

IntelliJ IDEA 上部メニューから次を開く。

`実行 > 実行/デバッグ構成の編集...`

---

### 2. Spring Boot の実行構成を選択する

`phone-order-api` の Spring Boot 実行構成を選択する。  
存在しない場合は Spring Boot 構成を新規作成する。

---

### 3. 有効なプロファイルに `local` を設定する

`Active profiles` に次を設定する。

```text
local
```

---

### 4. 「.env files」で `.env` を読み込む

`application-local.properties` では datasource と Basic 認証の値を環境変数から読み込む。  
環境変数は直接入力ではなく、`.env` から読み込む運用とする。

設定方法:

1. 実行構成の `.env files` タブを開く
2. `Enable` を有効化する
3. プロジェクトルートの `.env` を追加する

`.env` に必要なキーは `.env.example` を基準にする。

---

### 5. 保存して起動する

設定を保存し、同じ実行構成でアプリを起動する。

---

## 実行時の注意

- `local` プロファイルを有効にしないと `application-local.properties` は適用されない
- `.env` 未設定のまま起動すると、必須環境変数不足で起動失敗する
- `application.properties` に `spring.profiles.active=local` は書かない

---

## 関連資料

- [IntelliJ文字コード設定](./IntelliJ文字コード設定.md)
- [IntelliJ Maven文字化け対策](./IntelliJ%20Maven文字化け対策.md)
- [ローカル設定](../開発環境/ローカル設定.md)
