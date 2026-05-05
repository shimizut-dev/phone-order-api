# JPA Entity / Mapper ルール

## 目的

JPA Entity と domain object の責務を分離し、永続化まわりの実装判断を統一する。

---

## 基本方針

- domain object と JPA entity は別物として扱う
- domain は JPA に依存しない
- JPA entity への変換と逆変換は mapper に集約する

---

## JPA Entity のルール

- JPA entity は `infrastructure.persistence` 配下に置く
- クラス名は `XxxJpaEntity` とする
- DB カラムとの対応を表す責務に限定する
- domain logic は持たせない
- 値検証や業務ルールは domain 側に置く

---

## Mapper のルール

- domain object と JPA entity の相互変換は mapper に集約する
- クラス名は `XxxJpaMapper` とする
- `toEntity` と `toDomain` を基本の入口にする
- 変換時に永続化データの異常を検知したら、mapper で例外に変換する

例:

```java
public static OrderJpaEntity toEntity(final Order order) {
    // ...
}

public static Order toDomain(final OrderJpaEntity orderJpaEntity) {
    // ...
}
```

---

## 依存方向のルール

- domain は JPA entity を参照しない
- application も原則として JPA entity を直接扱わない
- repository 実装だけが mapper と JPA repository を使ってよい

---

## Entity の生成ルール

- 本番コードでは JPA entity を mapper 経由で組み立てる
- domain object から永続化表現へ落とす処理は `toEntity` に寄せる
- 別パッケージから `new XxxJpaEntity()` を前提にしない

---

## テストコードのルール

- 別パッケージのテストで JPA entity の constructor 可視性に依存しない
- 可能なら mapper を使って entity を組み立てる
- どうしても直接生成が必要な場合は、テスト配置か専用 factory を見直す

---

## Repository 実装のルール

- domain repository の interface は domain に置く
- JPA repository は infrastructure に置く
- domain repository 実装は `XxxRepositoryImpl` とする
- `RepositoryImpl` では JPA repository と mapper を組み合わせて domain を返す

---

## このプロジェクトでの推奨

- `OrderRepositoryImpl` が `OrderJpaRepository` と `OrderJpaMapper` を使う構成を踏襲する
- domain object の生成は domain 側 factory method で行う
- JPA entity は永続化の都合だけを表現する

