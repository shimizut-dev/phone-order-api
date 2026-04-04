package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.OrderLineRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.OrderLineResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.OrderLineMapper;
import jp.co.shimizutdev.phoneorderapi.service.OrderLineService;
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
 * 注文明細Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService orderLineService;
    private final OrderService orderService;

    /**
     * 注文明細を検索
     *
     * @return 注文明細Response DTO List
     */
    @GetMapping("/order-lines")
    public List<OrderLineResponseDto> searchOrderLines() {
        return OrderLineMapper.toResponseDtoList(orderLineService.searchOrderLines());
    }

    /**
     * 注文明細IDで注文明細を参照
     *
     * @param id 注文明細ID
     * @return 注文明細Response DTO
     */
    @GetMapping("/order-lines/{id}")
    public OrderLineResponseDto getOrderLineById(@PathVariable UUID id) {
        return OrderLineMapper.toResponseDto(getOrderLineOrThrow(id));
    }

    /**
     * 注文に注文明細を登録
     *
     * @param orderId 注文ID
     * @param requestDto 注文明細Request DTO
     * @return 注文明細Response DTO
     */
    @PostMapping("/orders/{orderId}/order-lines")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderLineResponseDto createOrderLine(@PathVariable UUID orderId,
                                                @Valid @RequestBody OrderLineRequestDto requestDto) {
        OrderEntity order = getOrderOrThrow(orderId);
        OrderLineEntity orderLine = OrderLineMapper.toEntity(requestDto, order);
        return OrderLineMapper.toResponseDto(orderLineService.saveOrderLine(orderLine));
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
     * 注文明細IDで注文明細を取得
     *
     * @param id 注文明細ID
     * @return 注文明細
     */
    private OrderLineEntity getOrderLineOrThrow(UUID id) {
        return orderLineService.getOrderLineById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "注文明細が見つかりません。"));
    }
}
