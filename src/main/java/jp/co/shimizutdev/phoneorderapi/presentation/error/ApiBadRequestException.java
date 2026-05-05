package jp.co.shimizutdev.phoneorderapi.presentation.error;

import lombok.Getter;

/** API入力不正エラー */
@Getter
public class ApiBadRequestException extends RuntimeException {

  /** エラー対象項目 */
  private final String field;

  /**
   * コンストラクタ
   *
   * @param field エラー対象項目
   * @param message エラーメッセージ
   */
  public ApiBadRequestException(final String field, final String message) {
    super(message);
    this.field = field;
  }
}
