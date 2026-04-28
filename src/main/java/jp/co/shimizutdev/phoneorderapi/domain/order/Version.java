package jp.co.shimizutdev.phoneorderapi.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * バージョン
 */
@Getter
@ToString
@EqualsAndHashCode
public class Version {

    /**
     * バージョン値不正時の例外メッセージ。
     */
    private static final String INVALID_VALUE_MESSAGE = "バージョンは0以上である必要があります。";

    /**
     * 値
     */
    private final long value;

    /**
     * コンストラクタ
     *
     * @param value 値
     */
    private Version(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException(INVALID_VALUE_MESSAGE);
        }

        this.value = value;
    }

    /**
     * バージョン(long)からバージョンを生成する
     *
     * @param value バージョン(long)
     * @return バージョン
     * @throws IllegalArgumentException バージョンが0未満の場合
     */
    public static Version of(final long value) {
        return new Version(value);
    }
}
