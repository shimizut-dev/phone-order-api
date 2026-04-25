package jp.co.shimizutdev.phoneorderapi.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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
     * 不正な形式の注文コードを生成しようとすると例外が発生すること。
     *
     * Given 不正な形式の注文コードを用意する
     * When 注文コードを生成する
     * Then 例外が発生する
     * </pre>
     */
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"   ", "ABC000001", "ORD00001"})
    @DisplayName("不正な形式の注文コードを生成しようとすると例外が発生すること")
    void shouldThrowExceptionWhenOrderCodeFormatIsInvalid(final String value) {
        String expectedMessage =
            assertThrows(IllegalArgumentException.class, () -> OrderCode.of("invalid")).getMessage();

        IllegalArgumentException actual =
            assertThrows(IllegalArgumentException.class, () -> OrderCode.of(value));

        assertEquals(expectedMessage, actual.getMessage());
    }
}
