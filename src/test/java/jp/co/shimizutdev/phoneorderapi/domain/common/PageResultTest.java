package jp.co.shimizutdev.phoneorderapi.domain.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** ページング済み検索結果テスト */
class PageResultTest {

  /**
   *
   *
   * <pre>
   * Given ページング済み検索結果の全項目を用意する
   * When ページング済み検索結果を生成する
   * Then 全項目とページ有無が保持される
   * </pre>
   */
  @Test
  @DisplayName("ページング済み検索結果を生成できること")
  void shouldCreatePageResult() {
    PageResult<String> actual = new PageResult<>(List.of("ORD000003", "ORD000002"), 0, 2, 3L, 2);

    assertEquals(2, actual.items().size());
    assertEquals("ORD000003", actual.items().get(0));
    assertEquals("ORD000002", actual.items().get(1));
    assertEquals(0, actual.page());
    assertEquals(2, actual.size());
    assertEquals(3L, actual.totalElements());
    assertEquals(2, actual.totalPages());
    assertTrue(actual.hasNext());
    assertFalse(actual.hasPrevious());
  }

  /**
   *
   *
   * <pre>
   * Given 生成後に変更される要素一覧を用意する
   * When ページング済み検索結果を生成する
   * Then 要素一覧は生成時点の内容から変更されない
   * </pre>
   */
  @Test
  @DisplayName("要素一覧が防御的コピーされること")
  void shouldDefensivelyCopyItems() {
    List<String> items = new ArrayList<>();
    items.add("ORD000001");

    PageResult<String> actual = new PageResult<>(items, 0, 1, 1L, 1);
    items.add("ORD000002");

    assertEquals(1, actual.items().size());
    assertEquals("ORD000001", actual.items().getFirst());
  }

  /**
   *
   *
   * <pre>
   * Given null の要素一覧を用意する
   * When ページング済み検索結果を生成する
   * Then 要素一覧必須例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("要素一覧がnullの場合は例外になること")
  void shouldThrowExceptionWhenItemsIsNull() {
    NullPointerException actual =
        assertThrows(NullPointerException.class, () -> new PageResult<>(null, 0, 20, 0L, 0));

    assertEquals("要素一覧は必須です。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given 負のページ番号を用意する
   * When ページング済み検索結果を生成する
   * Then ページ番号不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("ページ番号が負の場合は例外になること")
  void shouldThrowExceptionWhenPageIsNegative() {
    List<String> items = List.of();

    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> new PageResult<>(items, -1, 20, 0L, 0));

    assertEquals("ページ番号は0以上である必要があります。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given 0 のページサイズを用意する
   * When ページング済み検索結果を生成する
   * Then ページサイズ下限不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("ページサイズが1未満の場合は例外になること")
  void shouldThrowExceptionWhenSizeIsLessThanOne() {
    List<String> items = List.of();

    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> new PageResult<>(items, 0, 0, 0L, 0));

    assertEquals("ページサイズは1以上である必要があります。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given 最大値を超えるページサイズを用意する
   * When ページング済み検索結果を生成する
   * Then ページサイズ上限不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("ページサイズが最大値を超える場合は例外になること")
  void shouldThrowExceptionWhenSizeExceedsMaxSize() {
    List<String> items = List.of();

    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> new PageResult<>(items, 0, 101, 0L, 0));

    assertEquals("ページサイズは100以下である必要があります。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given 負の総件数を用意する
   * When ページング済み検索結果を生成する
   * Then 総件数不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("総件数が負の場合は例外になること")
  void shouldThrowExceptionWhenTotalElementsIsNegative() {
    List<String> items = List.of();

    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> new PageResult<>(items, 0, 20, -1L, 0));

    assertEquals("総件数は0以上である必要があります。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given 負の総ページ数を用意する
   * When ページング済み検索結果を生成する
   * Then 総ページ数不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("総ページ数が負の場合は例外になること")
  void shouldThrowExceptionWhenTotalPagesIsNegative() {
    List<String> items = List.of();

    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> new PageResult<>(items, 0, 20, 0L, -1));

    assertEquals("総ページ数は0以上である必要があります。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given ページサイズを超える要素一覧を用意する
   * When ページング済み検索結果を生成する
   * Then 要素数不正例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("要素数がページサイズを超える場合は例外になること")
  void shouldThrowExceptionWhenItemsSizeExceedsPageSize() {
    List<String> items = List.of("ORD000001", "ORD000002");

    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> new PageResult<>(items, 0, 1, 2L, 2));

    assertEquals("要素数はページサイズ以下である必要があります。", actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given 総件数とページサイズに一致しない総ページ数を用意する
   * When ページング済み検索結果を生成する
   * Then 総ページ数整合性例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("総ページ数が総件数とページサイズから算出される値と一致しない場合は例外になること")
  void shouldThrowExceptionWhenTotalPagesDoesNotMatchTotalElementsAndSize() {
    List<String> items = List.of("ORD000001", "ORD000002");

    IllegalArgumentException actual =
        assertThrows(IllegalArgumentException.class, () -> new PageResult<>(items, 0, 2, 3L, 1));

    assertEquals("総ページ数は総件数とページサイズから算出される値と一致する必要があります。", actual.getMessage());
  }
}
