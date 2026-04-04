package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.DeliveryLineRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.DeliveryLineResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryEntity;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryLineEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.DeliveryLineMapper;
import jp.co.shimizutdev.phoneorderapi.service.DeliveryLineService;
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
 * 配送明細Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryLineController {

    private final DeliveryLineService deliveryLineService;
    private final DeliveryService deliveryService;

    /**
     * 配送明細を検索
     *
     * @return 配送明細Response DTO List
     */
    @GetMapping("/delivery-lines")
    public List<DeliveryLineResponseDto> searchDeliveryLines() {
        return DeliveryLineMapper.toResponseDtoList(deliveryLineService.searchDeliveryLines());
    }

    /**
     * 配送明細IDで配送明細を参照
     *
     * @param id 配送明細ID
     * @return 配送明細Response DTO
     */
    @GetMapping("/delivery-lines/{id}")
    public DeliveryLineResponseDto getDeliveryLineById(@PathVariable UUID id) {
        return DeliveryLineMapper.toResponseDto(getDeliveryLineOrThrow(id));
    }

    /**
     * 配送に配送明細を登録
     *
     * @param deliveryId 配送ID
     * @param requestDto 配送明細Request DTO
     * @return 配送明細Response DTO
     */
    @PostMapping("/deliveries/{deliveryId}/delivery-lines")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryLineResponseDto createDeliveryLine(@PathVariable UUID deliveryId,
                                                      @Valid @RequestBody DeliveryLineRequestDto requestDto) {
        DeliveryEntity delivery = getDeliveryOrThrow(deliveryId);
        DeliveryLineEntity deliveryLine = DeliveryLineMapper.toEntity(requestDto, delivery);
        return DeliveryLineMapper.toResponseDto(deliveryLineService.saveDeliveryLine(deliveryLine));
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
     * 配送明細IDで配送明細を取得
     *
     * @param id 配送明細ID
     * @return 配送明細
     */
    private DeliveryLineEntity getDeliveryLineOrThrow(UUID id) {
        return deliveryLineService.getDeliveryLineById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "配送明細が見つかりません。"));
    }
}
