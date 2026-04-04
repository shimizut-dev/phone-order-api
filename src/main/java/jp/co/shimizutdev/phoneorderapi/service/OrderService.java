package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 注文Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * IDで注文を取得
     *
     * @param id 注文ID
     * @return 注文
     */
    public Optional<OrderEntity> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    /**
     * 注文コードで注文を取得
     *
     * @param orderCode 注文コード
     * @return 注文
     */
    public Optional<OrderEntity> getOrderByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    /**
     * 注文を検索
     *
     * @return 注文List
     */
    public List<OrderEntity> searchOrders() {
        return orderRepository.findAll();
    }

    /**
     * 注文を保存
     *
     * @param order 注文
     * @return 注文
     */
    @Transactional
    public OrderEntity saveOrder(OrderEntity order) {
        return orderRepository.save(order);
    }
}
