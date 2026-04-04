package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.DeliveryLineEntity;
import jp.co.shimizutdev.phoneorderapi.repository.DeliveryLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 配送明細Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryLineService {

    private final DeliveryLineRepository deliveryLineRepository;

    /**
     * IDで配送明細を取得
     *
     * @param id 配送明細ID
     * @return 配送明細
     */
    public Optional<DeliveryLineEntity> getDeliveryLineById(UUID id) {
        return deliveryLineRepository.findById(id);
    }

    /**
     * 配送明細を検索
     *
     * @return 配送明細List
     */
    public List<DeliveryLineEntity> searchDeliveryLines() {
        return deliveryLineRepository.findAll();
    }

    /**
     * 配送明細を保存
     *
     * @param deliveryLine 配送明細
     * @return 配送明細
     */
    @Transactional
    public DeliveryLineEntity saveDeliveryLine(DeliveryLineEntity deliveryLine) {
        return deliveryLineRepository.save(deliveryLine);
    }
}
