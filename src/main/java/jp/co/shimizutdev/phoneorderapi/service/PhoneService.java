package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.PhoneEntity;
import jp.co.shimizutdev.phoneorderapi.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 端末Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhoneService {

    private final PhoneRepository phoneRepository;

    /**
     * IDで端末を取得
     *
     * @param id 端末ID
     * @return 端末
     */
    public Optional<PhoneEntity> getPhoneById(UUID id) {
        return phoneRepository.findById(id);
    }

    /**
     * 端末を検索
     *
     * @return 端末List
     */
    public List<PhoneEntity> searchPhones() {
        return phoneRepository.findAll();
    }

    /**
     * 端末を保存
     *
     * @param phone 端末
     * @return 端末
     */
    @Transactional
    public PhoneEntity savePhone(PhoneEntity phone) {
        return phoneRepository.save(phone);
    }
}
