package jp.co.shimizutdev.phoneorderapi.repository;

import jp.co.shimizutdev.phoneorderapi.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * 端末Repository
 */
@Repository
public interface PhoneRepository extends JpaRepository<PhoneEntity, UUID> {
}
