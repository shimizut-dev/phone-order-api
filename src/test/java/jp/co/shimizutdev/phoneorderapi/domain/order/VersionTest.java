package jp.co.shimizutdev.phoneorderapi.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * バージョンテスト
 */
class VersionTest {

    /**
     * <pre>
     * 0以上のバージョンを生成できること。
     *
     * Given 0以上のバージョン値を用意する
     * When バージョンを生成する
     * Then バージョンが生成される
     * </pre>
     */
    @Test
    @DisplayName("0以上のバージョンを生成できること")
    void shouldCreateVersionWhenValueIsZeroOrGreater() {
        Version actual = Version.of(1L);

        assertEquals(1L, actual.getValue());
    }

    /**
     * <pre>
     * 負のバージョン値を生成しようとすると例外が発生すること。
     *
     * Given 負のバージョン値を用意する
     * When バージョンを生成する
     * Then 例外が発生する
     * </pre>
     */
    @ParameterizedTest
    @ValueSource(longs = {-1L, -10L})
    @DisplayName("負のバージョン値は例外になる")
    void shouldThrowExceptionWhenVersionIsNegative(final long value) {
        IllegalArgumentException actual =
            assertThrows(IllegalArgumentException.class, () -> Version.of(value));

        assertEquals("バージョンは0以上である必要があります。", actual.getMessage());
    }
}
