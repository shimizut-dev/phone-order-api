package jp.co.shimizutdev.phoneorderapi.domain.order;

import lombok.Getter;

/**
 * 注文ステータス
 */
@Getter
public enum OrderStatus {

    RECEIVED("001", "受付"),
    UNDER_REVIEW("002", "審査中"),
    ARRANGING("003", "手配中"),
    WAITING_SHIPMENT("004", "出荷待ち"),
    COMPLETED("005", "完了"),
    CANCELLED("006", "キャンセル");

    /**
     * コード
     */
    private final String code;

    /**
     * 名称
     */
    private final String name;

    /**
     * コンストラクタ
     *
     * @param code コード
     * @param name 名称
     */
    OrderStatus(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * コードから注文ステータスへ生成する
     *
     * @param code コード
     * @return 注文ステータス
     */
    public static OrderStatus fromCode(final String code) {
        return switch (code) {
            case "001" -> OrderStatus.RECEIVED;
            case "002" -> OrderStatus.UNDER_REVIEW;
            case "003" -> OrderStatus.ARRANGING;
            case "004" -> OrderStatus.WAITING_SHIPMENT;
            case "005" -> OrderStatus.COMPLETED;
            case "006" -> OrderStatus.CANCELLED;
            default -> throw new IllegalArgumentException("注文ステータスコードが不正です。");
        };
    }

    /**
     * 注文ステータスコードが有効か判定する
     *
     * @param code 注文ステータスコード
     * @return 判定結果
     */
    public static boolean isValidCode(final String code) {
        return switch (code) {
            case "001", "002", "003", "004", "005", "006" -> true;
            default -> false;
        };
    }
}
