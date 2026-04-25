package jp.co.shimizutdev.phoneorderapi.infrastructure.log;

import jp.co.shimizutdev.phoneorderapi.application.order.OrderService;
import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
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
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
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
     * メソッド開始終了ログに戻り値型と件数が出力されること。
     *
     * Given 注文データが複数件登録されている
     * When 注文一覧取得メソッドを実行する
     * Then 開始終了ログと戻り値型および件数が出力される
     * </pre>
     */
    @Test
    @DisplayName("メソッド開始終了ログに戻り値型と件数が出力されること")
    @Sql(statements = {
        "insert into orders (id, order_code, ordered_at, order_status, created_by, updated_by) values "
            + "('11111111-1111-1111-1111-111111111111', 'ORD000001', now(), '001', 'system', 'system')",
        "insert into orders (id, order_code, ordered_at, order_status, created_by, updated_by) values "
            + "('22222222-2222-2222-2222-222222222222', 'ORD000002', now(), '002', 'system', 'system')"
    })
    void shouldLogMethodStartAndReturnType(final CapturedOutput output) {
        orderService.getOrders();

        assertThat(output)
            .contains("[method start] methodName: OrderService#getOrders")
            .contains("[method end] methodName: OrderService#getOrders")
            .contains("[method end] return value: [List<Order>(size=2)]")
            .contains("\"orderCode\":{\"value\":\"ORD000001\"}")
            .contains("\"orderCode\":{\"value\":\"ORD000002\"}");
    }
}
