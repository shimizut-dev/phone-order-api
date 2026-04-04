package jp.co.shimizutdev.phoneorderapi.controller;

import jakarta.validation.Valid;
import jp.co.shimizutdev.phoneorderapi.dto.request.LineRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.LineResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.LineEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;
import jp.co.shimizutdev.phoneorderapi.mapper.LineMapper;
import jp.co.shimizutdev.phoneorderapi.service.LineService;
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
 * 回線Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;
    private final OrderLineService orderLineService;

    /**
     * 回線を検索
     *
     * @return 回線Response DTO List
     */
    @GetMapping("/lines")
    public List<LineResponseDto> searchLines() {
        return lineService.searchLines().stream()
                .map(LineMapper::toResponseDto)
                .toList();
    }

    /**
     * 回線IDで回線を参照
     *
     * @param id 回線ID
     * @return 回線Response DTO
     */
    @GetMapping("/lines/{id}")
    public LineResponseDto getLineById(@PathVariable UUID id) {
        return LineMapper.toResponseDto(getLineOrThrow(id));
    }

    /**
     * 注文明細に回線を登録
     *
     * @param orderLineId 注文明細ID
     * @param requestDto 回線Request DTO
     * @return 回線Response DTO
     */
    @PostMapping("/order-lines/{orderLineId}/lines")
    @ResponseStatus(HttpStatus.CREATED)
    public LineResponseDto createLine(@PathVariable UUID orderLineId,
                                      @Valid @RequestBody LineRequestDto requestDto) {
        OrderLineEntity orderLine = getOrderLineOrThrow(orderLineId);
        LineEntity line = LineMapper.toEntity(requestDto, orderLine);
        return LineMapper.toResponseDto(lineService.saveLine(line));
    }

    /**
     * 回線IDで回線を取得
     *
     * @param id 回線ID
     * @return 回線
     */
    private LineEntity getLineOrThrow(UUID id) {
        return lineService.getLineById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "回線が見つかりません。"));
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
