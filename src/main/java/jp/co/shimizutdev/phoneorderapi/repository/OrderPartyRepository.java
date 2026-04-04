package jp.co.shimizutdev.phoneorderapi.repository;

import jp.co.shimizutdev.phoneorderapi.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * 注文関係者Repository
 */
@Repository
public interface OrderPartyRepository extends JpaRepository<OrderPartyEntity, UUID> {
}
