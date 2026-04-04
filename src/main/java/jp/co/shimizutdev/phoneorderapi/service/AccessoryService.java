package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.AccessoryEntity;
import jp.co.shimizutdev.phoneorderapi.repository.AccessoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 付属品Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;

    /**
     * IDで付属品を取得
     *
     * @param id 付属品ID
     * @return 付属品
     */
    public Optional<AccessoryEntity> getAccessoryById(UUID id) {
        return accessoryRepository.findById(id);
    }

    /**
     * 付属品を検索
     *
     * @return 付属品List
     */
    public List<AccessoryEntity> searchAccessories() {
        return accessoryRepository.findAll();
    }

    /**
     * 付属品を保存
     *
     * @param accessory 付属品
     * @return 付属品
     */
    @Transactional
    public AccessoryEntity saveAccessory(AccessoryEntity accessory) {
        return accessoryRepository.save(accessory);
    }
}
