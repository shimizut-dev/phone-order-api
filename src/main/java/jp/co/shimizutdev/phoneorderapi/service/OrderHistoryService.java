package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.OrderHistoryEntity;
import jp.co.shimizutdev.phoneorderapi.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 注文履歴Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    /**
     * IDで注文履歴を取得
     *
     * @param id 注文履歴ID
     * @return 注文履歴
     */
    public Optional<OrderHistoryEntity> getOrderHistoryById(UUID id) {
        return orderHistoryRepository.findById(id);
    }

    /**
     * 注文履歴を検索
     *
     * @return 注文履歴List
     */
    public List<OrderHistoryEntity> searchOrderHistories() {
        return orderHistoryRepository.findAll();
    }

    /**
     * 注文履歴を保存
     *
     * @param orderHistory 注文履歴
     * @return 注文履歴
     */
    @Transactional
    public OrderHistoryEntity saveOrderHistory(OrderHistoryEntity orderHistory) {
        return orderHistoryRepository.save(orderHistory);
    }
}
