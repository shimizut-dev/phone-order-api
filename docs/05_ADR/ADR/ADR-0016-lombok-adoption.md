# ADR-0016: Lombok を採用する

## ステータス

採用

## 日付

2026-05-06

---

## コンテキスト

phone-order-api では、Spring Bean の constructor injection、JPA Entity の accessor、
logger 定義などで定型コードが増えやすい。

一方で、domain model や JPA Entity では自動生成メソッドの使い方を誤ると、
設計意図が不明瞭になったり、等価性や遅延ロードに関する問題を招く可能性がある。

このため、ボイラープレート削減と設計の明示性を両立するための方針が必要である。

---

## 決定

Lombok を採用する。

ただし、使用は `docs/03_開発/コーディング規約/Lombok利用ルール.md` に従い、
用途を限定して運用する。

主に以下を許可する。

- Spring Bean での `@RequiredArgsConstructor`
- JPA Entity での `@Getter`、`@Setter`、`@NoArgsConstructor`
- 必要最小限の `@Getter`、`@ToString`、`@EqualsAndHashCode`
- logger 定義のための `@Slf4j`

`@Data` と `@AllArgsConstructor` は原則として採用しない。

---

## 理由

- constructor injection や accessor の定型コードを減らせる
- 設計意図を保ったまま記述量を抑えられる
- `maven-compiler-plugin` の annotation processor 設定でビルドに統合済みである
- 使用ルールを文書化することで、Lombok の過剰利用を抑制できる

---

## 代替案

- Lombok を使わず、constructor、getter、setter、logger をすべて手書きする
- `@Data` や `@Value` を広く使って記述量をさらに減らす

---

## メリット

- Spring Bean や JPA Entity の記述が簡潔になる
- constructor injection を統一しやすい
- 定型コードの削減により、レビュー対象を本質的な実装へ寄せられる

---

## デメリット

- IDE や annotation processor の設定前提が増える
- 自動生成されるメソッドが見えにくく、慣れていないメンバーには追いにくい
- 不適切なアノテーション選択で設計を崩す余地がある

---

## Supersedes

なし

---

## Superseded by

なし
