package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.PhoneRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.PhoneResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;
import jp.co.shimizutdev.phoneorderapi.entity.PhoneEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.PhoneMapper;
import jp.co.shimizutdev.phoneorderapi.service.OrderLineService;
import jp.co.shimizutdev.phoneorderapi.service.PhoneService;
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
 * 移動機Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;
    private final OrderLineService orderLineService;

    /**
     * 移動機を検索
     *
     * @return 移動機Response DTO List
     */
    @GetMapping("/phones")
    public List<PhoneResponseDto> searchPhones() {
        return phoneService.searchPhones().stream()
                .map(PhoneMapper::toResponseDto)
                .toList();
    }

    /**
     * 移動機IDで移動機を参照
     *
     * @param id 移動機ID
     * @return 移動機Response DTO
     */
    @GetMapping("/phones/{id}")
    public PhoneResponseDto getPhoneById(@PathVariable UUID id) {
        return PhoneMapper.toResponseDto(getPhoneOrThrow(id));
    }

    /**
     * 注文明細に移動機を登録
     *
     * @param orderLineId 注文明細ID
     * @param requestDto 移動機Request DTO
     * @return 移動機Response DTO
     */
    @PostMapping("/order-lines/{orderLineId}/phones")
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneResponseDto createPhone(@PathVariable UUID orderLineId,
                                        @Valid @RequestBody PhoneRequestDto requestDto) {
        OrderLineEntity orderLine = getOrderLineOrThrow(orderLineId);
        PhoneEntity phone = PhoneMapper.toEntity(requestDto, orderLine);
        return PhoneMapper.toResponseDto(phoneService.savePhone(phone));
    }

    /**
     * 移動機IDで移動機を取得
     *
     * @param id 移動機ID
     * @return 移動機
     */
    private PhoneEntity getPhoneOrThrow(UUID id) {
        return phoneService.getPhoneById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "移動機が見つかりません。"));
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
