# Lombok 使用ルール

## 目的

Lombok の使用方針を統一し、可読性と設計意図を崩さずにボイラープレートを減らす。

---

## 基本方針

- Lombok は記述量を減らすために使う
- 設計意図が読みにくくなる使い方は避ける
- Spring / JPA / domain model の都合を優先する

---

## 使用してよい主なアノテーション

- `@RequiredArgsConstructor`
- `@Getter`
- `@Setter`
- `@NoArgsConstructor`
- `@EqualsAndHashCode`
- `@ToString`
- `@Slf4j`

---

## 使用ルール

### Spring Bean

- constructor injection を基本とする
- 依存フィールドは `private final` にする
- コンストラクタは `@RequiredArgsConstructor` で生成する

例:

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCodeGenerator orderCodeGenerator;
}
```

---

### Domain Object / Value Object

- `@Getter`, `@ToString`, `@EqualsAndHashCode` を必要に応じて使う
- 生成方法やバリデーションは明示的にコードで書く
- `@Data` は使わない
- `@Value` は自動生成される constructor や運用方針と衝突しやすいため、このプロジェクトでは基本にしない

---

### JPA Entity

- `@Getter`, `@Setter`, `@NoArgsConstructor` を使ってよい
- `@NoArgsConstructor` は `access = AccessLevel.PROTECTED` を基本とする
- `@Data` は使わない
- `@EqualsAndHashCode` は使わない
- `@ToString` は遅延ロードや循環参照の問題を招きうるため原則使わない

例:

```java
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderJpaEntity {
}
```

---

### Logging

- logger は `@Slf4j` を使用してよい
- logger 名を明示したい特別な理由がなければ手書きしない

---

## 使用しない方がよいアノテーション

### `@Data`

- getter / setter / equals / hashCode / toString を一括生成するため影響範囲が広い
- domain object や JPA entity では不要なメソッドまで増えやすい
- 設計意図がぼやけるため、このプロジェクトでは使わない

### `@AllArgsConstructor`

- 生成ルールを曖昧にしやすい
- factory method や `@RequiredArgsConstructor` の方針と競合しやすい
- 特別な理由がない限り使わない

---

## このプロジェクトでの推奨

- Spring Bean には `@RequiredArgsConstructor`
- domain / value object には必要最小限の Lombok
- JPA entity には `@Getter`, `@Setter`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
- `@Data` は使わない

