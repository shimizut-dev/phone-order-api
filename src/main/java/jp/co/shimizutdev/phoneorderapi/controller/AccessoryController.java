package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.AccessoryRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.AccessoryResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.AccessoryEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.AccessoryMapper;
import jp.co.shimizutdev.phoneorderapi.service.AccessoryService;
import jp.co.shimizutdev.phoneorderapi.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * アクセサリController
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccessoryController {

    private final AccessoryService accessoryService;
    private final OrderLineService orderLineService;

    /**
     * アクセサリを検索
     *
     * @return アクセサリResponse DTO List
     */
    @GetMapping("/accessories")
    public List<AccessoryResponseDto> searchAccessories() {
        return accessoryService.searchAccessories().stream()
                .map(AccessoryMapper::toResponseDto)
                .toList();
    }

    /**
     * アクセサリIDでアクセサリを参照
     *
     * @param id アクセサリID
     * @return アクセサリResponse DTO
     */
    @GetMapping("/accessories/{id}")
    public AccessoryResponseDto getAccessoryById(@PathVariable UUID id) {
        return AccessoryMapper.toResponseDto(getAccessoryOrThrow(id));
    }

    /**
     * 注文明細にアクセサリを登録
     *
     * @param orderLineId 注文明細ID
     * @param requestDto アクセサリRequest DTO
     * @return アクセサリResponse DTO
     */
    @PostMapping("/order-lines/{orderLineId}/accessories")
    @ResponseStatus(HttpStatus.CREATED)
    public AccessoryResponseDto createAccessory(@PathVariable UUID orderLineId,
                                                @Valid @RequestBody AccessoryRequestDto requestDto) {
        OrderLineEntity orderLine = getOrderLineOrThrow(orderLineId);
        AccessoryEntity accessory = AccessoryMapper.toEntity(requestDto, orderLine);
        return AccessoryMapper.toResponseDto(accessoryService.saveAccessory(accessory));
    }

    /**
     * アクセサリIDでアクセサリを取得
     *
     * @param id アクセサリID
     * @return アクセサリ
     */
    private AccessoryEntity getAccessoryOrThrow(UUID id) {
        return accessoryService.getAccessoryById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "アクセサリが見つかりません。"));
    }

    /**
     * 注文明細IDで注文明細を取得
     *
     * @param orderLineId 注文明細ID
     * @return 注文明細
     */
    private OrderLineEntity getOrderLineOrThrow(UUID orderLineId) {
        return orderLineService.getOrderLineById(orderLineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "注文明細が見つかりません。"));
    }
}
