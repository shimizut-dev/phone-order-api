package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.DeliveryHistoryRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.DeliveryHistoryResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryEntity;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryHistoryEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 配送履歴Mapper
 */
public final class DeliveryHistoryMapper {

    /**
     * インスタンス化を防止
     */
    private DeliveryHistoryMapper() {
    }

    /**
     * 配送履歴Request DTOを配送履歴エンティティへ変換
     *
     * @param dto 配送履歴Request DTO
     * @param delivery 配送
     * @return 配送履歴
     */
    public static DeliveryHistoryEntity toEntity(DeliveryHistoryRequestDto dto, DeliveryEntity delivery) {
        if (dto == null) {
            return null;
        }

        DeliveryHistoryEntity entity = new DeliveryHistoryEntity();
        entity.setDelivery(delivery);
        entity.setDeliveryStatus(dto.getDeliveryStatus());
        return entity;
    }

    /**
     * 配送履歴Listを配送履歴エンティティListへ変換
     *
     * @param dtos 配送履歴Request DTO List
     * @param delivery 配送
     * @return 配送履歴List
     */
    public static List<DeliveryHistoryEntity> toEntityList(List<DeliveryHistoryRequestDto> dtos, DeliveryEntity delivery) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> toEntity(dto, delivery))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 配送履歴エンティティを配送履歴Response DTOへ変換
     *
     * @param entity 配送履歴
     * @return 配送履歴Response DTO
     */
    public static DeliveryHistoryResponseDto toResponseDto(DeliveryHistoryEntity entity) {
        if (entity == null) {
            return null;
        }

        DeliveryHistoryResponseDto dto = new DeliveryHistoryResponseDto();
        dto.setId(entity.getId());
        dto.setDeliveryId(entity.getDelivery() != null ? entity.getDelivery().getId() : null);
        dto.setDeliveryStatus(entity.getDeliveryStatus());
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }

    /**
     * 配送履歴Listを配送履歴Response DTO Listへ変換
     *
     * @param entities 配送履歴List
     * @return 配送履歴Response DTO List
     */
    public static List<DeliveryHistoryResponseDto> toResponseDtoList(List<DeliveryHistoryEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(DeliveryHistoryMapper::toResponseDto)
                .filter(Objects::nonNull)
                .toList();
    }
}
