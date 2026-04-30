package jp.co.shimizutdev.phoneorderapi.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/** テストクラスで変更したログレベルをテスト後に戻すアノテーション */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ResetLogLevelExtension.class)
public @interface ResetLogLevel {

  /**
   * ログレベルを戻すロガークラス
   *
   * @return ロガークラス
   */
  Class<?>[] value();
}
