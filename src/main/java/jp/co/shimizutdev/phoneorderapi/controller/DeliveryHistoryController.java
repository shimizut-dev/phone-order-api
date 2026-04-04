package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.DeliveryHistoryRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.DeliveryHistoryResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryEntity;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryHistoryEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.DeliveryHistoryMapper;
import jp.co.shimizutdev.phoneorderapi.service.DeliveryHistoryService;
import jp.co.shimizutdev.phoneorderapi.service.DeliveryService;
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
 * 配送履歴Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryHistoryController {

    private final DeliveryHistoryService deliveryHistoryService;
    private final DeliveryService deliveryService;

    /**
     * 配送履歴を検索
     *
     * @return 配送履歴Response DTO List
     */
    @GetMapping("/delivery-histories")
    public List<DeliveryHistoryResponseDto> searchDeliveryHistories() {
        return DeliveryHistoryMapper.toResponseDtoList(deliveryHistoryService.searchDeliveryHistories());
    }

    /**
     * 配送履歴IDで配送履歴を参照
     *
     * @param id 配送履歴ID
     * @return 配送履歴Response DTO
     */
    @GetMapping("/delivery-histories/{id}")
    public DeliveryHistoryResponseDto getDeliveryHistoryById(@PathVariable UUID id) {
        return DeliveryHistoryMapper.toResponseDto(getDeliveryHistoryOrThrow(id));
    }

    /**
     * 配送に配送履歴を登録
     *
     * @param deliveryId 配送ID
     * @param requestDto 配送履歴Request DTO
     * @return 配送履歴Response DTO
     */
    @PostMapping("/deliveries/{deliveryId}/delivery-histories")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryHistoryResponseDto createDeliveryHistory(@PathVariable UUID deliveryId,
                                                            @Valid @RequestBody DeliveryHistoryRequestDto requestDto) {
        DeliveryEntity delivery = getDeliveryOrThrow(deliveryId);
        DeliveryHistoryEntity deliveryHistory = DeliveryHistoryMapper.toEntity(requestDto, delivery);
        return DeliveryHistoryMapper.toResponseDto(deliveryHistoryService.saveDeliveryHistory(deliveryHistory));
    }

    /**
     * 配送IDで配送を取得
     *
     * @param deliveryId 配送ID
     * @return 配送
     */
    private DeliveryEntity getDeliveryOrThrow(UUID deliveryId) {
        return deliveryService.getDeliveryById(deliveryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "配送が見つかりません。"));
    }

    /**
     * 配送履歴IDで配送履歴を取得
     *
     * @param id 配送履歴ID
     * @return 配送履歴
     */
    private DeliveryHistoryEntity getDeliveryHistoryOrThrow(UUID id) {
        return deliveryHistoryService.getDeliveryHistoryById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "配送履歴が見つかりません。"));
    }
}
