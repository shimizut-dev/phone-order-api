package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.DeliveryEntity;
import jp.co.shimizutdev.phoneorderapi.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 配送Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    /**
     * IDで配送を取得
     *
     * @param id 配送ID
     * @return 配送
     */
    public Optional<DeliveryEntity> getDeliveryById(UUID id) {
        return deliveryRepository.findById(id);
    }

    /**
     * 配送を検索
     *
     * @return 配送List
     */
    public List<DeliveryEntity> searchDeliveries() {
        return deliveryRepository.findAll();
    }

    /**
     * 配送を保存
     *
     * @param delivery 配送
     * @return 配送
     */
    @Transactional
    public DeliveryEntity saveDelivery(DeliveryEntity delivery) {
        return deliveryRepository.save(delivery);
    }
}
