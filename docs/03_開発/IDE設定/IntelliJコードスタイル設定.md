# IntelliJ Code Style 設定手順

プロジェクト共有用の IntelliJ Code Style は、リポジトリ内の次の場所に配置する

- `config/intellij/intellij-java-google-style.xml`

`.idea` はコミット対象にしないため、配布用の Code Style XML はこの場所で管理する

---

## 配置方針

- リポジトリには配布用の Code Style XML を置く
- 各自の `.idea` 配下はコミットしない
- IntelliJ ではこの XML を import して使う

---

## IntelliJ への import 手順

1. `File > Settings > Editor > Code Style` を開く
2. 右上の歯車メニューを開く
3. `Import Scheme > IntelliJ IDEA code style XML` を選ぶ
4. `config/intellij/intellij-java-google-style.xml` を指定する
5. import 後に適用されていることを確認する

---

## 運用ルール

- Code Style を更新した場合は、この XML を更新してコミットする
- `.idea/codeStyles` は共有対象にしない
- Java の最終的な整形結果は Spotless を正本とする
