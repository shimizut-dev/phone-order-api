package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.application.order.OrderService;
import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.presentation.exception.ApiErrorMessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 注文コントローラ
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    /**
     * 注文サービス
     */
    private final OrderService orderService;

    /**
     * 注文一覧を取得する
     *
     * @return 注文レスポンス一覧
     */
    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        return OrderMapper.toResponseList(orderService.getOrders());
    }

    /**
     * 注文コードで注文を取得する
     *
     * @param orderCode 注文コード
     * @return 注文レスポンス
     */
    @GetMapping("/orders/order-code/{orderCode}")
    public OrderResponse getOrderByOrderCode(@PathVariable final String orderCode) {
        Order order = orderService.getOrderByOrderCode(orderCode)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                ApiErrorMessageConstants.ORDER_NOT_FOUND
            ));

        return OrderMapper.toResponse(order);
    }

    /**
     * 注文を登録する
     *
     * @param orderRequest 注文リクエスト
     * @return 注文レスポンス
     */
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody final OrderRequest orderRequest) {
        return OrderMapper.toResponse(orderService.createOrder(orderRequest.orderedAt()));
    }
}
