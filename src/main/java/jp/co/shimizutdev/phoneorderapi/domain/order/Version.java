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
            throw new IllegalArgumentException("バージョンは0以上である必要があります。");
        }

        this.value = value;
    }

    /**
     * バージョン(long)からバージョンを生成する
     *
     * @param value バージョン(long)
     * @return バージョン
     */
    public static Version of(final long value) {
        return new Version(value);
    }
}
