package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * 注文リクエスト
 *
 * @param orderedAt 注文日時
 */
public record OrderRequest(
    @NotNull(message = "{validation.order.orderedAt.required}")
    OffsetDateTime orderedAt
) {
}
