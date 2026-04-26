package jp.co.shimizutdev.phoneorderapi.infrastructure.codegen;

import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCode;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCodeGenerator;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 連番形式の注文コード採番
 */
@Component
@RequiredArgsConstructor
public class SequenceOrderCodeGenerator implements OrderCodeGenerator {

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
        return OrderCode.of(orderJpaRepository.nextOrderCode());
    }
}
