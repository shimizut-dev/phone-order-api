package jp.co.shimizutdev.phoneorderapi.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;

/**
 * 注文日時
 */
@Getter
@ToString
@EqualsAndHashCode
public class OrderedAt {

    /**
     * 値
     */
    private final OffsetDateTime value;

    /**
     * コンストラクタ
     *
     * @param value 値
     */
    private OrderedAt(final OffsetDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("注文日時は必須です。");
        }
        this.value = value;
    }

    /**
     * 注文日時を生成する
     *
     * @param value 値
     * @return 注文日時
     */
    public static OrderedAt of(final OffsetDateTime value) {
        return new OrderedAt(value);
    }
}
