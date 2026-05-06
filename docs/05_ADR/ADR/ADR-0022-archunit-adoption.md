# ADR-0022: ArchUnit を採用する

## ステータス

採用

## 日付

2026-05-06

---

## コンテキスト

phone-order-api では Clean Architecture と DDD を採用しており、
`presentation`、`application`、`domain`、`infrastructure` の依存方向を維持する必要がある。

しかし、レイヤ間依存の逸脱は通常の単体テストやコードレビューだけでは見落としやすい。
特に domain が外側レイヤやフレームワークへ依存する変更、
application が presentation や infrastructure の詳細へ直接依存する変更は、
設計崩れを徐々に進行させる要因になる。

このため、パッケージ依存ルールをテストとして継続検証する仕組みが必要である。

---

## 決定

ArchUnit を採用する。

`archunit-junit5` を test scope で導入し、
`src/test/java/jp/co/shimizutdev/phoneorderapi/ArchitectureTest.java` を正本として
アーキテクチャ依存ルールを検証する。

現時点では少なくとも以下をテストで保証する。

- `domain` が `application`、`presentation`、`infrastructure` に依存しない
- `domain` が `org.springframework..`、`jakarta.servlet..`、`jakarta.persistence..` に依存しない
- `application` が `presentation` と `infrastructure` の詳細に依存しない

---

## 理由

- 設計上の依存ルールをコードとして固定できる
- レビューの目視確認だけに頼らず、回帰をテストで検出できる
- Clean Architecture の前提を継続的に守りやすい
- JUnit 5 と統合しやすく、既存の Maven テスト実行に自然に組み込める

---

## 代替案

- レイヤ依存ルールをドキュメントだけで管理する
- コードレビュー時の確認だけで逸脱を防ぐ
- パッケージ構成の命名規約だけで設計維持を期待する

---

## メリット

- アーキテクチャ逸脱を早期に検出できる
- 設計方針と実装の整合を継続的に確認できる
- リファクタリング時の安全性を高められる

---

## デメリット

- 依存ルール変更時はテストの保守が必要になる
- ルールの粒度が粗いと、正当な依存まで禁止する可能性がある
- パッケージ構成変更時に追従コストが発生する

---

## Supersedes

なし

---

## Superseded by

なし
