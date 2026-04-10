package jp.co.shimizutdev.phoneorderapi.domain.order;

/**
 * 注文コード採番
 */
public interface OrderCodeGenerator {

    /**
     * 注文コードを採番する
     *
     * @return 注文コード
     */
    OrderCode generate();
}
