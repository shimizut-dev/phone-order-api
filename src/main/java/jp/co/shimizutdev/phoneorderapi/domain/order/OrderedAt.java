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
     * 注文日時未指定時の例外メッセージ。
     */
    private static final String REQUIRED_MESSAGE = "注文日時は必須です。";

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
            throw new IllegalArgumentException(REQUIRED_MESSAGE);
        }
        this.value = value;
    }

    /**
     * 注文日時(OffsetDateTime)から注文日時を生成する
     *
     * @param value 注文日時(OffsetDateTime)
     * @return 注文日時
     * @throws IllegalArgumentException 注文日時が指定されていない場合
     */
    public static OrderedAt of(final OffsetDateTime value) {
        return new OrderedAt(value);
    }
}
