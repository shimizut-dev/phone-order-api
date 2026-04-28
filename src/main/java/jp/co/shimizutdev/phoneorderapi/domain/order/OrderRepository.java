package jp.co.shimizutdev.phoneorderapi.domain.order;

import java.util.List;
import java.util.Optional;

/**
 * 注文リポジトリ
 */
public interface OrderRepository {

    /**
     * 注文一覧を取得する
     *
     * @return 注文一覧。存在しない場合は空リスト
     */
    List<Order> findAll();

    /**
     * 注文コードで注文を取得する
     *
     * @param orderCode 注文コード
     * @return 注文。存在しない場合は空の Optional
     */
    Optional<Order> findByOrderCode(OrderCode orderCode);

    /**
     * 注文を登録する
     *
     * @param order 注文
     * @return 注文
     */
    Order create(Order order);

    /**
     * 注文を更新する
     *
     * @param order 注文
     * @return 注文
     * @throws OrderVersionConflictException 更新対象の注文バージョンが一致しない場合
     */
    Order update(Order order);
}
