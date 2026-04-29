package jp.co.shimizutdev.phoneorderapi.domain.common;

/**
 * ページング条件
 *
 * @param page 取得ページ番号。0 始まり
 * @param size 1 ページあたりの取得件数。1 以上 100 以下
 */
public record PagingCondition(int page, int size) {

    /**
     * デフォルトページ番号
     */
    public static final int DEFAULT_PAGE = 0;

    /**
     * デフォルトページサイズ
     */
    public static final int DEFAULT_SIZE = 20;

    /**
     * 最大ページサイズ
     */
    public static final int MAX_SIZE = 100;

    /**
     * ページング条件を生成する
     *
     * <p>ページ番号は 0 以上、ページサイズは 1 以上 100 以下である必要がある</p>
     *
     * @throws IllegalArgumentException ページ番号またはページサイズが不正な場合
     */
    public PagingCondition {
        if (page < 0) {
            throw new IllegalArgumentException("ページ番号は0以上である必要があります。");
        }
        if (size < 1) {
            throw new IllegalArgumentException("ページサイズは1以上である必要があります。");
        }
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException("ページサイズは" + MAX_SIZE + "以下である必要があります。");
        }
    }

    /**
     * ページング条件を生成する
     *
     * @param page 取得ページ番号。0 始まり
     * @param size 1 ページあたりの取得件数。1 以上 100 以下
     * @return ページング条件
     * @throws IllegalArgumentException ページ番号またはページサイズが不正な場合
     */
    public static PagingCondition of(final int page, final int size) {
        return new PagingCondition(page, size);
    }

    /**
     * 未指定値をデフォルト値で補完してページング条件を生成する
     *
     * @param page 取得ページ番号。null の場合はデフォルトページ番号
     * @param size 1 ページあたりの取得件数。null の場合はデフォルトページサイズ
     * @return ページング条件
     * @throws IllegalArgumentException ページ番号またはページサイズが不正な場合
     */
    public static PagingCondition ofNullable(final Integer page, final Integer size) {
        return new PagingCondition(
            page == null ? DEFAULT_PAGE : page,
            size == null ? DEFAULT_SIZE : size
        );
    }
}
