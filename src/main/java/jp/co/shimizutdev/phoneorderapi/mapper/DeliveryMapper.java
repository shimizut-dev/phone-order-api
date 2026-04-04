package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.DeliveryRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.DeliveryResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 配送Mapper
 */
public final class DeliveryMapper {

    /**
     * インスタンス化を防止
     */
    private DeliveryMapper() {
    }

    /**
     * 配送Request DTOを配送エンティティへ変換
     *
     * @param dto 配送Request DTO
     * @param order 注文
     * @return 配送
     */
    public static DeliveryEntity toEntity(DeliveryRequestDto dto, OrderEntity order) {
        if (dto == null) {
            return null;
        }

        DeliveryEntity entity = new DeliveryEntity();
        entity.setOrder(order);
        entity.setDeliveryCode(dto.getDeliveryCode());
        entity.setDeliveryStatus(dto.getDeliveryStatus());
        entity.setRecipientLastName(dto.getRecipientLastName());
        entity.setRecipientFirstName(dto.getRecipientFirstName());
        entity.setRecipientLastNameKana(dto.getRecipientLastNameKana());
        entity.setRecipientFirstNameKana(dto.getRecipientFirstNameKana());
        entity.setDeliveryPostalCode(dto.getDeliveryPostalCode());
        entity.setDeliveryPrefecture(dto.getDeliveryPrefecture());
        entity.setDeliveryCity(dto.getDeliveryCity());
        entity.setDeliveryTown(dto.getDeliveryTown());
        entity.setDeliveryStreetAddress(dto.getDeliveryStreetAddress());
        entity.setDeliveryBuildingName(dto.getDeliveryBuildingName());
        entity.setDeliveryTimeSlotDivision(dto.getDeliveryTimeSlotDivision());
        entity.setLeavePlaceDivision(dto.getLeavePlaceDivision());
        entity.setLeaveAtPlaceDivision(dto.getLeaveAtPlaceDivision());
        entity.setDeliveryLines(DeliveryLineMapper.toEntityList(dto.getDeliveryLines(), entity));
        entity.setDeliveryHistories(DeliveryHistoryMapper.toEntityList(dto.getDeliveryHistories(), entity));
        return entity;
    }

    /**
     * 配送Listを配送エンティティListへ変換
     *
     * @param dtos 配送Request DTO List
     * @param order 注文
     * @return 配送List
     */
    public static List<DeliveryEntity> toEntityList(List<DeliveryRequestDto> dtos, OrderEntity order) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> toEntity(dto, order))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 配送エンティティを配送Response DTOへ変換
     *
     * @param entity 配送
     * @return 配送Response DTO
     */
    public static DeliveryResponseDto toResponseDto(DeliveryEntity entity) {
        if (entity == null) {
            return null;
        }

        DeliveryResponseDto dto = new DeliveryResponseDto();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getId() : null);
        dto.setDeliveryCode(entity.getDeliveryCode());
        dto.setDeliveryStatus(entity.getDeliveryStatus());
        dto.setRecipientLastName(entity.getRecipientLastName());
        dto.setRecipientFirstName(entity.getRecipientFirstName());
        dto.setRecipientLastNameKana(entity.getRecipientLastNameKana());
        dto.setRecipientFirstNameKana(entity.getRecipientFirstNameKana());
        dto.setDeliveryPostalCode(entity.getDeliveryPostalCode());
        dto.setDeliveryPrefecture(entity.getDeliveryPrefecture());
        dto.setDeliveryCity(entity.getDeliveryCity());
        dto.setDeliveryTown(entity.getDeliveryTown());
        dto.setDeliveryStreetAddress(entity.getDeliveryStreetAddress());
        dto.setDeliveryBuildingName(entity.getDeliveryBuildingName());
        dto.setDeliveryTimeSlotDivision(entity.getDeliveryTimeSlotDivision());
        dto.setLeavePlaceDivision(entity.getLeavePlaceDivision());
        dto.setLeaveAtPlaceDivision(entity.getLeaveAtPlaceDivision());
        dto.setDeliveryLines(DeliveryLineMapper.toResponseDtoList(entity.getDeliveryLines()));
        dto.setDeliveryHistories(DeliveryHistoryMapper.toResponseDtoList(entity.getDeliveryHistories()));
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }

    /**
     * 配送Listを配送Response DTO Listへ変換
     *
     * @param entities 配送List
     * @return 配送Response DTO List
     */
    public static List<DeliveryResponseDto> toResponseDtoList(List<DeliveryEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(DeliveryMapper::toResponseDto)
                .filter(Objects::nonNull)
                .toList();
    }
}
