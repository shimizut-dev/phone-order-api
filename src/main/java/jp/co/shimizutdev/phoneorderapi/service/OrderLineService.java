package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;
import jp.co.shimizutdev.phoneorderapi.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 注文明細Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;

    /**
     * IDで注文明細を取得
     *
     * @param id 注文明細ID
     * @return 注文明細
     */
    public Optional<OrderLineEntity> getOrderLineById(UUID id) {
        return orderLineRepository.findById(id);
    }

    /**
     * 注文明細を検索
     *
     * @return 注文明細List
     */
    public List<OrderLineEntity> searchOrderLines() {
        return orderLineRepository.findAll();
    }

    /**
     * 注文明細を保存
     *
     * @param orderLine 注文明細
     * @return 注文明細
     */
    @Transactional
    public OrderLineEntity saveOrderLine(OrderLineEntity orderLine) {
        return orderLineRepository.save(orderLine);
    }
}
