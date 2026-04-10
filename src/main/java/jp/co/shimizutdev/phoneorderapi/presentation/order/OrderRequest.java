package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * 注文リクエスト
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {

    /**
     * 注文日時
     */
    @NotNull(message = "注文日時は必須です。")
    private OffsetDateTime orderedAt;
}
