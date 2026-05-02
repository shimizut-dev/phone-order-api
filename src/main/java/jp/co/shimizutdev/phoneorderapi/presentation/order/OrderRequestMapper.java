package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCode;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderedAt;
import jp.co.shimizutdev.phoneorderapi.domain.order.Version;
import jp.co.shimizutdev.phoneorderapi.presentation.error.ApiBadRequestException;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.CancelOrderRequest;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderRequest;

/** 注文リクエストマッパー */
public class OrderRequestMapper {

  /** コンストラクタ(インスタンス化を防止) */
  private OrderRequestMapper() {}

  /**
   * 注文コードを変換する
   *
   * @param orderCode 注文コード文字列
   * @return 注文コード
   * @throws ApiBadRequestException 注文コードの形式が不正な場合
   */
  public static OrderCode toOrderCode(final String orderCode) {
    try {
      return OrderCode.of(orderCode);
    } catch (IllegalArgumentException ex) {
      throw new ApiBadRequestException("orderCode", ex.getMessage());
    }
  }

  /**
   * 注文日時を変換する
   *
   * @param createOrderRequest 注文作成リクエスト
   * @return 注文日時
   * @throws ApiBadRequestException 注文日時が不正な場合
   */
  public static OrderedAt toOrderedAt(final OrderRequest createOrderRequest) {
    try {
      return OrderedAt.of(createOrderRequest.getOrderedAt());
    } catch (IllegalArgumentException ex) {
      throw new ApiBadRequestException("orderedAt", ex.getMessage());
    }
  }

  /**
   * バージョンを変換する
   *
   * @param cancelOrderRequest 注文キャンセルリクエスト
   * @return バージョン
   * @throws ApiBadRequestException バージョンが不正な場合
   */
  public static Version toVersion(final CancelOrderRequest cancelOrderRequest) {
    try {
      return Version.of(cancelOrderRequest.getVersion());
    } catch (IllegalArgumentException ex) {
      throw new ApiBadRequestException("version", ex.getMessage());
    }
  }
}
