# IntelliJ 文字コード設定

## 概要

IntelliJ で文字化けを防ぎ、リポジトリ方針と一致した保存設定を維持するための手順を定義する。

---

## 対象

- IntelliJ でファイルを新規作成または編集する場合
- 環境差による文字化けを防ぎたい場合
- `.editorconfig` に合わせて保存設定を統一したい場合

---

## 設定手順

### 1. File Encodings を開く

以下を開く

```text
File > Settings > Editor > File Encodings
```

### 2. 文字コードを UTF-8 に設定する

以下を設定する

```text
Global Encoding: UTF-8
Project Encoding: UTF-8
Default encoding for properties files: UTF-8
Transparent native-to-ascii conversion: OFF
```

### 3. `.editorconfig` と整合することを確認する

- テキストファイルは UTF-8 を使用する
- UTF-8 BOM は使用しない
- 保存時に `.editorconfig` の設定が優先されることを確認する

---

## 補足

- 文字コード運用の正本は `.editorconfig` とする
- Maven ツールウィンドウ実行時の文字化けは `IntelliJ Maven文字化け対策.md` を参照する
