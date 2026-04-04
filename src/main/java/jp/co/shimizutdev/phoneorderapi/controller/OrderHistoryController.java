package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.OrderHistoryRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.OrderHistoryResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderHistoryEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.OrderHistoryMapper;
import jp.co.shimizutdev.phoneorderapi.service.OrderHistoryService;
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
 * 注文履歴Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;
    private final OrderService orderService;

    /**
     * 注文履歴を検索
     *
     * @return 注文履歴Response DTO List
     */
    @GetMapping("/order-histories")
    public List<OrderHistoryResponseDto> searchOrderHistories() {
        return OrderHistoryMapper.toResponseDtoList(orderHistoryService.searchOrderHistories());
    }

    /**
     * 注文履歴IDで注文履歴を参照
     *
     * @param id 注文履歴ID
     * @return 注文履歴Response DTO
     */
    @GetMapping("/order-histories/{id}")
    public OrderHistoryResponseDto getOrderHistoryById(@PathVariable UUID id) {
        return OrderHistoryMapper.toResponseDto(getOrderHistoryOrThrow(id));
    }

    /**
     * 注文に注文履歴を登録
     *
     * @param orderId 注文ID
     * @param requestDto 注文履歴Request DTO
     * @return 注文履歴Response DTO
     */
    @PostMapping("/orders/{orderId}/order-histories")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderHistoryResponseDto createOrderHistory(@PathVariable UUID orderId,
                                                      @Valid @RequestBody OrderHistoryRequestDto requestDto) {
        OrderEntity order = getOrderOrThrow(orderId);
        OrderHistoryEntity orderHistory = OrderHistoryMapper.toEntity(requestDto, order);
        return OrderHistoryMapper.toResponseDto(orderHistoryService.saveOrderHistory(orderHistory));
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
     * 注文履歴IDで注文履歴を取得
     *
     * @param id 注文履歴ID
     * @return 注文履歴
     */
    private OrderHistoryEntity getOrderHistoryOrThrow(UUID id) {
        return orderHistoryService.getOrderHistoryById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "注文履歴が見つかりません。"));
    }
}
