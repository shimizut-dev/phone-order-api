package jp.co.shimizutdev.phoneorderapi.application.order;

import java.util.Objects;
import jp.co.shimizutdev.phoneorderapi.domain.common.PageResult;
import jp.co.shimizutdev.phoneorderapi.domain.common.PagingCondition;
import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCannotBeCancelledException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCode;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCodeGenerator;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderNotFoundException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderRepository;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderVersionConflictException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderedAt;
import jp.co.shimizutdev.phoneorderapi.domain.order.Version;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 注文サービス */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  /** 注文リポジトリ */
  private final OrderRepository orderRepository;

  /** 注文コード採番 */
  private final OrderCodeGenerator orderCodeGenerator;

  /**
   * 注文一覧を取得する
   *
   * @param pagingCondition ページング条件
   * @return 注文日時降順、同一日時は注文コード降順のページング済み注文一覧。存在しない場合または範囲外ページの場合は空のページ
   * @throws NullPointerException ページング条件が null の場合
   */
  public PageResult<Order> getOrders(final PagingCondition pagingCondition) {
    return orderRepository.findAll(Objects.requireNonNull(pagingCondition, "ページング条件は必須です。"));
  }

  /**
   * 注文コードで注文を取得する
   *
   * @param orderCode 注文コード
   * @return 注文
   * @throws NullPointerException 注文コードが null の場合
   * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
   */
  public Order getOrderByOrderCode(final OrderCode orderCode) {
    return findOrderByOrderCode(orderCode);
  }

  /**
   * 注文を登録する
   *
   * @param orderedAt 注文日時
   * @return 注文
   * @throws NullPointerException 注文日時が null の場合
   */
  @Transactional
  public Order createOrder(final OrderedAt orderedAt) {
    OrderCode orderCode = orderCodeGenerator.generate();
    Order order = Order.create(orderCode, Objects.requireNonNull(orderedAt, "注文日時は必須です。"));
    return orderRepository.create(order);
  }

  /**
   * 注文をキャンセルする
   *
   * @param orderCode 注文コード
   * @param version キャンセル要求時の注文バージョン
   * @return 注文
   * @throws NullPointerException 注文コードまたはバージョンが null の場合
   * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
   * @throws OrderCannotBeCancelledException 注文の状態によりキャンセルできない場合
   * @throws OrderVersionConflictException 注文のバージョンが一致しない場合
   */
  @Transactional
  public Order cancelOrder(final OrderCode orderCode, final Version version) {
    Order order = findOrderByOrderCode(orderCode);

    return orderRepository.update(order.cancel(Objects.requireNonNull(version, "バージョンは必須です。")));
  }

  /**
   * 注文コードで注文を取得し、存在しない場合は注文未存在例外を発生させる
   *
   * @param orderCode 注文コード
   * @return 注文
   * @throws NullPointerException 注文コードが null の場合
   * @throws OrderNotFoundException 注文コードに対応する注文が存在しない場合
   */
  private Order findOrderByOrderCode(final OrderCode orderCode) {
    OrderCode validOrderCode = Objects.requireNonNull(orderCode, "注文コードは必須です。");
    return orderRepository
        .findByOrderCode(validOrderCode)
        .orElseThrow(() -> OrderNotFoundException.byOrderCode(validOrderCode.getValue()));
  }
}
