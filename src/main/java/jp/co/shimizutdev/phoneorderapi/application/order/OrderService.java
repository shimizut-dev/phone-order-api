package jp.co.shimizutdev.phoneorderapi.application.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 注文サービス
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
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
     * @throws IllegalArgumentException 注文コードの形式が不正な場合
     * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
     */
    public Order getOrderByOrderCode(final String orderCode) {
        return findOrderByOrderCode(orderCode);
    }

    /**
     * 注文を登録する
     *
     * @param orderedAt 注文日時
     * @return 注文
     * @throws IllegalArgumentException 注文日時が指定されていない場合
     */
    @Transactional
    public Order createOrder(final OffsetDateTime orderedAt) {
        OrderCode orderCode = orderCodeGenerator.generate();
        Order order = Order.create(orderCode, OrderedAt.of(orderedAt));
        return orderRepository.create(order);
    }

    /**
     * 注文をキャンセルする
     *
     * @param orderCode 注文コード
     * @param version   キャンセル要求時の注文バージョン
     * @return 注文
     * @throws IllegalArgumentException 注文コードまたはバージョンが不正な場合
     * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
     * @throws OrderCannotBeCancelledException 注文の状態によりキャンセルできない場合
     * @throws OrderVersionConflictException 注文のバージョンが一致しない場合
     */
    @Transactional
    public Order cancelOrder(final String orderCode, final long version) {
        Order order = findOrderByOrderCode(orderCode);

        return orderRepository.update(order.cancel(Version.of(version)));
    }

    /**
     * 注文コードで注文を取得し、存在しない場合は注文未存在例外を発生させる。
     *
     * @param orderCode 注文コード
     * @return 注文
     * @throws IllegalArgumentException 注文コードの形式が不正な場合
     * @throws OrderNotFoundException   注文コードに対応する注文が存在しない場合
     */
    private Order findOrderByOrderCode(final String orderCode) {
        return orderRepository.findByOrderCode(OrderCode.of(orderCode))
            .orElseThrow(() -> OrderNotFoundException.byOrderCode(orderCode));
    }
}
