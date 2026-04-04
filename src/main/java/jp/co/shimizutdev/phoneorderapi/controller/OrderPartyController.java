package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.OrderPartyRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.OrderPartyResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderPartyEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.OrderPartyMapper;
import jp.co.shimizutdev.phoneorderapi.service.OrderPartyService;
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
 * 注文関係者Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderPartyController {

    private final OrderPartyService orderPartyService;
    private final OrderService orderService;

    /**
     * 注文関係者を検索
     *
     * @return 注文関係者Response DTO List
     */
    @GetMapping("/order-parties")
    public List<OrderPartyResponseDto> searchOrderParties() {
        return OrderPartyMapper.toResponseDtoList(orderPartyService.searchOrderParties());
    }

    /**
     * 注文関係者IDで注文関係者を参照
     *
     * @param id 注文関係者ID
     * @return 注文関係者Response DTO
     */
    @GetMapping("/order-parties/{id}")
    public OrderPartyResponseDto getOrderPartyById(@PathVariable UUID id) {
        return OrderPartyMapper.toResponseDto(getOrderPartyOrThrow(id));
    }

    /**
     * 注文に注文関係者を登録
     *
     * @param orderId 注文ID
     * @param requestDto 注文関係者Request DTO
     * @return 注文関係者Response DTO
     */
    @PostMapping("/orders/{orderId}/order-parties")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderPartyResponseDto createOrderParty(@PathVariable UUID orderId,
                                                  @Valid @RequestBody OrderPartyRequestDto requestDto) {
        OrderEntity order = getOrderOrThrow(orderId);
        OrderPartyEntity orderParty = OrderPartyMapper.toEntity(requestDto, order);
        return OrderPartyMapper.toResponseDto(orderPartyService.saveOrderParty(orderParty));
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
     * 注文関係者IDで注文関係者を取得
     *
     * @param id 注文関係者ID
     * @return 注文関係者
     */
    private OrderPartyEntity getOrderPartyOrThrow(UUID id) {
        return orderPartyService.getOrderPartyById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "注文関係者が見つかりません。"));
    }
}
