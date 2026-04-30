package jp.co.shimizutdev.phoneorderapi.domain.common;

import java.util.List;
import java.util.Objects;

/**
 * ページング済みの検索結果
 *
 * @param items 取得した要素一覧
 * @param page 取得ページ番号。0 始まり
 * @param size 1 ページあたりの取得件数。1 以上 100 以下
 * @param totalElements 条件に一致する総件数
 * @param totalPages 総件数とページサイズから算出した総ページ数
 * @param <T> 要素型
 */
public record PageResult<T>(List<T> items, int page, int size, long totalElements, int totalPages) {

  /**
   * ページング済みの検索結果を生成する
   *
   * <p>要素一覧は防御的コピーされ、不変リストとして保持される
   *
   * <p>要素数はページサイズ以下、総ページ数は総件数とページサイズから算出される値と一致する必要がある
   *
   * @throws NullPointerException 要素一覧が null の場合
   * @throws IllegalArgumentException ページ番号、ページサイズ、総件数、総ページ数、要素数が不正な場合
   */
  public PageResult {
    items = List.copyOf(Objects.requireNonNull(items, "要素一覧は必須です。"));
    if (page < 0) {
      throw new IllegalArgumentException("ページ番号は0以上である必要があります。");
    }
    if (size < 1) {
      throw new IllegalArgumentException("ページサイズは1以上である必要があります。");
    }
    if (size > PagingCondition.MAX_SIZE) {
      throw new IllegalArgumentException("ページサイズは" + PagingCondition.MAX_SIZE + "以下である必要があります。");
    }
    if (totalElements < 0) {
      throw new IllegalArgumentException("総件数は0以上である必要があります。");
    }
    if (totalPages < 0) {
      throw new IllegalArgumentException("総ページ数は0以上である必要があります。");
    }
    if (items.size() > size) {
      throw new IllegalArgumentException("要素数はページサイズ以下である必要があります。");
    }
    long calculatedTotalPages = totalElements == 0L ? 0L : ((totalElements - 1L) / size) + 1L;
    if (totalPages != calculatedTotalPages) {
      throw new IllegalArgumentException("総ページ数は総件数とページサイズから算出される値と一致する必要があります。");
    }
  }

  /**
   * 次ページが存在するか判定する
   *
   * @return 次ページが存在する場合 true
   */
  public boolean hasNext() {
    return page + 1 < totalPages;
  }

  /**
   * 前ページが存在するか判定する
   *
   * @return 前ページが存在する場合 true
   */
  public boolean hasPrevious() {
    return page > 0;
  }
}
