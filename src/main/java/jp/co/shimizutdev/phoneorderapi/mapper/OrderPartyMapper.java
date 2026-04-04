package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.OrderPartyRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.OrderPartyResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderPartyEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 注文関係者Mapper
 */
public final class OrderPartyMapper {

    /**
     * インスタンス化を防止
     */
    private OrderPartyMapper() {
    }

    /**
     * 注文関係者Request DTOを注文関係者エンティティへ変換
     *
     * @param dto 注文関係者Request DTO
     * @param order 注文
     * @return 注文関係者
     */
    public static OrderPartyEntity toEntity(OrderPartyRequestDto dto, OrderEntity order) {
        if (dto == null) {
            return null;
        }

        OrderPartyEntity entity = new OrderPartyEntity();
        entity.setOrder(order);
        entity.setOrderPartyCode(dto.getOrderPartyCode());
        entity.setOrderPartyRoleDivision(dto.getOrderPartyRoleDivision());
        entity.setOrderPartyLastName(dto.getOrderPartyLastName());
        entity.setOrderPartyFirstName(dto.getOrderPartyFirstName());
        entity.setOrderPartyLastNameKana(dto.getOrderPartyLastNameKana());
        entity.setOrderPartyFirstNameKana(dto.getOrderPartyFirstNameKana());
        entity.setOrderPartyGenderDivision(dto.getOrderPartyGenderDivision());
        entity.setOrderPartyBirthDate(dto.getOrderPartyBirthDate());
        entity.setOrderPartyPostalCode(dto.getOrderPartyPostalCode());
        entity.setOrderPartyPrefecture(dto.getOrderPartyPrefecture());
        entity.setOrderPartyCity(dto.getOrderPartyCity());
        entity.setOrderPartyTown(dto.getOrderPartyTown());
        entity.setOrderPartyStreetAddress(dto.getOrderPartyStreetAddress());
        entity.setOrderPartyBuildingName(dto.getOrderPartyBuildingName());
        return entity;
    }

    /**
     * 注文関係者Listを注文関係者エンティティListへ変換
     *
     * @param dtos 注文関係者Request DTO List
     * @param order 注文
     * @return 注文関係者List
     */
    public static List<OrderPartyEntity> toEntityList(List<OrderPartyRequestDto> dtos, OrderEntity order) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> toEntity(dto, order))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 注文関係者エンティティを注文関係者Response DTOへ変換
     *
     * @param entity 注文関係者
     * @return 注文関係者Response DTO
     */
    public static OrderPartyResponseDto toResponseDto(OrderPartyEntity entity) {
        if (entity == null) {
            return null;
        }

        OrderPartyResponseDto dto = new OrderPartyResponseDto();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getId() : null);
        dto.setOrderPartyCode(entity.getOrderPartyCode());
        dto.setOrderPartyRoleDivision(entity.getOrderPartyRoleDivision());
        dto.setOrderPartyLastName(entity.getOrderPartyLastName());
        dto.setOrderPartyFirstName(entity.getOrderPartyFirstName());
        dto.setOrderPartyLastNameKana(entity.getOrderPartyLastNameKana());
        dto.setOrderPartyFirstNameKana(entity.getOrderPartyFirstNameKana());
        dto.setOrderPartyGenderDivision(entity.getOrderPartyGenderDivision());
        dto.setOrderPartyBirthDate(entity.getOrderPartyBirthDate());
        dto.setOrderPartyPostalCode(entity.getOrderPartyPostalCode());
        dto.setOrderPartyPrefecture(entity.getOrderPartyPrefecture());
        dto.setOrderPartyCity(entity.getOrderPartyCity());
        dto.setOrderPartyTown(entity.getOrderPartyTown());
        dto.setOrderPartyStreetAddress(entity.getOrderPartyStreetAddress());
        dto.setOrderPartyBuildingName(entity.getOrderPartyBuildingName());
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }

    /**
     * 注文関係者Listを注文関係者Response DTO Listへ変換
     *
     * @param entities 注文関係者List
     * @return 注文関係者Response DTO List
     */
    public static List<OrderPartyResponseDto> toResponseDtoList(List<OrderPartyEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(OrderPartyMapper::toResponseDto)
                .filter(Objects::nonNull)
                .toList();
    }
}
