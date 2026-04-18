package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.presentation.generated.model.OrderResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 注文マッパー
 */
public class OrderMapper {

    /**
     * コンストラクタ(インスタンス化を防止)
     */
    private OrderMapper() {
    }

    /**
     * 注文を注文レスポンスへ変換する
     *
     * @param order 注文
     * @return 注文レスポンス
     */
    public static OrderResponse toResponse(final Order order) {
        if (order == null) {
            return null;
        }

        return new OrderResponse(
            order.getOrderCode().getValue(),
            order.getOrderedAt().getValue(),
            OrderResponse.OrderStatusEnum.fromValue(order.getOrderStatus().getCode())
        );
    }

    /**
     * 注文一覧を注文レスポンス一覧へ変換する
     *
     * @param orders 注文一覧
     * @return 注文レスポンス一覧
     */
    public static List<OrderResponse> toResponseList(final List<Order> orders) {
        if (orders == null) {
            return Collections.emptyList();
        }

        return orders.stream()
            .map(OrderMapper::toResponse)
            .filter(Objects::nonNull)
            .toList();
    }
}
