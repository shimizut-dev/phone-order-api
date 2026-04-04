package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.DeliveryHistoryEntity;
import jp.co.shimizutdev.phoneorderapi.repository.DeliveryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 配送履歴Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryHistoryService {

    private final DeliveryHistoryRepository deliveryHistoryRepository;

    /**
     * IDで配送履歴を取得
     *
     * @param id 配送履歴ID
     * @return 配送履歴
     */
    public Optional<DeliveryHistoryEntity> getDeliveryHistoryById(UUID id) {
        return deliveryHistoryRepository.findById(id);
    }

    /**
     * 配送履歴を検索
     *
     * @return 配送履歴List
     */
    public List<DeliveryHistoryEntity> searchDeliveryHistories() {
        return deliveryHistoryRepository.findAll();
    }

    /**
     * 配送履歴を保存
     *
     * @param deliveryHistory 配送履歴
     * @return 配送履歴
     */
    @Transactional
    public DeliveryHistoryEntity saveDeliveryHistory(DeliveryHistoryEntity deliveryHistory) {
        return deliveryHistoryRepository.save(deliveryHistory);
    }
}
