package jp.co.shimizutdev.phoneorderapi.presentation.order;

import java.time.OffsetDateTime;

/**
 * 注文レスポンス
 *
 * @param orderCode   注文コード
 * @param orderedAt   注文日時
 * @param orderStatus 注文ステータス
 */
public record OrderResponse(
    String orderCode,
    OffsetDateTime orderedAt,
    String orderStatus
) {
}
