# IntelliJ 警告運用ガイド

IntelliJ の警告は開発時の補助として使い、品質の合否判定は Maven と CI を正本とする

---

## 基本方針

- IntelliJ の警告をゼロにすること自体は目的にしない
- コミット可否、マージ可否は `./mvnw spotless:check test` と GitHub Actions の結果で判断する
- IntelliJ の警告は、実装中に早く気づくための補助情報として扱う

---

## 正本とするチェック

本プロジェクトでは次を正式な品質チェックとする

- `./mvnw spotless:check test`
- GitHub Actions の CI
- Spotless
- Maven Enforcer
- Checkstyle
- SpotBugs
- ArchUnit

IntelliJ 上の警告は、これらを補助するものとして扱う

---

## 警告の分類

IntelliJ の警告は次の 3 種類に分けて扱う

### 1. 修正する警告

- プロダクトコード上の未使用コード
- null 安全性に関わる警告
- import の問題
- 到達不能コード
- チームのルールに反する警告

### 2. 抑止または設定で解決する警告

- Lombok 由来の警告
- OpenAPI generated sources の警告
- フレームワーク都合で発生する警告
- XML / DTD / スキーマ参照など IDE 固有の警告

### 3. 運用対象外とする警告

- 本プロジェクトで品質基準にしていない inspection
- generated code に対する再生成で戻る警告

---

## Lombok の扱い

Lombok 由来の警告が出る場合は、まず IntelliJ の設定を確認する

### 必要な設定

- Lombok plugin をインストールする
- `Settings > Build, Execution, Deployment > Compiler > Annotation Processors`
  - `Enable annotation processing` を有効にする

Lombok の警告は、コード修正より先に IDE 設定で解決できるかを確認する

---

## generated sources の扱い

`target/generated-sources/openapi` 配下は生成コードであり、手修正を前提にしない

### 方針

- generated code の警告は原則として品質評価の中心にしない
- 必要な warning 抑止は生成元設定で行う
- IntelliJ 上の unresolved symbol は Maven 再読込や `generate-sources` で解決する

詳細は `intellij-generated-sources-setup.md` を参照する

---

## 未使用の宣言の扱い

`Unused declaration` inspection は無効化しない

理由:

- プロダクトコードでは実際に不要なコードを見つけられる
- Spring / JPA / JUnit / ArchUnit / Lombok / generated code の影響で false positive も出る
- ただし、決定的な代替手段がないため inspection 自体は残す

運用:

- 警告はノイズとして一定量出る前提で扱う
- generated code は必要に応じて inspection scope から外す
- 本当に邪魔な false positive だけ最小限で suppress する
- 合否判断は `./mvnw spotless:check test` と CI を正本にする

---

## 抑止の考え方

警告を抑止する場合は、次の順で検討する

1. IntelliJ の設定で解決できるか
2. Maven / Generator / plugin 設定で解決できるか
3. generated code なら生成元で吸収できるか
4. それでも必要な場合のみ `@SuppressWarnings` や `//noinspection` を使う

抑止は、理由を説明できる場合のみ行う

---

## 日常運用

### 開発中

- IntelliJ の警告で早めに異常を見つける
- ただし、generated code や Lombok 警告に過剰反応しない

### コミット前

- `./mvnw spotless:check test` を実行する
- IntelliJ の Commit Checks では、Inspection Profile を使う `コードを解析` は有効のままにする
- IntelliJ の Commit Checks では、Cleanup は有効にしない
- 自動整形や自動修正は IntelliJ のコミット時処理ではなく、Spotless と Maven のチェックに寄せる

### レビュー時

- IntelliJ の個別警告より、CI と Maven チェック結果を優先して確認する

---

## 判断基準

- CI で落ちるものは必ず修正する
- プロダクトコードの警告は優先して直す
- generated code は原則手修正しない
- IDE だけの警告は、設定で吸収できるかを先に確認する
