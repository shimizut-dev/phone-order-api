# document-format-guideline

開発ドキュメントの記載とファイル形式の運用ルールを定義する

---

## 概要

- ドキュメントの書き方を揃える
- 開発者ごとの差異が出やすいファイル形式を固定する
- README と `docs/README.md` を入口として維持する

---

## 見出しルール

- タイトルは `#` を 1 つだけ使用する
- 大見出しは `##`、必要に応じて `###` を使用する
- 見出しの粒度は揃える
- 関連情報は近い位置にまとめる

---

## ファイル命名

- README は `README.md`
- ルール文書は `xxx-rules.md`
- 概要文書は `xxx-overview.md`
- ガイドや手順書は `xxx-guideline.md` または `xxx-setup.md`
- ADR は `ADR-0000-xxx.md`

---

## ファイル形式

- 文字コードは UTF-8 とする
- 改行コードは Windows 開発環境でも `LF` を正本とする
- `*.cmd` / `*.bat` は Windows 実行互換性のため `CRLF` とする
- `mvnw` は Git Bash / CI 互換性のため `LF` とする
- インデントはスペースを使用し、XML / YAML は 2 スペースを基本とする
- Markdown は hard line break を使う場合があるため、末尾空白の自動削除はしない
- 共通の整形ルールは `.editorconfig` を正本とする
- 改行方針は `.gitattributes` と `.editorconfig` の両方で管理する
- PowerShell で日本語ファイルを確認する場合は `-Encoding UTF8` を明示する
- Java ソースと `properties` は UTF-8 no BOM とする

---

## 更新ルール

- 実装や運用変更に関連するドキュメントは同じ変更内で更新する
- ルート README と `docs/README.md` は入口として常に最新に揃える
- 開発手順に関わる内容は `docs/09_development/` に集約する
