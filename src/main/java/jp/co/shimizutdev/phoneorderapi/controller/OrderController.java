package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.OrderRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.OrderResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.OrderMapper;
import jp.co.shimizutdev.phoneorderapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 注文Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 注文を検索
     *
     * @return 注文Response DTO List
     */
    @GetMapping("/orders")
    public List<OrderResponseDto> searchOrders() {
        return OrderMapper.toResponseDtoList(orderService.searchOrders());
    }

    /**
     * 注文コードで注文を参照
     *
     * @param orderCode 注文コード
     * @return 注文Response DTO
     */
    @GetMapping("/orders/order-code/{orderCode}")
    public OrderResponseDto getOrderByOrderCode(@PathVariable String orderCode) {
        OrderEntity order = orderService.getOrderByOrderCode(orderCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "注文が見つかりません。"));
        return OrderMapper.toResponseDto(order);
    }

    /**
     * 注文を登録
     *
     * @param requestDto 注文Request DTO
     * @return 注文Response DTO
     */
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@Valid @RequestBody OrderRequestDto requestDto) {
        OrderEntity order = OrderMapper.toEntity(requestDto);
        return OrderMapper.toResponseDto(orderService.saveOrder(order));
    }
}
