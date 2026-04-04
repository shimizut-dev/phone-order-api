package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.OrderPartyEntity;
import jp.co.shimizutdev.phoneorderapi.repository.OrderPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 注文関係者Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderPartyService {

    private final OrderPartyRepository orderPartyRepository;

    /**
     * IDで注文関係者を取得
     *
     * @param id 注文関係者ID
     * @return 注文関係者
     */
    public Optional<OrderPartyEntity> getOrderPartyById(UUID id) {
        return orderPartyRepository.findById(id);
    }

    /**
     * 注文関係者を検索
     *
     * @return 注文関係者List
     */
    public List<OrderPartyEntity> searchOrderParties() {
        return orderPartyRepository.findAll();
    }

    /**
     * 注文関係者を保存
     *
     * @param orderParty 注文関係者
     * @return 注文関係者
     */
    @Transactional
    public OrderPartyEntity saveOrderParty(OrderPartyEntity orderParty) {
        return orderPartyRepository.save(orderParty);
    }
}
