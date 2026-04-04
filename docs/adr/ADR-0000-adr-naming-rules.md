# ADR-0000: ADR 命名ルール

## ステータス
採用

## 日付
2026-04-04

---

## コンテキスト

phone-order-api プロジェクトでは、
Architecture Decision Record（ADR）を管理する。

ADR のファイル名が統一されていないと、
以下の問題が発生する。

- 可読性の低下
- 一覧性の低下
- ルールの不統一
- プロジェクトの品質低下

そのため、ADR の命名ルールを定義する。

---

## 決定

ADR のファイル名は以下の形式で統一する。

```
ADR-XXXX-<decision>-adoption.md
```

例

```
ADR-0001-java21-adoption.md
ADR-0002-spring-boot-3-adoption.md
ADR-0003-postgresql-adoption.md
ADR-0004-flyway-adoption.md
ADR-0005-testcontainers-adoption.md
```

---

## 命名ルール

### 1. 連番

- 4桁の連番とする
- 0001 から開始する

例

```
ADR-0001
ADR-0002
ADR-0003
```

---

### 2. 英語小文字・ハイフン区切り

単語は以下の形式とする

- 小文字
- ハイフン区切り

例

```
spring-boot
clean-architecture
testcontainers
```

---

### 3. adoption を付与する

ADR は「採用判断」を記録するため、
原則として `adoption` を付与する。

例

```
java21-adoption
flyway-adoption
uuid-adoption
```

---

### 4. 例外ルール

以下の場合は `adoption` を省略可能

- ガイドライン
- 命名ルール
- 設計方針

例

```
ADR-0000-adr-naming-rules.md
```

ただし、可能な限り adoption を使用する。

---

## 目的

このルールにより

- ADR 一覧性向上
- 可読性向上
- 設計品質向上

を実現する。

---

## 結論

phone-order-api では
ADR ファイル名を以下の形式で統一する。

```
ADR-XXXX-<decision>-adoption.md
```
