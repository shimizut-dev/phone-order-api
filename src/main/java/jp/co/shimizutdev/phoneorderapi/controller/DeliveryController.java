package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.DeliveryRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.DeliveryResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.DeliveryMapper;
import jp.co.shimizutdev.phoneorderapi.service.DeliveryService;
import jp.co.shimizutdev.phoneorderapi.service.OrderService;
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
 * 配送Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final OrderService orderService;

    /**
     * 配送を検索
     *
     * @return 配送Response DTO List
     */
    @GetMapping("/deliveries")
    public List<DeliveryResponseDto> searchDeliveries() {
        return DeliveryMapper.toResponseDtoList(deliveryService.searchDeliveries());
    }

    /**
     * 配送IDで配送を参照
     *
     * @param id 配送ID
     * @return 配送Response DTO
     */
    @GetMapping("/deliveries/{id}")
    public DeliveryResponseDto getDeliveryById(@PathVariable UUID id) {
        return DeliveryMapper.toResponseDto(getDeliveryOrThrow(id));
    }

    /**
     * 注文に配送を登録
     *
     * @param orderId 注文ID
     * @param requestDto 配送Request DTO
     * @return 配送Response DTO
     */
    @PostMapping("/orders/{orderId}/deliveries")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryResponseDto createDelivery(@PathVariable UUID orderId,
                                              @Valid @RequestBody DeliveryRequestDto requestDto) {
        OrderEntity order = getOrderOrThrow(orderId);
        DeliveryEntity delivery = DeliveryMapper.toEntity(requestDto, order);
        return DeliveryMapper.toResponseDto(deliveryService.saveDelivery(delivery));
    }

    /**
     * 注文IDで注文を取得
     *
     * @param orderId 注文ID
     * @return 注文
     */
    private OrderEntity getOrderOrThrow(UUID orderId) {
        return orderService.getOrderById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "注文が見つかりません。"));
    }

    /**
     * 配送IDで配送を取得
     *
     * @param id 配送ID
     * @return 配送
     */
    private DeliveryEntity getDeliveryOrThrow(UUID id) {
        return deliveryService.getDeliveryById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "配送が見つかりません。"));
    }
}
