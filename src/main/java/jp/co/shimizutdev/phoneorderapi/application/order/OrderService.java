package jp.co.shimizutdev.phoneorderapi.application.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCode;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCodeGenerator;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 注文サービス
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    /**
     * 注文リポジトリ
     */
    private final OrderRepository orderRepository;

    /**
     * 注文コード採番
     */
    private final OrderCodeGenerator orderCodeGenerator;

    /**
     * 注文一覧を取得する
     *
     * @return 注文一覧
     */
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    /**
     * 注文コードで注文を取得する
     *
     * @param orderCode 注文コード
     * @return 注文
     */
    public Optional<Order> getOrderByOrderCode(final String orderCode) {
        return orderRepository.findByOrderCode(OrderCode.of(orderCode));
    }

    /**
     * 注文を登録する
     *
     * @param orderedAt 注文日時
     * @return 注文
     */
    @Transactional
    public Order createOrder(final OffsetDateTime orderedAt) {
        Order order = Order.create(orderedAt, orderCodeGenerator);
        return orderRepository.create(order);
    }

    /**
     * 注文をキャンセルする
     *
     * @param orderCode 注文コード
     * @return 注文
     */
    @Transactional
    public Optional<Order> cancelOrder(final String orderCode) {
        return orderRepository.findByOrderCode(OrderCode.of(orderCode))
            .map(Order::cancel)
            .map(orderRepository::update);
    }
}
