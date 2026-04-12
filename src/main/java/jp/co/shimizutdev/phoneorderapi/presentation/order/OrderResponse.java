package jp.co.shimizutdev.phoneorderapi.presentation.order;

import lombok.*;

import java.time.OffsetDateTime;

/**
 * 注文レスポンス
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class OrderResponse {

    /**
     * 注文コード
     */
    private String orderCode;

    /**
     * 注文日時
     */
    private OffsetDateTime orderedAt;

    /**
     * 注文ステータス
     */
    private String orderStatus;
}


