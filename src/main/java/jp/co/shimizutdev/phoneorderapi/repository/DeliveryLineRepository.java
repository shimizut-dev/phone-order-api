package jp.co.shimizutdev.phoneorderapi.repository;

import jp.co.shimizutdev.phoneorderapi.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * 配送明細Repository
 */
@Repository
public interface DeliveryLineRepository extends JpaRepository<DeliveryLineEntity, UUID> {
}
