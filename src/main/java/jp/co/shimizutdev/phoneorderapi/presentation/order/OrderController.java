package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jp.co.shimizutdev.phoneorderapi.application.order.OrderService;
import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCannotBeCancelledException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderNotFoundException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderVersionConflictException;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.api.OrdersApi;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.CancelOrderRequest;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderRequest;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 注文コントローラ
 */
@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    /**
     * 注文サービス
     */
    private final OrderService orderService;

    /**
     * 注文一覧を取得する
     *
     * @return 注文レスポンス一覧
     */
    @Override
    public List<OrderResponse> getOrders() {
        List<Order> orders = orderService.getOrders();
        return OrderMapper.toResponses(orders);
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
        Order order = orderService.getOrderByOrderCode(orderCode);
        return OrderMapper.toResponse(order);
    }

    /**
     * 注文を登録する
     *
     * @param createOrderRequest 注文登録リクエスト
     * @return 注文レスポンス
     */
    @Override
    public OrderResponse createOrder(final OrderRequest createOrderRequest) {
        Order order = orderService.createOrder(createOrderRequest.getOrderedAt());
        return OrderMapper.toResponse(order);
    }

    /**
     * 注文をキャンセルする
     *
     * @param orderCode          注文コード
     * @param cancelOrderRequest 注文キャンセルリクエスト
     * @return 注文レスポンス
     * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
     * @throws OrderCannotBeCancelledException 注文の状態によりキャンセルできない場合
     * @throws OrderVersionConflictException 注文のバージョンが一致しない場合
     */
    @Override
    public OrderResponse cancelOrder(
        final String orderCode,
        final CancelOrderRequest cancelOrderRequest) {
        Order order = orderService.cancelOrder(orderCode, cancelOrderRequest.getVersion());
        return OrderMapper.toResponse(order);
    }
}
