# Java アノテーション並び順ルール

## 目的

クラス宣言まわりのアノテーション順を統一し、可読性とレビュー基準を揃える。

---

## 基本方針

- アルファベット順ではなく、意味の強さで並べる
- 先に「そのクラスの役割を決めるもの」を置く
- 次に「振る舞い・補助設定を決めるもの」を置く
- Lombok は最後にまとめる

基本順:

1. 役割を決めるアノテーション
2. 振る舞い・補助設定のアノテーション
3. Lombok のアノテーション

---

## 役割を決めるアノテーション

例:

- `@RestController`
- `@Service`
- `@Repository`
- `@Component`
- `@Configuration`
- `@Entity`
- `@MappedSuperclass`
- `@RestControllerAdvice`
- `@SpringBootTest`
- `@DataJpaTest`

---

## 振る舞い・補助設定のアノテーション

例:

- `@Transactional`
- `@Order`
- `@Table`
- `@EntityListeners`
- `@EnableJpaAuditing`
- `@Import`
- `@AutoConfigureMockMvc`
- `@AutoConfigureTestDatabase`
- `@SqlMergeMode`
- `@Sql`
- `@ExtendWith`

---

## Lombok のアノテーション

例:

- `@RequiredArgsConstructor`
- `@Getter`
- `@Setter`
- `@NoArgsConstructor`
- `@EqualsAndHashCode`
- `@ToString`
- `@Slf4j`

---

## 適用例

### Spring Service

```java

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
}
```

### JPA Entity

```java

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderJpaEntity {
}
```

### JPA MappedSuperclass

```java

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseAuditJpaEntity {
}
```

### Filter / Component

```java

@Component
@Order(2)
@Slf4j
@RequiredArgsConstructor
public class RequestResponseLogFilter {
}
```

### Test Class

```java

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class RequestResponseLogFilterTest {
}
```

---

## 補足

- 同じ意味の層の中では、関連の強いものを近くに置く
- 迷った場合は「このクラスが何者かを先に読める順」を優先する
- 既存コードを修正するときは、このルールに合わせて並び順も揃える
- メソッドやフィールドのアノテーション順は、必要になった時点で別途ルール化する

