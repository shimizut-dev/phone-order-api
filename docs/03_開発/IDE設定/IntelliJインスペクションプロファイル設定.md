# IntelliJインスペクションプロファイル設定

プロジェクト共有用の IntelliJ Inspection Profile は、リポジトリ内の次の場所に配置する

- `config/intellij/phone_order_api_profile.xml`

`.idea` はコミット対象にしないため、配布用の profile XML はこの場所で管理する

---

## 配置方針

- リポジトリには配布用の profile XML を置く
- 各自の `.idea` 配下はコミットしない
- IntelliJ ではこの XML を import して使う

---

## 利用プラグイン

IntelliJ では次のプラグインを利用する。

- `SonarQube for IDE`
  - ローカルで解析結果を確認する
- `GitHub Copilot - Your AI Pair Programmer`
  - 実装補助に利用する
- `Mermaid`
  - Markdown 内の Mermaid 図を確認する

---

## IntelliJ への import 手順

1. `File > Settings > Editor > Inspections` を開く
2. Profile のメニューから `Import Profile...` を選ぶ
3. `config/intellij/phone_order_api_profile.xml` を指定する
4. import 後に、その profile を選択して適用する

---

## 運用ルール

- profile を更新した場合は、この XML を更新してコミットする
- `.idea/inspectionProfiles` は共有対象にしない
- generated code や framework 由来の inspection 運用は、この profile と `IntelliJ警告対応ガイド.md` で揃える
