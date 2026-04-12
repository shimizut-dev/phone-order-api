package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * 注文リクエスト
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class OrderRequest {

    /**
     * 注文日時
     */
    @NotNull(message = "{validation.order.orderedAt.required}")
    private OffsetDateTime orderedAt;
}
