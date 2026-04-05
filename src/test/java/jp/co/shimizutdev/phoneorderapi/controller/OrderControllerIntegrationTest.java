package jp.co.shimizutdev.phoneorderapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 注文Controller統合テスト
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Sql(
        statements = {
                "delete from orders where order_code in ('ORD000001', 'ORD999999')"
        },
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        statements = {
                "delete from orders where order_code in ('ORD000001', 'ORD999999')"
        },
        executionPhase = ExecutionPhase.AFTER_TEST_METHOD
)
class OrderControllerIntegrationTest {

    /**
     * PostgreSQLコンテナ
     */
    @Container
    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("phone_order_api_test_db")
                    .withUsername("test")
                    .withPassword("test");

    /**
     * MockMvc
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * テスト用データソースを設定
     *
     * @param registry DynamicPropertyRegistry
     */
    @DynamicPropertySource
    static void registerDataSourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRESQL_CONTAINER::getDriverClassName);
    }

    /**
     * 注文コードで注文を参照できること
     *
     * @throws Exception 例外
     */
    @Test
    @Sql(statements = {
            "insert into orders (order_code, ordered_at, order_status, created_by, updated_by) " +
                    "values ('ORD000001', now(), '001', 'system', 'system')"
    })
    void shouldReturnOrderWhenOrderCodeExists() throws Exception {
        mockMvc.perform(get("/api/orders/order-code/{orderCode}", "ORD000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value("ORD000001"))
                .andExpect(jsonPath("$.orderStatus").value("001"));
    }

    /**
     * 注文コードに対応する注文が存在しない場合は404を返すこと
     *
     * @throws Exception 例外
     */
    @Test
    void shouldReturnNotFoundWhenOrderCodeDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/orders/order-code/{orderCode}", "ORD999999"))
                .andExpect(status().isNotFound());
    }
}
