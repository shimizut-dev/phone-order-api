package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jp.co.shimizutdev.phoneorderapi.application.order.OrderService;
import jp.co.shimizutdev.phoneorderapi.domain.common.PageResult;
import jp.co.shimizutdev.phoneorderapi.domain.common.PagingCondition;
import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCannotBeCancelledException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderNotFoundException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderVersionConflictException;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.api.OrdersApi;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.CancelOrderRequest;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderPageResponse;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderRequest;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/** 注文コントローラ */
@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

  /** 注文サービス */
  private final OrderService orderService;

  /**
   * 注文一覧を取得する
   *
   * @param page 取得ページ番号。0始まり。null の場合はデフォルトのページ番号
   * @param size 1ページあたりの取得件数。null の場合はデフォルトのページサイズ
   * @return 注文日時降順、同日時は注文コード降順のページング済み注文一覧レスポンス。該当ページが存在しない場合も空ページ
   */
  @Override
  public OrderPageResponse getOrders(final Integer page, final Integer size) {
    PageResult<Order> orders = orderService.getOrders(PagingCondition.ofNullable(page, size));
    return OrderResponseMapper.toPageResponse(orders);
  }

  /**
   * 注文コードで注文を取得する
   *
   * @param orderCode 注文コード
   * @return 注文レスポンス
   * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
   */
  @Override
  public OrderResponse getOrderByOrderCode(final String orderCode) {
    Order order = orderService.getOrderByOrderCode(OrderRequestMapper.toOrderCode(orderCode));
    return OrderResponseMapper.toResponse(order);
  }

  /**
   * 注文を登録する
   *
   * @param createOrderRequest 注文登録リクエスト
   * @return 注文レスポンス
   */
  @Override
  public OrderResponse createOrder(final OrderRequest createOrderRequest) {
    Order order = orderService.createOrder(OrderRequestMapper.toOrderedAt(createOrderRequest));
    return OrderResponseMapper.toResponse(order);
  }

  /**
   * 注文をキャンセルする
   *
   * @param orderCode 注文コード
   * @param cancelOrderRequest 注文キャンセルリクエスト
   * @return 注文レスポンス
   * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
   * @throws OrderCannotBeCancelledException 注文状態によりキャンセルできない場合
   * @throws OrderVersionConflictException 注文のバージョンが一致しない場合
   */
  @Override
  public OrderResponse cancelOrder(
      final String orderCode, final CancelOrderRequest cancelOrderRequest) {
    Order order =
        orderService.cancelOrder(
            OrderRequestMapper.toOrderCode(orderCode),
            OrderRequestMapper.toVersion(cancelOrderRequest));

    return OrderResponseMapper.toResponse(order);
  }
}
