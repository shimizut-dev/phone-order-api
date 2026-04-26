package jp.co.shimizutdev.phoneorderapi.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

/**
 * 注文ID
 */
@Getter
@ToString
@EqualsAndHashCode
public class OrderId {

    /**
     * 値
     */
    private final UUID value;

    /**
     * コンストラクタ
     *
     * @param value 値
     */
    private OrderId(final UUID value) {
        this.value = Objects.requireNonNull(value, "注文IDは必須です。");
    }

    /**
     * 注文IDを採番する
     *
     * @return 注文ID
     */
    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }

    /**
     * 注文ID(UUID)から注文IDを生成する
     *
     * @param value 注文ID(UUID)
     * @return 注文ID
     */
    public static OrderId of(final UUID value) {
        return new OrderId(value);
    }
}
