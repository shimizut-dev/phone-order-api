package jp.co.shimizutdev.phoneorderapi.infrastructure.codegen;

import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCode;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCodeGenerator;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaEntity;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 連番形式の注文コード採番
 */
@Component
@RequiredArgsConstructor
public class SequentialOrderCodeGenerator implements OrderCodeGenerator {

    /**
     * 注文コードプレフィックス
     */
    private static final String ORDER_CODE_PREFIX = "ORD";

    /**
     * 連番桁数
     */
    private static final int NUMBER_OF_DIGITS = 6;

    /**
     * 注文リポジトリ
     */
    private final OrderJpaRepository orderJpaRepository;

    /**
     * 注文コードを採番する
     *
     * @return 注文コード
     */
    @Override
    public OrderCode generate() {
        return orderJpaRepository.findTopByOrderCodeStartingWithOrderByOrderCodeDesc(ORDER_CODE_PREFIX)
            .map(OrderJpaEntity::getOrderCode)
            .map(this::increment)
            .orElseGet(() -> OrderCode.of(formatOrderCode(1)));
    }

    /**
     * 注文コードをインクリメントする
     *
     * @param currentOrderCode 現在の注文コード
     * @return 次の注文コード
     */
    private OrderCode increment(final String currentOrderCode) {
        if (currentOrderCode == null || !currentOrderCode.startsWith(ORDER_CODE_PREFIX)) {
            throw new IllegalStateException("不正な注文コードです。");
        }

        String numericPart = currentOrderCode.substring(ORDER_CODE_PREFIX.length());

        try {
            int currentNumber = Integer.parseInt(numericPart);
            return OrderCode.of(formatOrderCode(currentNumber + 1));
        } catch (NumberFormatException ex) {
            throw new IllegalStateException("注文コードの連番部が不正です。", ex);
        }
    }

    /**
     * 注文コードを整形する
     *
     * @param sequence 連番
     * @return 注文コード
     */
    private String formatOrderCode(final int sequence) {
        return ORDER_CODE_PREFIX + String.format("%0" + NUMBER_OF_DIGITS + "d", sequence);
    }
}
