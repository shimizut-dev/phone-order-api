package jp.co.shimizutdev.phoneorderapi.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.regex.Pattern;

/**
 * 注文コード
 */
@Getter
@ToString
@EqualsAndHashCode
public class OrderCode {

    /**
     * 注文コード形式不一致時の例外メッセージ。
     */
    private static final String INVALID_FORMAT_MESSAGE = "注文コード（ORD000001）の形式と不一致です。";

    /**
     * 注文コードパターン
     */
    private static final Pattern ORDER_CODE_PATTERN = Pattern.compile("^ORD\\d{6}$");

    /**
     * 値
     */
    private final String value;

    /**
     * コンストラクタ
     *
     * @param value 値
     */
    private OrderCode(final String value) {
        if (value == null || !ORDER_CODE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(INVALID_FORMAT_MESSAGE);
        }
        this.value = value;
    }

    /**
     * 注文コード(String)から注文コードを生成する
     *
     * @param value 注文コード(String)
     * @return 注文コード
     * @throws IllegalArgumentException 注文コードの形式が不正な場合
     */
    public static OrderCode of(final String value) {
        return new OrderCode(value);
    }

    /**
     * 注文コード文字列が有効形式か判定する
     *
     * @param value 注文コード文字列
     * @return 判定結果
     */
    public static boolean isValid(final String value) {
        return value != null && ORDER_CODE_PATTERN.matcher(value).matches();
    }
}
