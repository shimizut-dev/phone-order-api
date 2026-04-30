package jp.co.shimizutdev.phoneorderapi.domain.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** ページング条件テスト */
class PagingConditionTest {

  /**
   *
   *
   * <pre>
   * Given ページ番号とページサイズを用意する
   * When ページング条件を生成する
   * Then 指定したページ番号とページサイズが保持される
   * </pre>
   */
  @Test
  @DisplayName("ページング条件を生成できること")
  void shouldCreatePagingCondition() {
    PagingCondition actual = PagingCondition.of(1, 50);

    assertEquals(1, actual.page());
    assertEquals(50, actual.size());
  }

  /**
   *
   *
   * <pre>
   * Given null のページ番号とページサイズを用意する
   * When 未指定値を補完してページング条件を生成する
   * Then デフォルトページ番号とデフォルトページサイズが設定される
   * </pre>
   */
  @Test
  @DisplayName("ページ番号とページサイズがnullの場合はデフォルト値で補完されること")
  void shouldUseDefaultValuesWhenPageAndSizeAreNull() {
    PagingCondition actual = PagingCondition.ofNullable(null, null);

    assertEquals(0, actual.page());
    assertEquals(20, actual.size());
  }

  /**
   *
   *
   * <pre>
   * Given null のページ番号とページサイズ指定を用意する
   * When 未指定値を補完してページング条件を生成する
   * Then ページ番号だけデフォルト値で補完される
   * </pre>
   */
  @Test
  @DisplayName("ページ番号がnullの場合はデフォルトページ番号で補完されること")
  void shouldUseDefaultPageWhenPageIsNull() {
    PagingCondition actual = PagingCondition.ofNullable(null, 50);

    assertEquals(0, actual.page());
    assertEquals(50, actual.size());
  }

  /**
   *
   *
   * <pre>
   * Given ページ番号指定と null のページサイズを用意する
   * When 未指定値を補完してページング条件を生成する
   * Then ページサイズだけデフォルト値で補完される
   * </pre>
   */
  @Test
  @DisplayName("ページサイズがnullの場合はデフォルトページサイズで補完されること")
  void shouldUseDefaultSizeWhenSizeIsNull() {
    PagingCondition actual = PagingCondition.ofNullable(1, null);

    assertEquals(1, actual.page());
    assertEquals(20, actual.size());
  }

  /**
   *
   *
   * <pre>
   * Given 負のページ番号を用意する
   * When ページング条件を生成する
   * Then ページ番号不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("ページ番号が負の場合は例外になること")
  void shouldThrowExceptionWhenPageIsNegative() {
    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> PagingCondition.of(-1, 20));

    assertEquals("ページ番号は0以上である必要があります。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given 0 のページサイズを用意する
   * When ページング条件を生成する
   * Then ページサイズ下限不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("ページサイズが1未満の場合は例外になること")
  void shouldThrowExceptionWhenSizeIsLessThanOne() {
    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> PagingCondition.of(0, 0));

    assertEquals("ページサイズは1以上である必要があります。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given 最大値を超えるページサイズを用意する
   * When ページング条件を生成する
   * Then ページサイズ上限不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("ページサイズが最大値を超える場合は例外になること")
  void shouldThrowExceptionWhenSizeExceedsMaxSize() {
    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> PagingCondition.of(0, 101));

    assertEquals("ページサイズは100以下である必要があります。", actual.getMessage());
  }
}
