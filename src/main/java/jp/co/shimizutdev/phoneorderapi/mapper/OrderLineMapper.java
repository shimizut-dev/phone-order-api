package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.OrderLineRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.OrderLineResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 注文明細Mapper
 */
public final class OrderLineMapper {

    /**
     * インスタンス化を防止
     */
    private OrderLineMapper() {
    }

    /**
     * 注文明細Request DTOを注文明細エンティティへ変換
     *
     * @param dto 注文明細Request DTO
     * @param order 注文
     * @return 注文明細
     */
    public static OrderLineEntity toEntity(OrderLineRequestDto dto, OrderEntity order) {
        if (dto == null) {
            return null;
        }

        OrderLineEntity entity = new OrderLineEntity();
        entity.setOrder(order);
        entity.setOrderLineCode(dto.getOrderLineCode());
        entity.setOrderLineDivision(dto.getOrderLineDivision());
        entity.setOrderQuantity(dto.getOrderQuantity());
        entity.setLine(LineMapper.toEntity(dto.getLine(), entity));
        entity.setPhone(PhoneMapper.toEntity(dto.getPhone(), entity));
        entity.setAccessory(AccessoryMapper.toEntity(dto.getAccessory(), entity));
        return entity;
    }

    /**
     * 注文明細Listを注文明細エンティティListへ変換
     *
     * @param dtos 注文明細Request DTO List
     * @param order 注文
     * @return 注文明細List
     */
    public static List<OrderLineEntity> toEntityList(List<OrderLineRequestDto> dtos, OrderEntity order) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> toEntity(dto, order))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 注文明細エンティティを注文明細Response DTOへ変換
     *
     * @param entity 注文明細
     * @return 注文明細Response DTO
     */
    public static OrderLineResponseDto toResponseDto(OrderLineEntity entity) {
        if (entity == null) {
            return null;
        }

        OrderLineResponseDto dto = new OrderLineResponseDto();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getId() : null);
        dto.setOrderLineCode(entity.getOrderLineCode());
        dto.setOrderLineDivision(entity.getOrderLineDivision());
        dto.setOrderQuantity(entity.getOrderQuantity());
        dto.setLine(LineMapper.toResponseDto(entity.getLine()));
        dto.setPhone(PhoneMapper.toResponseDto(entity.getPhone()));
        dto.setAccessory(AccessoryMapper.toResponseDto(entity.getAccessory()));
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }

    /**
     * 注文明細Listを注文明細Response DTO Listへ変換
     *
     * @param entities 注文明細List
     * @return 注文明細Response DTO List
     */
    public static List<OrderLineResponseDto> toResponseDtoList(List<OrderLineEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(OrderLineMapper::toResponseDto)
                .filter(Objects::nonNull)
                .toList();
    }
}
