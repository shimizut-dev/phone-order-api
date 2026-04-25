package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.application.order.OrderService;
import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.presentation.error.ApiErrorResponseMessages;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.api.OrdersApi;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderRequest;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        return OrderMapper.toResponseList(orderService.getOrders());
    }

    /**
     * 注文コードで注文を取得する
     *
     * @param orderCode 注文コード
     * @return 注文レスポンス
     */
    @Override
    public OrderResponse getOrderByOrderCode(final String orderCode) {
        Order order = orderService.getOrderByOrderCode(orderCode)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                ApiErrorResponseMessages.ORDER_NOT_FOUND
            ));

        return OrderMapper.toResponse(order);
    }

    /**
     * 注文を登録する
     *
     * @param orderRequest 注文リクエスト
     * @return 注文レスポンス
     */
    @Override
    public OrderResponse createOrder(@Valid @RequestBody final OrderRequest orderRequest) {
        return OrderMapper.toResponse(orderService.createOrder(orderRequest.getOrderedAt()));
    }

    /**
     * 注文をキャンセルする
     *
     * @param orderCode 注文コード
     * @return 注文レスポンス
     */
    @Override
    public OrderResponse cancelOrder(final String orderCode) {
        Order order = orderService.cancelOrder(orderCode)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                ApiErrorResponseMessages.ORDER_NOT_FOUND
            ));

        return OrderMapper.toResponse(order);
    }
}
