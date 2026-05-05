# IntelliJ でワイルドカード import を使わない設定

## 概要

IntelliJ で `*` 形式の import を使わず、個別 import を維持するための設定手順を定義する

---

## 対象

- IntelliJ で `Optimize Imports` を実行する場合
- 保存時や整形時にワイルドカード import が入るのを防ぎたい場合
- Checkstyle の `AvoidStarImport` と整合させたい場合

---

## 設定手順

### 1. Java の Imports 設定を開く

以下を開く

```text
File > Settings > Editor > Code Style > Java > Imports
```

### 2. ワイルドカード import を使わない値に変更する

以下を設定する

```text
Class count to use import with '*': 999
Names count to use static import with '*': 999
Use single class import: ON
```

### 3. 既存コードに反映する

必要に応じて以下を実行する

```text
Code > Optimize Imports
```

---

## 補足

- 本プロジェクトは Checkstyle で `AvoidStarImport` を有効にしている
- Java 整形の正本は Spotless だが、IntelliJ 側でも import 設定を揃えておく
