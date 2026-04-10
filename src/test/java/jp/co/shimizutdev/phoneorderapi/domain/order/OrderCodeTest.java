package jp.co.shimizutdev.phoneorderapi.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 注文コードテスト
 */
class OrderCodeTest {

    /**
     * <pre>
     * 正常な注文コードを生成できること。
     *
     * Given 正常な注文コードを用意する
     * When 注文コードを生成する
     * Then 注文コードが生成される
     * </pre>
     */
    @Test
    @DisplayName("正常な注文コードを生成できること")
    void shouldCreateOrderCodeWhenFormatIsValid() {
        OrderCode actual = OrderCode.of("ORD000001");

        assertEquals("ORD000001", actual.getValue());
    }

    /**
     * <pre>
     * 注文コードがnullの場合は例外を送出すること。
     *
     * Given nullの注文コードを用意する
     * When 注文コードを生成する
     * Then 形式不一致の例外を送出する
     * </pre>
     */
    @Test
    @DisplayName("注文コードがnullの場合は例外を送出すること")
    void shouldThrowExceptionWhenOrderCodeIsNull() {
        IllegalArgumentException actual =
            assertThrows(IllegalArgumentException.class, () -> OrderCode.of(null));

        assertEquals("注文コード（ORD000001）の形式と不一致です。", actual.getMessage());
    }

    /**
     * <pre>
     * 注文コードが空文字の場合は例外を送出すること。
     *
     * Given 空文字の注文コードを用意する
     * When 注文コードを生成する
     * Then 形式不一致の例外を送出する
     * </pre>
     */
    @Test
    @DisplayName("注文コードが空文字の場合は例外を送出すること")
    void shouldThrowExceptionWhenOrderCodeIsEmpty() {
        IllegalArgumentException actual =
            assertThrows(IllegalArgumentException.class, () -> OrderCode.of(""));

        assertEquals("注文コード（ORD000001）の形式と不一致です。", actual.getMessage());
    }

    /**
     * <pre>
     * 注文コードが空白のみの場合は例外を送出すること。
     *
     * Given 空白のみの注文コードを用意する
     * When 注文コードを生成する
     * Then 形式不一致の例外を送出する
     * </pre>
     */
    @Test
    @DisplayName("注文コードが空白のみの場合は例外を送出すること")
    void shouldThrowExceptionWhenOrderCodeIsBlank() {
        IllegalArgumentException actual =
            assertThrows(IllegalArgumentException.class, () -> OrderCode.of("   "));

        assertEquals("注文コード（ORD000001）の形式と不一致です。", actual.getMessage());
    }

    /**
     * <pre>
     * 注文コードのプレフィックスが不正な場合は例外を送出すること。
     *
     * Given プレフィックスが不正な注文コードを用意する
     * When 注文コードを生成する
     * Then 形式不一致の例外を送出する
     * </pre>
     */
    @Test
    @DisplayName("注文コードのプレフィックスが不正な場合は例外を送出すること")
    void shouldThrowExceptionWhenOrderCodePrefixIsInvalid() {
        IllegalArgumentException actual =
            assertThrows(IllegalArgumentException.class, () -> OrderCode.of("ABC000001"));

        assertEquals("注文コード（ORD000001）の形式と不一致です。", actual.getMessage());
    }

    /**
     * <pre>
     * 注文コードの桁数が不正な場合は例外を送出すること。
     *
     * Given 桁数が不正な注文コードを用意する
     * When 注文コードを生成する
     * Then 形式不一致の例外を送出する
     * </pre>
     */
    @Test
    @DisplayName("注文コードの桁数が不正な場合は例外を送出すること")
    void shouldThrowExceptionWhenOrderCodeLengthIsInvalid() {
        IllegalArgumentException actual =
            assertThrows(IllegalArgumentException.class, () -> OrderCode.of("ORD00001"));

        assertEquals("注文コード（ORD000001）の形式と不一致です。", actual.getMessage());
    }
}
