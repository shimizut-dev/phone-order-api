package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.Order;

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

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderCode(order.getOrderCode().getValue());
        orderResponse.setOrderedAt(order.getOrderedAt().getValue());
        orderResponse.setOrderStatus(order.getOrderStatus().getCode());

        return orderResponse;
    }

    /**
     * 注文リストを注文レスポンスリストへ変換する
     *
     * @param orderList 注文リスト
     * @return 注文レスポンスリスト
     */
    public static List<OrderResponse> toResponseList(final List<Order> orderList) {
        if (orderList == null) {
            return Collections.emptyList();
        }

        return orderList.stream()
            .map(OrderMapper::toResponse)
            .filter(Objects::nonNull)
            .toList();
    }
}
