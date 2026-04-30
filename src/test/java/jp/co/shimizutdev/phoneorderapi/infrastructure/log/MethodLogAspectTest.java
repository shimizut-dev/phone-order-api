package jp.co.shimizutdev.phoneorderapi.infrastructure.log;

import jp.co.shimizutdev.phoneorderapi.application.order.OrderService;
import jp.co.shimizutdev.phoneorderapi.domain.common.PagingCondition;
import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
import jp.co.shimizutdev.phoneorderapi.support.ResetLogLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * メソッド開始/終了ログ出力アスペクトテスト
 */
@SpringBootTest(properties = "logging.level.jp.co.shimizutdev.phoneorderapi.infrastructure.log.MethodLogAspect=DEBUG")
@ExtendWith(OutputCaptureExtension.class)
@ResetLogLevel(MethodLogAspect.class)
@SqlMergeMode(MergeMode.MERGE)
@Sql(
    scripts = "classpath:sql/cleanup/cleanup-transaction-tables.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = "classpath:sql/cleanup/cleanup-transaction-tables.sql",
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD
)
class MethodLogAspectTest extends AbstractPostgreSQLTest {

    /**
     * 注文サービス
     */
    @Autowired
    private OrderService orderService;

    /**
     * <pre>
     * Given 注文データが複数件登録されている
     * When 注文一覧取得メソッドを実行する
     * Then 開始終了ログ、戻り値型、注文コードが出力される
     * </pre>
     */
    @Test
    @DisplayName("メソッド開始終了ログに戻り値型と注文コードが出力されること")
    @Sql(statements = {
        "insert into orders (id, order_code, ordered_at, order_status, created_by, updated_by) values "
            + "('11111111-1111-1111-1111-111111111111', 'ORD000001', '2026-04-09T10:00:00+09:00', '001', 'system', 'system')",
        "insert into orders (id, order_code, ordered_at, order_status, created_by, updated_by) values "
            + "('22222222-2222-2222-2222-222222222222', 'ORD000002', '2026-04-09T11:00:00+09:00', '002', 'system', 'system')"
    })
    void shouldLogMethodStartEndReturnTypeAndOrderCodes(final CapturedOutput output) {
        orderService.getOrders(PagingCondition.of(0, 20));

        assertThat(output)
            .contains("[method start] methodName: OrderService#getOrders")
            .contains("[method start] arguments: [Object[]] [{\"page\":0,\"size\":20}]")
            .contains("[method end] methodName: OrderService#getOrders")
            .contains("[method end] duration(ms):")
            .contains("[method end] return value: [PageResult<Order>]")
            .contains("\"items\":[")
            .contains("\"orderId\":{\"value\":\"22222222-2222-2222-2222-222222222222\"}")
            .contains("\"orderCode\":{\"value\":\"ORD000002\"}")
            .contains("\"orderedAt\":{\"value\":\"2026-04-09T02:00:00Z\"}")
            .contains("\"orderStatus\":\"UNDER_REVIEW\"")
            .contains("\"version\":{\"value\":0}")
            .contains("\"orderId\":{\"value\":\"11111111-1111-1111-1111-111111111111\"}")
            .contains("\"orderCode\":{\"value\":\"ORD000001\"}")
            .contains("\"orderedAt\":{\"value\":\"2026-04-09T01:00:00Z\"}")
            .contains("\"orderStatus\":\"RECEIVED\"")
            .contains("\"version\":{\"value\":0}")
            .contains("\"page\":0")
            .contains("\"size\":20")
            .contains("\"totalElements\":2")
            .contains("\"totalPages\":1");
    }
}
